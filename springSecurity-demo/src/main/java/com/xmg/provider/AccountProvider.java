package com.xmg.provider;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author makui
 * @created 2023/3/10
 **/
@Data
@NoArgsConstructor
public class AccountProvider {

    public static final List<Account> ACCOUNTS = new ArrayList<>();

    static {
        ACCOUNTS.add(new AccountProvider.Account("zhang", "123123", "account_list"));
        ACCOUNTS.add(new AccountProvider.Account("ls", "123123", "account_list"));
        ACCOUNTS.add(new AccountProvider.Account("wang", "123123"));
    }

    public static Account findByUserName(String userName) {
        return AccountProvider.ACCOUNTS.stream().filter(it -> it.getUsername().equals(userName)).findFirst().orElse(null);
    }

    @Data
    public static class Account {
        private final String username;
        private final String password;
        private final String[] authorities;

        public Account(String username, String password, String... authorities) {
            this.username = username;
            this.password = password;
            this.authorities = authorities;
        }

        public Account(String username, String password) {
            this.username = username;
            this.password = password;
            this.authorities = null;
        }
    }


}
