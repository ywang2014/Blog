package com.example.casestudy.feign.springcloud.service.business;

import com.example.casestudy.feign.springcloud.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午8:54
 */
@RestController
public class RefactorBusinessController {
    private static final Logger log = LoggerFactory.getLogger(RefactorBusinessController.class);

    @Autowired
    private RefactorServerInterface refactorServerService;

    @GetMapping("user/get")
    public User getUserInfo(@RequestParam String name) {
        log.info("Get user info start. name: {}", name);
        return refactorServerService.getUserInfo(name);
    }

    @PostMapping("user/update")
    public User updateUser(@RequestBody String name) {
        log.info("Update user info start. name: {}", name);
        User user = new User();
        user.setName(name);
        return refactorServerService.updateUser(user);
    }

    @DeleteMapping("user/delete")
    public String DeleteUser(@RequestParam String name) {
        log.info("Delete user start. name: {}", name);
        return refactorServerService.deleteUser(name);
    }
}
