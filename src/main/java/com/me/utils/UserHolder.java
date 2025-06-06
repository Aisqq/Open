package com.me.utils;

import com.me.entity.User;

public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    // 保存用户到当前线程
    public static void saveUser(User user) {
        tl.set(user);
    }

    // 获取当前线程的用户
    public static User getUser() {
        return tl.get();
    }

    // 移除用户信息
    public static void removeUser() {
        tl.remove();
    }
}
