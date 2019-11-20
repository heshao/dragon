package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.entity.MenuRole;
import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.repository.MenuRepository;
import com.ltsw.dragon.base.repository.MenuRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author heshaobing
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class MenuService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuRoleRepository menuRoleRepository;

    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    @PostConstruct
    public void init() {
        requestMap = new HashMap<>(20);
        List<Menu> menus = menuRepository.findAll();
        menus.forEach(menu -> {
            if (!StringUtils.isEmpty(menu.getUri())) {

                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(menu.getUri());
                Collection<MenuRole> menuRoles = menuRoleRepository.findByMenu_Id(menu.getId());

                List<String> authorities = new ArrayList<>();
                menuRoles.forEach(menuRole -> authorities.add(menuRole.getRole().getAuthority()));

                requestMap.put(requestMatcher, SecurityConfig.createList(authorities.toArray(new String[0])));
            }
        });
    }

    public void refresh() {
        log.info("刷新菜单权限——开始");
        long start = System.currentTimeMillis();
        requestMap.clear();
        init();
        long end = System.currentTimeMillis();
        log.info("刷新菜单权限——结束：耗时{}ms", end - start);
    }

    public Optional<Menu> get(long id) {
        Optional<Menu> optional = menuRepository.findById(id);
        optional.ifPresent(menu -> {
            List<MenuRole> menuRoles = menuRoleRepository.findByMenu_Id(id);
            List<Role> roles = new ArrayList<>();
            menuRoles.forEach(menuRole -> roles.add(menuRole.getRole()));
            menu.setRoles(roles);
        });
        return optional;
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Menu menu) {
        menuRepository.save(menu);
        menuRoleRepository.deleteByMenu_Id(menu.getId());
        Collection<Role> roles = menu.getRoles();
        roles.forEach(role -> {
            MenuRole menuRole = new MenuRole();
            menuRole.setMenu(menu);
            menuRole.setRole(role);
            menuRoleRepository.save(menuRole);
        });
        refresh();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        menuRepository.deleteById(id);
        menuRoleRepository.deleteByMenu_Id(id);
        refresh();
    }

    public MenuService set(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
        return this;
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
                .entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return this.securityMetadataSource.getAttributes(object);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
