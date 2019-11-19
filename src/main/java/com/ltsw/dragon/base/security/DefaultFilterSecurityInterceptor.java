package com.ltsw.dragon.base.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * @author heshaobing
 */
public class DefaultFilterSecurityInterceptor extends FilterSecurityInterceptor {

    @Override
    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }

}
