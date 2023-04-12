package com.api.controller;


import cn.hutool.core.bean.BeanUtil;
import com.api.annotation.AuthCheck;
import com.api.common.BaseResponse;
import com.api.common.ErrorCode;
import com.api.common.ResultUtils;
import com.api.exception.BusinessException;
import com.api.mapper.UserInterfaceInfoMapper;
import com.api.model.entity.InterfaceInfo;
import com.api.model.entity.UserInterfaceInfo;
import com.api.model.vo.InterfaceInfoVo;
import com.api.service.InterfaceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fhc
 * @version 1.0
 * @description:
 * @date 2023/3/24 10:13
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVo>> listTopInvokeInterfaceInfo(){
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listInvokeInterfaceInfo(4);
        //将UserInterfaceInfo对象按照interfaceInfoId进行分组
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream().
                                                                   collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper=new QueryWrapper<>();
        //将id属性的值设置为map集合的key（key是interfaceinfoid）   keySet() 方法返回映射中所有 key 组成的 Set 视图
        //只查top3的接口信息
        queryWrapper.in("id",interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        //top3为空，报系统错误
        if (CollectionUtils.isEmpty(list)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVo> interfaceInfoVoList = list.stream().map(interfaceInfo -> {
            //将interfaceinfo对象转换成interfaceInfovo对象
            InterfaceInfoVo interfaceInfoVo = new InterfaceInfoVo();
            //将interfaceinfo对象的属性转到interfaceInfovo对象
            BeanUtil.copyProperties(interfaceInfo, interfaceInfoVo);
            //接着使用Map的get方法获取interfaceInfoIdObjMap中对应InterfaceInfo对象id的UserInterfaceInfo对象集合中的第一个对象，
            //最后将UserInterfaceInfo对象的totalNum属性设置为InterfaceInfoVo对象的totalNum属性
            List<UserInterfaceInfo> userInterfaceInfoList1 = interfaceInfoIdObjMap.get(interfaceInfo.getId());
            Integer totalNum = userInterfaceInfoList1.get(0).getTotalNum();
            interfaceInfoVo.setTotalNum(totalNum);
            return interfaceInfoVo;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVoList);
    }
}
