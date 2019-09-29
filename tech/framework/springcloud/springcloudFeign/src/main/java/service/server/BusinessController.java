package com.example.casestudy.feign.springcloud.service.server;

import com.example.casestudy.feign.springcloud.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午8:41
 */
@RestController
public class BusinessController {
    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private ServerInterface serverService;

    @GetMapping("user")
    public User getUserInfo(@RequestParam String name) {
        log.info("Get user info start. name: {}", name);
        return serverService.getUserInfo(name);
    }
}
