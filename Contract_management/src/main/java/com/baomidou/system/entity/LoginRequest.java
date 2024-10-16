package com.baomidou.system.entity;

public class LoginRequest {
    private String account;
    private String password;

    // 默认构造函数
    public LoginRequest() {}

    // 带参数的构造函数
    public LoginRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }

    // Getter 和 Setter
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
