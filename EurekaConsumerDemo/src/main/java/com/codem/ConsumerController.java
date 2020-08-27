package com.codem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LiuJun
 * @Title: eureka consumer 测试 controller
 * @Description:
 * @date 2020/8/27下午10:04
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    private Environment environment;

    @Autowired
    private FeignService feignService;

    @GetMapping("/func")
    public String func(){
        System.out.println("consumer name:" + environment.getProperty("name"));
        return  feignService.func();
    }

}
