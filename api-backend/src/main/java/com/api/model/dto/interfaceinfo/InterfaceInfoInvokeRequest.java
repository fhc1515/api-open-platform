package com.api.model.dto.interfaceinfo;

import lombok.Data;

/**
 * @author fhc
 * @version 1.0
 * @description: 接口调用请求
 * @date 2023/3/13 15:26
 */
@Data
public class InterfaceInfoInvokeRequest {
    /**
     * 主键
     */
    private Long id;

    /**
     * 请求参数
     */
    private String requestParams;
}
