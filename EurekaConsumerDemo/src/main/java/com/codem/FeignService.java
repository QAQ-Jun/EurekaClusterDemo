package com.codem;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LiuJun
 * @Title: eureka consumer 测试 demo
 * @Description:
 * @date 2020/8/27下午10:03
 */
@FeignClient("eureka-provider-server")
public interface FeignService {

    @GetMapping("/provider/func")
    String func();


}
