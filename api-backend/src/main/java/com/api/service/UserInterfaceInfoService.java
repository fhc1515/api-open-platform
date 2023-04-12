package com.api.service;

import com.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86189
* @description 针对表【user_interface_info】的数据库操作Service
* @createDate 2023-03-15 16:07:14
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
            * @param interfaceInfoId
            * @param userId
            * @return int
            * @author fhc
            * @date 2023/3/15 17:01
     */
   boolean invokeCount(long interfaceInfoId,long userId);

}
