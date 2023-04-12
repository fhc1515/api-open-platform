package com.api.service.impl.inner;

import com.api.common.ErrorCode;
import com.api.exception.BusinessException;
import com.api.mapper.UserMapper;
import com.api.model.entity.User;
import com.api.service.InnerUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author fhc
 * @version 1.0
 * @description:
 * @date 2023/3/23 16:49
 */

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        //为空表示不存在
        if (StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //根据accessKey拿到用户信息，返回用户信息
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
