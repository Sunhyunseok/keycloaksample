/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pms.keycloak.spring.demo.web;

import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.pms.keycloak.spring.demo.service.ProductService;
import org.keycloak.common.util.KeycloakUriBuilder;
import org.keycloak.constants.ServiceUrlConstants;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ProductController {

    private final ProductService productService;

    private final HttpServletRequest request;

    public ProductController(ProductService productService, HttpServletRequest request) {
        this.productService = productService;
        this.request = request;
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String handleCustomersRequest(Principal principal, Model model) {
        model.addAttribute("products", productService.getProducts());
        model.addAttribute("principal",  principal);
        String logoutUri = KeycloakUriBuilder.fromUri("http://localhost:8080/auth").path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
                .queryParam("redirect_uri", "http://localhost:8081/products").build("quickstart").toString();
        model.addAttribute("logout",  logoutUri);
        return "products";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String handleLogoutt() throws ServletException {
        request.logout();
        return "landing";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String landing() throws ServletException {
        return "landing";
    }

}
