package xmg.xmg.accountmanage.service.impl;

import xmg.xmg.accountmanage.model.AccountModel;
import xmg.xmg.accountmanage.service.AccountManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountManageServiceLoadBalancerImpl implements AccountManageService {
    @Autowired
    private LoadBalancerClient loadBalancerClient; //Ribbon 负载均衡器
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<AccountModel> getAccountList() {
        StringBuilder sb = new StringBuilder();
        final ServiceInstance serviceInstance = loadBalancerClient.choose("service-provider");
        sb.append("http://")
                .append(serviceInstance.getHost())
                .append(":")
                .append(serviceInstance.getPort())
                .append("/user/list");
        System.out.println(sb.toString());
        final ResponseEntity<List<AccountModel>> exchange = restTemplate.exchange(sb.toString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AccountModel>>() {
                });

        return exchange.getBody();
    }
}
