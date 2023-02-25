package xmg.example.springdatastart.account.vo;

import java.util.Set;

/**
 * @author makui
 * @created on  2022/11/6
 **/
public interface UserVo {
    Long getId();

    String getUsername();

    String getPhone();

    Set<MenuVo> menus();

    Set<ResourceVo> resources();

    interface MenuVo {
        Long getId();

        String getName();
    }
    interface ResourceVo{
        Long getId();

        String getName();
    }

}
