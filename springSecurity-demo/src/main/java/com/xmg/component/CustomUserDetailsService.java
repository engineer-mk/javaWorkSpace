package com.xmg.component;

import com.xmg.provider.AccountProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author makui
 * @created 2023/3/10
 **/
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final AccountProvider.Account account = AccountProvider.findByUserName(username);
        if (account == null) {
            throw new UsernameNotFoundException("未找到账户:" + username);
        }
        return User.builder().username(account.getUsername())
                .password(passwordEncoder.encode(account.getPassword()))
                .authorities(account.getAuthorities())
                .build();
    }
}
