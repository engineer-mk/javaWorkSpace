package xmg.xmg.user.controller;

import xmg.xmg.user.entity.User;
import xmg.xmg.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    EntityManager entityManager;

    @RequestMapping(value = "/consumer/user/addOrder",method = RequestMethod.POST)
    public String userAddOrder(){
        userService.useraddOrder();
        return "success";
    }

    @RequestMapping(value = "/provider/orderCount/add",method = RequestMethod.POST)
    public String orderCountAdd(){
        User user = entityManager.find(User.class, 1);
        userService.orderCountAdd(user);
        return "success";
    }
}
