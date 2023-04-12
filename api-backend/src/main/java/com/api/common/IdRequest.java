package com.api.common;

import com.api.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author yupi
 */
@Data
public class IdRequest implements Serializable {

    private Long id;

    private static final long serialVersionUID = 1L;
}
