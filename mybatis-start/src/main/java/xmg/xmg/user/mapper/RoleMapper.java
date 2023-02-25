package xmg.xmg.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xmg.xmg.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author makui
 * @created on  2022/11/21
 **/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findByUserId(Long userId);
}
