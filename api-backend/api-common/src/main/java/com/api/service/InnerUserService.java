package com.api.service;

import com.api.model.entity.User;

/**
 * @author 86189
 * @version 1.0
 * @description:
 * @date 2023/3/23 16:17
 */
public interface InnerUserService {
    /**
     * 数据库中查是否已分配给用户密钥（accessKey）
     * @param accessKey
     * @return model.entity.User
     * @author fhc
     * @date 2023/3/23 16:17
     */
    User getInvokeUser(String accessKey);
}
