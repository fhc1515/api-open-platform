package com.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 86189
 * @version 1.0
 * @description: TODO
 * @date 2023/3/23 16:07
 */
public interface InnerUserInterfaceInfoService{

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return boolean
     * @author fhc
     * @date 2023/3/23 16:11
     */
    boolean invokeCount(long interfaceInfoId,long userId);
}

