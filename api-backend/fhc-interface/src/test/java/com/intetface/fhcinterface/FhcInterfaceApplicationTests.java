package com.intetface.fhcinterface;

import com.api.apiclientsdk.client.ApiClient;
import com.api.apiclientsdk.model.User;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;


@SpringBootTest
class FhcInterfaceApplicationTests {

    @Resource
    private ApiClient apiClient;

    @Test
    void contextLoads() {
        String result = apiClient.getNameByGet("fhc1");
        User user=new User();
        user.setUserName("fhc1");
        String result2 = apiClient.getUsernameByPost(user);
        System.out.println(result);
        System.out.println(result2);
    }
}
