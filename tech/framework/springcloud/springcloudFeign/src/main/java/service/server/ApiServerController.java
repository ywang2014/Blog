package com.example.casestudy.feign.springcloud.service.server;

import com.example.casestudy.feign.springcloud.model.User;
import com.example.casestudy.feign.springcloud.service.api.ApiInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Spring Cloud Feign 的继承特性
 *     将接口从 Controller 中玻璃
 *     配合 Maven 私有仓库可以实现接口定义的共享，减少服务消费方的绑定配置。
 *
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午8:47
 */
@RestController
public class ApiServerController implements ApiInterface {
    private static final Logger log = LoggerFactory.getLogger(ApiServerController.class);

    @Override
    public User getUserInfo(@RequestParam String name) {
        log.info("Get user info start. name: {}", name);
        User user = new User();
        user.setName(name);
        return user;
    }

    @Override
    public User updateUser(@RequestBody User user) {
        log.info("Update user info start. name: {}", user.getName());
        return new User();
    }

    @Override
    public String deleteUser(@RequestParam String name) {
        log.info("Delete user start. name: {}", name);
        return "ok";
    }
}
