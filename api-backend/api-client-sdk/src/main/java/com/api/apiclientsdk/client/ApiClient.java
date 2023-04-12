package com.api.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.api.apiclientsdk.model.User;
import com.api.apiclientsdk.util.SignUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fhc
 * @version 1.0
 * @description: 调用第三方接口客户端
 * @date 2023/3/8 17:02
 */
public class ApiClient {
    private String accessKey;
    private String secretKey;
    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String result = HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }
    public String getNameByPost(@RequestParam String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name",name);
        String result = HttpUtil.post("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    private Map<String,String> getHeaderMap(String body){
        Map<String,String> hashMap=new HashMap<>();
        hashMap.put("accessKey",accessKey);
        //不能直接发送给后端
//        hashMap.put("secretKey",secretKey);
        //随机数，防止重放
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        //用户请求参数
        hashMap.put("body",body);
        //时间戳
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        //
        hashMap.put("sign", SignUtil.getSign(body,secretKey));
        return hashMap;
    }

    public String getUsernameByPost(@RequestBody User user){     //json
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }
}
