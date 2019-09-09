package com.example.casestudy.feign.springcloud.service.server;

import com.example.casestudy.feign.springcloud.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午8:38
 */
@FeignClient("ServerService")
public interface ServerInterface {

    /**
     * 用户信息查询
     *
     * @param name
     * @return
     */
    @GetMapping("user/info")
    User getUserInfo(@RequestParam("name") String name);

    /**
     * 用户信息编辑
     *
     * @param user
     * @return
     */
    @PostMapping("user/update")
    User updateUser(@RequestBody User user);
}
