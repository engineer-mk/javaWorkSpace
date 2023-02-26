package xmg.xmg.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import xmg.xmg.service.UserService;
import xmg.xmg.user.entity.Account;
import xmg.xmg.user.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author makui
 * @created 2022/10/30
 **/
@RestController
@RequiredArgsConstructor
public class UserController {
    private final AccountMapper accountMapper;
    private final UserService userService;


    @RequestMapping("/info/{id}")
    public List<Account> accountInfo(@PathVariable Long id) {
        return accountMapper.findByIdUseAssociation();
    }

    @RequestMapping("/account/page")
    public PageInfo<Account> accountInfo(int page, int size) {
        return PageHelper.startPage(page, size)
                .doSelectPageInfo(accountMapper::findByIdUseSpread);
    }
    @RequestMapping("/cancel")
    public String cancel(){
        userService.cancel();
        return "success";
    }
    @RequestMapping("/updateTask")
    public String updateTask(int minute, int hour,int second) {
        userService.updateTask(minute, hour,second);
        return "success";
    }
}
