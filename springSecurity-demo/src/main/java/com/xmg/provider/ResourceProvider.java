package com.xmg.provider;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author makui
 * @created 2023/3/11
 **/
public class ResourceProvider {

    public static final Set<Resource> RESOURCES = new LinkedHashSet<>();

    static {
        RESOURCES.add(new Resource("/error", "anon"));
        RESOURCES.add(new Resource("/api/account/login", "anon"));
        RESOURCES.add(new Resource("/api/account/info", "authc"));
        RESOURCES.add(new Resource("/api/account/list", "account_list"));
    }

    @Getter
    @Setter
    public static class Resource {
        private final String url;
        private final String permission;

        public Resource(String url, String permission) {
            this.url = url;
            this.permission = permission;
        }
    }
}
