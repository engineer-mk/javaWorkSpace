package com.xmg.accountmanage.service.impl;

import com.xmg.accountmanage.model.AccountModel;
import com.xmg.accountmanage.service.AccountManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountManageServiceDiscoveryImpl implements AccountManageService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public List<AccountModel> getAccountList() {
        //获取服务列表
        final List<String> services = discoveryClient.getServices();
        if (services.isEmpty()) {
            return null;
        }
        //根据服务名称获取服务
        final List<ServiceInstance> instances = discoveryClient.getInstances("service-provider");
        if (instances.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        final ServiceInstance serviceInstance = instances.get(0);
        sb.append("http://")
                .append(serviceInstance.getHost())
                .append(":")
                .append(serviceInstance.getPort())
                .append("/user/list");
        final ResponseEntity<List<AccountModel>> exchange = restTemplate.exchange(sb.toString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<AccountModel>>() {
                });

        return exchange.getBody();
    }
}
