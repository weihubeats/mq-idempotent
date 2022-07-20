package com.samples.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.samples.entity.TestDO;
import org.apache.ibatis.annotations.Mapper;

/**
 *@author : wh
 *@date : 2022/7/20 14:27
 *@description:
 */
@Mapper
public interface TestMapper extends BaseMapper<TestDO> {
}
