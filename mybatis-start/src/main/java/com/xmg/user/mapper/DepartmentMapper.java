package com.xmg.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmg.user.entity.Department;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

/**
 * @author makui
 * @created on  2022/11/21
 **/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    
}
