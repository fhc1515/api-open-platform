package com.intetface.fhcinterface.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import com.api.apiclientsdk.model.User;
import com.api.apiclientsdk.util.SignUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fhc
 * @version 1.0
 * @description: 名称 API
 * @date 2023/3/8 16:40
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/")
    public String getName(String name){
        return "GET 你的名字"+ name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "Post 你的名字" +name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request){     //json
        String accessKey = request.getHeader("accessKey");
        //不能传密钥到后端
//        String secretKey = request.getHeader("secretKey");
        String nonce=request.getHeader("nonce");
        String timestamp=request.getHeader("timestamp");
        String sign=request.getHeader("sign");
        String body=request.getHeader("body");
        //true为null
        boolean hasBlank = StrUtil.hasBlank(accessKey, body, sign, nonce, timestamp);
        // 判断是否有空
        if (hasBlank) {
            return "无权限";
        }
        if (!accessKey.equals("fhc")){
            throw new RuntimeException("无权限");
        }
        // TODO 使用accessKey去数据库查询secretKey
        // 假设查到的secretKey是fhc 进行加密得到sign
        String secretKey = "abcdefgh";
        String sign1 = SignUtil.getSign(body, secretKey);
        if (!StrUtil.equals(sign, sign1)) {
            return "无权限";
        }
        // 时间戳是否为数字
        if (!NumberUtil.isNumber(timestamp)) {
            return "无权限";
        }
         //五分钟内的请求有效
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String nowDate = sdf.format(date);
        Date afterDate = new Date(date .getTime() + 300000);
        String after = sdf.format(afterDate);
        if (nowDate.equals(after)){
            return "无权限";
        }
        String result = "POST 用户名字是" + user.getUserName();
        return result;

    }
}

