package com.codem;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LiuJun
 * @Title: eureka provider 测试demo
 * @Description:
 * @date 2020/8/27下午9:50
 */
@RestController
@RequestMapping("/provider")
public class TestController {

    @Resource
    private Environment environment;

    @GetMapping("/func")
    public String func(){
       return "application port :" + environment.getProperty("name");
    }

}
