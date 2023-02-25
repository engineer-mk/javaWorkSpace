package xmg.example.springdatastart.account.controller;

import xmg.example.springdatastart.account.entity.User;
import xmg.example.springdatastart.account.repository.UserRepository;
import xmg.example.springdatastart.account.service.UserService;
import xmg.example.springdatastart.base.BaseQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author makui
 * @created on  2022/11/6
 **/
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @RequestMapping("/userById/{method}/{id}")
    public User user(@PathVariable Long id, @PathVariable String method) {
        switch (method) {
            case "getOne":
                return userService.getOne(id);
            case "find":
                return entityManager.find(User.class, id);
            case "findById":
                return userRepository.findById(id).orElse(null);
            default:
                return null;

        }
    }


    @RequestMapping("/userByName/{method}")
    public Object user(String username, String roleName, String sourceName, String pName, @PathVariable String method) {
        switch (method) {
            case "findByUsername":
                return userRepository.findByUsername(username);
            case "sqlByEm":
                return userService.sqlByEm(username, roleName, sourceName);
            case "sqlByHb":
                return userService.sqlByHb(username, roleName, sourceName);
            case "byEmCriteriaQuery":
                return userService.byEmCriteriaQuery(username, roleName, sourceName, null, null);
            case "bySpecification":
                return userService.bySpecification(username, roleName, sourceName, pName);
            case "byQueryDsl":
                return userService.byQueryDsl(username, roleName, sourceName);
            case "findAll":
                final PageRequest pageRequest = PageRequest.of(1, 20);
                return userRepository.findAll(pageRequest);
            default:
                return Collections.emptyList();
        }
    }

    @RequestMapping("/userPage/{method}")
    public Object userPage(@PathVariable String method, HttpServletRequest httpServletRequest) {
        switch (method) {
            case "findPage":
                return userRepository.findPage(BaseQuery.of(httpServletRequest));
            case "findList":
                return userRepository.findAll(BaseQuery.of(httpServletRequest));
            case "findAll":
                final PageRequest pageRequest = PageRequest.of(1, 20);
                return userRepository.findAll(pageRequest);
            default:
                return Collections.emptyList();
        }
    }

    @RequestMapping("/usernames")
    public List<String> usernames() {
        return userService.usernames();
    }


    @RequestMapping("/list")
    public List<User> userList(HttpServletRequest httpServletRequest) {
        final BaseQuery baseQuery =  BaseQuery.of(httpServletRequest);
        return userRepository.findAll(baseQuery);
    }
}
