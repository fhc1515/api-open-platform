package com.api.service;

import com.api.model.entity.InterfaceInfo;

/**
 * @author 86189
 * @version 1.0
 * @description: TODO
 * @date 2023/3/23 16:15
 */
public interface InnerInterfaceInfoService {
    /**
     *  从数据库中查询模拟接口是否存在
     * @param path
     * @param method
     * @return model.entity.InterfaceInfo
     * @author fhc
     * @date 2023/3/23 16:16
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
