package xmg.xmg.accountmanage.service.impl;

import xmg.xmg.accountmanage.model.AccountModel;
import xmg.xmg.accountmanage.service.AccountManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountMangeServiceLoadBalancerAnnotationImpl implements AccountManageService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<AccountModel> getAccountList() {
        String url = "http://service-provider/user/list";
        final ResponseEntity<List<AccountModel>> exchange = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountModel>>() {
                });
        return exchange.getBody();
    }

}
