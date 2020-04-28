package com.pms.keycloak.spring.demo.config;
 
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
 
    import org.keycloak.KeycloakPrincipal;
    /*
     * Copyright 2016 Red Hat, Inc. and/or its affiliates
     * and other contributors as indicated by the @author tags.
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */
    import org.keycloak.adapters.springsecurity.account.KeycloakRole;
    import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
    import org.springframework.security.authentication.AuthenticationProvider;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
 
    /**
     * Performs authentication on a {@link KeycloakAuthenticationToken}.
     *
     * @author <a href="mailto:srossillo@smartling.com">Scott Rossillo</a>
     * @version $Revision: 1 $
     */
 
    public class RoleAddKeycloakAuthenticationProvider implements AuthenticationProvider {
        private GrantedAuthoritiesMapper grantedAuthoritiesMapper;
 
        public void setGrantedAuthoritiesMapper(GrantedAuthoritiesMapper grantedAuthoritiesMapper) {
            this.grantedAuthoritiesMapper = grantedAuthoritiesMapper;
        }
 
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
 
            ArrayList<String> list = (ArrayList)getSearchRoles();
             
            for(int i=0;i<list.size();i++) {
                grantedAuthorities.add(new KeycloakRole(list.get(i)));
            }
      
           token.getAccount().getKeycloakSecurityContext().getToken().getPreferredUsername();       
            return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), mapAuthorities(grantedAuthorities));
        }
 
        private Collection<? extends GrantedAuthority> mapAuthorities(
                Collection<? extends GrantedAuthority> authorities) {
            return grantedAuthoritiesMapper != null
                ? grantedAuthoritiesMapper.mapAuthorities(authorities)
                : authorities;
        }
      
        @Override
        public boolean supports(Class<?> aClass) {
            return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
        }
         
        protected RoleAddKeycloakAuthenticationProvider roleAddKeycloakAuthenticationProvider() {
            return new RoleAddKeycloakAuthenticationProvider();
        }
         
        public List getSearchRoles() {
            List<String> list = new ArrayList<String>();
             
            //DB 조회값                       
            list.add("ADMIN");
            list.add("USER");      
             
            return list;
        }
    }