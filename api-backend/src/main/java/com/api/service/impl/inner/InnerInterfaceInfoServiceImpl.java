package com.api.service.impl.inner;

import com.api.common.ErrorCode;
import com.api.exception.BusinessException;
import com.api.mapper.InterfaceInfoMapper;
import com.api.model.entity.InterfaceInfo;
import com.api.service.InnerInterfaceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author fhc
 * @version 1.0
 * @description:
 * @date 2023/3/23 16:48
 */

@DubboService
public class InnerInterfaceInfoServiceImpl  implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }

}
