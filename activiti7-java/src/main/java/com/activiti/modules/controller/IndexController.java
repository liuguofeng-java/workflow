package com.activiti.modules.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * @author liuguofeng
 * @date 2023/10/17 10:46
 **/
@RestController
@RequestMapping("/index")
public class IndexController {

    @GetMapping("demo01")
    public String demo01(){
        return "hello world";
    }
}
