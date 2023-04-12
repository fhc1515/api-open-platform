package com.api.mapper;

import com.api.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86189
* @description 针对表【user_interface_info】的数据库操作Mapper
* @createDate 2023-03-15 16:07:14
* @Entity com.api.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listInvokeInterfaceInfo(int limit);

}




