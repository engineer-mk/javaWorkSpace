package com.xmg.config;

import com.alibaba.csp.sentinel.datasource.FileRefreshableDataSource;
import com.alibaba.csp.sentinel.datasource.FileWritableDataSource;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Sentinel 流控规则持久化
 */
//@Component
public class FileDataSourceInit implements InitFunc {
    /**
     * 文件数据源保存限流降级规则
     * 参考https://github.com/alibaba/Sentinel/wiki/%E5%9C%A8%E7%94%9F%E4%BA%A7%E7%8E%AF%E5%A2%83%E4%B8%AD%E4%BD%BF%E7%94%A8-Sentinel
     * @throws Exception
     */
    @PostConstruct
    @Override
    public void init() throws Exception {
        //流控规则
        String flowRuleFiePath = "/Users/mk/Work/WorkSpaceOne/springcloud-alibaba-demo/json/FlowRule.json";
        ReadableDataSource<String, List<FlowRule>> ds = new FileRefreshableDataSource<>(
                flowRuleFiePath, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {})
        );
        // 将可读数据源注册至 FlowRuleManager.
        FlowRuleManager.register2Property(ds.getProperty());
        WritableDataSource<List<FlowRule>> wds = new FileWritableDataSource<>(flowRuleFiePath, this::encodeJson);
        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerFlowDataSource(wds);
        //熔断降级
        String degradeRuleFilePath = "/Users/mk/Work/WorkSpaceOne/springcloud-alibaba-demo/json/DegradeRule.json";
        ReadableDataSource<String, List<DegradeRule>> degradeDs = new FileRefreshableDataSource<>(
                degradeRuleFilePath, source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {})
        );
        DegradeRuleManager.register2Property(degradeDs.getProperty());
        WritableDataSource<List<DegradeRule>> delegateWds = new FileWritableDataSource<>(degradeRuleFilePath, this::encodeJson);
        WritableDataSourceRegistry.registerDegradeDataSource(delegateWds);
        //系统规则
        String systemRuleFilePath = "/Users/mk/Work/WorkSpaceOne/springcloud-alibaba-demo/json/SystemRule.json";
        ReadableDataSource<String, List<SystemRule>> systemRuleDs = new FileRefreshableDataSource<>(
                systemRuleFilePath, source -> JSON.parseObject(source, new TypeReference<List<SystemRule>>() {})
        );
        SystemRuleManager.register2Property(systemRuleDs.getProperty());
        WritableDataSource<List<SystemRule>> systemRuleWds = new FileWritableDataSource<>(systemRuleFilePath, this::encodeJson);
        WritableDataSourceRegistry.registerSystemDataSource(systemRuleWds);

    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }

}