package com.baomidou.system.mapper;

import com.baomidou.system.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Sun
 * @since 2024-09-26
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
