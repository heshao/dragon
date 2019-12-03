package com.ltsw.dragon.base.security;

import com.ltsw.dragon.base.entity.Menu;
import com.ltsw.dragon.base.entity.MenuRole;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author heshaobing
 */
@Component
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private AccessDecisionManager accessDecisionManager;
    private FilterInvocationSecurityMetadataSource securityMetadataSource;
    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new HashMap<>(20);

    public SecurityMetadataSource set(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
        return this;
    }

    public SecurityMetadataSource set(AccessDecisionManager accessDecisionManager) {
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


    public boolean decide(String uri) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FilterInvocation object = new FilterInvocation(uri, "");
        Collection<ConfigAttribute> attributes = getAttributes(object);
        try {
            accessDecisionManager.decide(authentication, object, attributes);
            return true;
        } catch (AccessDeniedException accessDeniedException) {
            return false;
        }
    }

    private void put(String uri, String authority) {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
        Collection<ConfigAttribute> configAttributes;
        if (requestMap.containsKey(requestMatcher)) {
            configAttributes = requestMap.get(requestMatcher);
        } else {
            configAttributes = new ArrayList<>();
            requestMap.put(requestMatcher, configAttributes);
        }
        configAttributes.addAll(SecurityConfig.createList(authority));
    }

    private void remove(String uri) {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(uri);
        requestMap.remove(requestMatcher);
    }

    public void put(List<MenuRole> menuRoles) {
        menuRoles.forEach(menuRole -> {
            Menu menu = menuRole.getMenu();
            if (menu != null && !StringUtils.isEmpty(menu.getUri())) {
                put(menu.getUri(), menuRole.getRole().getAuthority());
            }
        });
    }

    public void remove(Menu menu) {
        if (menu != null && !StringUtils.isEmpty(menu.getUri())) {
            remove(menu.getUri());
        }
    }
}
