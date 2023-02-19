package com.xmg.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmg.user.entity.Department;
import com.xmg.user.entity.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author makui
 * @created on  2022/11/21
 **/
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> findByUserId(Long userId);
}
