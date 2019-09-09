package com.example.casestudy.feign.springcloud.service.api;

import com.example.casestudy.feign.springcloud.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午9:01
 */
@FeignClient("ApiService")
public interface ApiInterface {

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

    /**
     * 用户信息编辑
     *
     * @param name
     * @return
     */
    @DeleteMapping("user/delete")
    String deleteUser(@RequestParam("name") String name);
}
