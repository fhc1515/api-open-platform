package com.api.apiclientsdk.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author fhc
 * @version 1.0
 * @description: TODO
 * @date 2023/3/8 20:57
 */
public class SignUtil {
    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return java.lang.String
     * @author fhc
     * @date 2023/3/8 20:41
     */
    public static String getSign(String body, String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body+"."+secretKey;
        return md5.digestHex(content);
    }
}
