package com.example.casestudy.feign.springcloud.service.business;

import com.example.casestudy.feign.springcloud.service.api.ApiInterface;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 在 业务代码中 中引入 service-api 依赖，创建 RefactorHelloService 继承自 HelloService。
 *
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午8:53
 */
@FeignClient(name = "refactor-service", fallback = ServiceFallback.class)
public interface RefactorServerInterface extends ApiInterface {
}
