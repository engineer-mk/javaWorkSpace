package com.xmg.component;

import com.xmg.provider.ResourceProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author makui
 * @created 2023/3/11
 **/
@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private static final AuthorizationDecision ADOPT = new AuthorizationDecision(true);

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext context) {
        final String path = context.getRequest().getServletPath();
        final Set<ResourceProvider.Resource> resources = ResourceProvider.RESOURCES;
        final Optional<ResourceProvider.Resource> optional = resources.stream()
                .filter(it -> pathMatcher.match(it.getUrl(), path))
                .findFirst();
        if (!optional.isPresent()) {
            return DENY;
        }
        final ResourceProvider.Resource resource = optional.get();
        final String permission = resource.getPermission();
        if (Objects.equals(permission, "anon")) {
            return ADOPT;
        }
        if (!isAuthenticated(supplier)) {
            return DENY;
        }
        if (Objects.equals(permission, "authc")) {
            return ADOPT;
        }
        final Authentication authentication = supplier.get();
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        final Collection<? extends GrantedAuthority> authorities = springSecurityUser.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return DENY;
        }
        if (authorities.stream().noneMatch(it -> it.getAuthority().equals(permission))) {
            return DENY;
        }
        return ADOPT;
    }

    private boolean isAuthenticated(Supplier<Authentication> supplier) {
        final Authentication authentication = supplier.get();
        return authentication != null && authentication.isAuthenticated();
    }
}
