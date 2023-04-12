package com.api.service.impl.inner;

import com.api.service.InnerUserInterfaceInfoService;
import com.api.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author fhc
 * @version 1.0
 * @description:
 * @date 2023/3/23 16:48
 */

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
          return userInterfaceInfoService.invokeCount(interfaceInfoId,userId);
    }
}
