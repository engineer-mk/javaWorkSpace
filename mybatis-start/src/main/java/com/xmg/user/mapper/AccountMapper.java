package com.xmg.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmg.user.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author makui
 * @created on  2022/9/17
 **/
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    List<Account> findById();

    /**
     * 关联查询
     * @return
     */
    List<Account> findByIdUseAssociation();

    /**
     * 分布查询
     * @return
     */
    List<Account> findByIdUseSpread();

    @Select("select  username from account where  id= #{id}")
    String findUsernameById(Long id);

    @Insert("insert  into account(username, phone, create_time, update_time) values (#{username},#{phone},#{createTime},#{updateTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void insertAccount(Account account);


}
