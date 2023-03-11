package com.xmg.controller;

import com.xmg.provider.AccountProvider;
import com.xmg.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author makui
 * @created 2023/3/10
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String logIn(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return JwtUtil.createToken(authentication, false);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public AccountProvider.Account info() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            String username = springSecurityUser.getUsername();
            return AccountProvider.findByUserName(username);
        }
        return null;
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<AccountProvider.Account> list(){
        return AccountProvider.ACCOUNTS;
    }

}
