package com.ltsw.dragon.base.service;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.entity.MenuRole;
import com.ltsw.dragon.base.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author heshaobing
 */
@Service
public class MenuService implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuRepository menuRepository;

    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    @PostConstruct
    public void init() {
        requestMap = new HashMap<>(20);
        List<Menu> menus = menuRepository.findAll();
        menus.forEach(menu -> {
            if (!StringUtils.isEmpty(menu.getUri())) {

                AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(menu.getUri());
                Collection<MenuRole> menuRoles = menu.getMenuRoles();
                List<String> authorities = new ArrayList<>();
                menuRoles.forEach(menuRole -> authorities.add(menuRole.getRole().getAuthority()));
                requestMap.put(requestMatcher, SecurityConfig.createList(hasAnyRole(authorities.toArray(new String[0]))));
            }
        });
    }

    private String hasAnyRole(String... authorities) {
        String anyAuthorities = StringUtils.arrayToDelimitedString(authorities,
                "','ROLE_");
        return "hasAnyRole('ROLE_" + anyAuthorities + "')";
    }

    public void refresh() {
        requestMap.clear();
        init();
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
