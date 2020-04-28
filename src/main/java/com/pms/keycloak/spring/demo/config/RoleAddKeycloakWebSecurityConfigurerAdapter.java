package com.pms.keycloak.spring.demo.config;
 
    import org.keycloak.adapters.AdapterDeploymentContext;
    import org.keycloak.adapters.KeycloakConfigResolver;
    import org.keycloak.adapters.springsecurity.AdapterDeploymentContextFactoryBean;
    import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
    import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
    import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
    import org.keycloak.adapters.springsecurity.config.KeycloakSpringConfigResolverWrapper;
    import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
    import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
    import org.keycloak.adapters.springsecurity.filter.KeycloakCsrfRequestMatcher;
    import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
    import org.keycloak.adapters.springsecurity.filter.KeycloakSecurityContextRequestFilter;
    import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.core.io.Resource;
    import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.builders.WebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.security.web.authentication.logout.LogoutFilter;
    import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
    import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
 
    public abstract class RoleAddKeycloakWebSecurityConfigurerAdapter  extends WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity>{
        @Value("${keycloak.configurationFile:WEB-INF/keycloak.json}")
        private Resource keycloakConfigFileResource;
        @Autowired(required = false)
        private KeycloakConfigResolver keycloakConfigResolver;
 
        @Bean
        protected AdapterDeploymentContext adapterDeploymentContext() throws Exception {
            AdapterDeploymentContextFactoryBean factoryBean;
            if (keycloakConfigResolver != null) {
                 factoryBean = new AdapterDeploymentContextFactoryBean(new KeycloakSpringConfigResolverWrapper(keycloakConfigResolver));
            }
            else {
                factoryBean = new AdapterDeploymentContextFactoryBean(keycloakConfigFileResource);
            }
            factoryBean.afterPropertiesSet();
            return factoryBean.getObject();
        }
 
        protected AuthenticationEntryPoint authenticationEntryPoint() throws Exception {
            return new KeycloakAuthenticationEntryPoint(adapterDeploymentContext());
        }
 
        protected RoleAddKeycloakAuthenticationProvider roleAddKeycloakAuthenticationProvider() {
            return new RoleAddKeycloakAuthenticationProvider();
        }
 
        @Bean
        protected KeycloakAuthenticationProcessingFilter keycloakAuthenticationProcessingFilter() throws Exception {
            KeycloakAuthenticationProcessingFilter filter = new KeycloakAuthenticationProcessingFilter(authenticationManagerBean());
            filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
            return filter;
        }
 
        @Bean
        protected KeycloakPreAuthActionsFilter keycloakPreAuthActionsFilter() {
            return new KeycloakPreAuthActionsFilter(httpSessionManager());
        }
 
        protected KeycloakCsrfRequestMatcher keycloakCsrfRequestMatcher() {
            return new KeycloakCsrfRequestMatcher();
        }
 
        @Bean
        protected HttpSessionManager httpSessionManager() {
            return new HttpSessionManager();
        }
 
        protected KeycloakLogoutHandler keycloakLogoutHandler() throws Exception {
            return new KeycloakLogoutHandler(adapterDeploymentContext());
        }
 
        protected abstract SessionAuthenticationStrategy sessionAuthenticationStrategy();
 
        @Override
        protected void configure(HttpSecurity http) throws Exception {
 
            http
                .csrf().requireCsrfProtectionMatcher(keycloakCsrfRequestMatcher())
                .and()
                .sessionManagement()
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), LogoutFilter.class)
                .addFilterAfter(keycloakSecurityContextRequestFilter(), SecurityContextHolderAwareRequestFilter.class)
                .addFilterAfter(keycloakAuthenticatedActionsRequestFilter(), KeycloakSecurityContextRequestFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler())
                .logoutUrl("/sso/logout").permitAll()
                .logoutSuccessUrl("/");
        }
 
        @Bean
        protected KeycloakSecurityContextRequestFilter keycloakSecurityContextRequestFilter() {
            return new KeycloakSecurityContextRequestFilter();
        }
 
        @Bean
        protected KeycloakAuthenticatedActionsFilter keycloakAuthenticatedActionsRequestFilter() {
            return new KeycloakAuthenticatedActionsFilter();
        }
    }  