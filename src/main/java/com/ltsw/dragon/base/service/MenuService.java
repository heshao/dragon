package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.entity.MenuRole;
import com.ltsw.dragon.base.entity.Role;
import com.ltsw.dragon.base.repository.MenuRepository;
import com.ltsw.dragon.base.repository.MenuRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.stream.Collectors;

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

    private AccessDecisionManager accessDecisionManager;
    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new HashMap<>(20);
    ;

    /**
     * 初始化加载菜单权限
     */
    @PostConstruct
    public void init() {

        List<MenuRole> menuRoles = menuRoleRepository.findAll();
        menuRoles.forEach(menuRole -> {
            Menu menu = menuRole.getMenu();
            if (menu != null && !StringUtils.isEmpty(menu.getUri())) {

                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(menu.getUri());
                Collection<ConfigAttribute> configAttributes;
                if (requestMap.containsKey(requestMatcher)) {
                    configAttributes = requestMap.get(requestMatcher);
                } else {
                    configAttributes = new ArrayList<>();
                    requestMap.put(requestMatcher, configAttributes);
                }
                configAttributes.addAll(SecurityConfig.createList(menuRole.getRole().getAuthority()));
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

    public Optional<Menu> get(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<Menu> optional = menuRepository.findById(id);
        optional.ifPresent(menu -> {
            List<MenuRole> menuRoles = menuRoleRepository.findByMenuId(id);
            List<Role> roles = new ArrayList<>();
            menuRoles.forEach(menuRole -> roles.add(menuRole.getRole()));
            menu.setRoles(roles);
        });
        return optional;
    }

    public Page<Menu> findAll(Pageable pageable) {
        return menuRepository.findAll(pageable);
    }

    /**
     * 获取当前用户已授权菜单
     * <p>可用、可见</p>
     *
     * @return
     */
    public List<Menu> findAllWithGranted() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Menu filter = new Menu();
        filter.setEnabled(true);
        filter.setVisible(true);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("sort");
        Example<Menu> ex = Example.of(filter, matcher);
        return menuRepository.findAll(ex).stream().filter(menu -> {
            if (StringUtils.isEmpty(menu.getUri())) {
                return true;
            }
            FilterInvocation object = new FilterInvocation(menu.getUri(), "");
            Collection<ConfigAttribute> attributes = getAttributes(object);
            try {
                accessDecisionManager.decide(authentication, object, attributes);
                return true;
            } catch (AccessDeniedException accessDeniedException) {
                return false;
            }
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(Menu menu) {
        menuRepository.save(menu);
        //暂时取消角色关联保存
//        menuRoleRepository.deleteByMenuId(menu.getId());
//        Collection<Role> roles = menu.getRoles();
//        roles.forEach(role -> {
//            MenuRole menuRole = new MenuRole();
//            menuRole.setMenu(menu);
//            menuRole.setRole(role);
//            menuRoleRepository.save(menuRole);
//        });
        refresh();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(long id) {
        menuRepository.deleteById(id);
        menuRoleRepository.deleteByMenuId(id);
        refresh();
    }

    public MenuService set(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
        return this;
    }

    public MenuService set(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
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
