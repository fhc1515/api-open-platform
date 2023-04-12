package com.api.model.vo;

import com.api.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**  接口信息封装
 * @author fhc
 * @version 1.0
 * @description: TODO
 * @date 2023/3/24 10:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVo extends InterfaceInfo {
    /*
     *  接口调用次数
     */
    private Integer totalNum;


    private static final long serialVersionUID = 1L;
}
