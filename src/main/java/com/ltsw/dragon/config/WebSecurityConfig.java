package com.ltsw.dragon.config;

import com.ltsw.dragon.base.security.DefaultFilterSecurityInterceptor;
import com.ltsw.dragon.base.security.SecurityMetadataSource;
import com.ltsw.dragon.base.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heshaobing
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private SecurityMetadataSource metadataSource;
    private AccessDecisionManager accessDecisionManager;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 配置iframe
        http.headers().frameOptions().sameOrigin();
        // @formatter:off
        http.csrf().disable();
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(metadataSource.set(object.getSecurityMetadataSource())
                                .set(accessDecisionManager())
                        );
                        object.setAccessDecisionManager(accessDecisionManager());
                        return object;
                    }
                })
                .anyRequest().authenticated();

        http.formLogin().loginPage("/login").permitAll();
        http.logout().permitAll();

        http.rememberMe()
                .key("unique-and-secret")
                .rememberMeCookieName("remember-me-cookie-name")
                .tokenValiditySeconds(24 * 60 * 60);

        DefaultFilterSecurityInterceptor defaultFilterSecurityInterceptor = createFilterSecurityInterceptor(metadataSource, accessDecisionManager(), authenticationManager());
        http.addFilterAt(defaultFilterSecurityInterceptor, FilterSecurityInterceptor.class);

        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }


    /**
     * 访问决策管理器
     *
     * @return
     */
    public AccessDecisionManager accessDecisionManager() {

        if (accessDecisionManager == null) {
            List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
            RoleVoter roleVoter = new RoleVoter();
            roleVoter.setRolePrefix("");
            decisionVoters.add(roleVoter);

            WebExpressionVoter expressionVoter = new WebExpressionVoter();
            expressionVoter.setExpressionHandler(new DefaultWebSecurityExpressionHandler());
            decisionVoters.add(expressionVoter);
            accessDecisionManager = new AffirmativeBased(decisionVoters);
        }

        return accessDecisionManager;
    }

    /**
     * 过滤器安全拦截器
     *
     * @param metadataSource        源数据来源
     * @param accessDecisionManager 访问决策管理器
     * @param authenticationManager 身份验证管理器
     * @return
     * @throws Exception
     */
    private DefaultFilterSecurityInterceptor createFilterSecurityInterceptor(FilterInvocationSecurityMetadataSource metadataSource, AccessDecisionManager accessDecisionManager,
                                                                             AuthenticationManager authenticationManager) throws Exception {
        DefaultFilterSecurityInterceptor securityInterceptor = new DefaultFilterSecurityInterceptor();
        securityInterceptor.setSecurityMetadataSource(metadataSource);
        securityInterceptor.setAccessDecisionManager(accessDecisionManager);
        securityInterceptor.setAuthenticationManager(authenticationManager);
        securityInterceptor.afterPropertiesSet();
        return securityInterceptor;
    }


}

