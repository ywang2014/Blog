package com.example.casestudy.feign.springcloud.service.business;

import com.example.casestudy.feign.springcloud.model.User;
import org.springframework.stereotype.Component;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午9:22
 */
@Component
public class ServiceFallback implements RefactorServerInterface {
    private User getErrorUser() {
        User error = new User();
        error.setName("Error");
        error.setAge(-1);
        return error;
    }

    @Override
    public User getUserInfo(String name) {
        return getErrorUser();
    }

    @Override
    public User updateUser(User user) {
        return getErrorUser();
    }

    @Override
    public String deleteUser(String name) {
        return "error";
    }
}
