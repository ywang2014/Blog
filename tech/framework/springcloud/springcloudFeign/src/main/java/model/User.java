package com.example.casestudy.feign.springcloud.model;

/**
 * @author wangyong.1991
 * @version 创建时间：2019/9/9 下午8:39
 */
public class User {
    private String name;
    private String address;
    private String email;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
