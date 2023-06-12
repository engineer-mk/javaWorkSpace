package com.xmg.controller;

import com.xmg.model.User;
import com.xmg.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * logger --name com.xmg.controller.TestController --level debug
 * @author makui
 * @created on  2023/1/12
 **/
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {
    private final TestService testService;
    /**
     * 获取A的值
     * ognl -x 3 '@com.xmg.controller.TestController@B'
     */
    private static final String A = "A";
    /**
     * 修改B的值
     * ognl -x 3 '#field=@com.xmg.controller.TestController@class.getDeclaredField("B"),#field.setAccessible(true),#field.set(null,"b")'
     *
     */
    private static String B = "B";

    /**
     * ognl -x 3 '#field=@com.xmg.controller.TestController@class.getDeclaredField("run"),#field.setAccessible(true),#field.set(null,true)'
     */
    private static Boolean run = false;

    /**
     * 无法通过ognl表达式操作
     * 该变量非类所有
     */
    private String C = "C";

    public TestController(TestService testService) {
        this.testService = testService;
    }

    /**
     * watch com.xmg.controller.TestController doSomeThing '{params,returnObj,throwExp}'  -n 5  -x 3
     * - x 输出结果的遍历深度
     * - n 观察次数
     */
//    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    @RequestMapping("/doSomeThing")
    public void doSomeThing() {
        if (run == null || !run) {
            return;
        }
        String str = " A: " + A + " B: " + B;
        log.error("---error" + str);
        log.warn("---warn" + str);
        log.info("---info" + str);
        log.debug("---debug" + str);
        log.trace("---trace" + str);
    }
    @RequestMapping("/exception")
    public void doSo() {
        System.out.println(10 / 0);
    }

    /**
     * vmtool 获取内存中的实例执行方法
     * vmtool -x 3 --action getInstances --className com.xmg.controller.TestController  --express 'instances[0].test("zhangsan")'
     */
    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String test(@PathVariable String name) {
        return "hello " + name + "!";
    }


    /**
     * vmtool 获取内存中的实例执行方法
     * vmtool -x 3 --action getInstances --className com.xmg.controller.TestController  --express 'instances[0].addUser(new com.xmg.model.User("zhangsan","111"))'
     */
    public int addUser(User user) {
        User.users.add(user);
        return User.users.size();
    }


    /**
     * 追踪方法执行耗时
     * trace com.xmg.controller.TestController getUsers  -n 5 --skipJDKMethod false
     * @return
     */
    public List<User> getUsers() {
        return testService.test();
    }
}
