package com.me.utils;

import com.me.entity.User;

public class UserHolder {
    private static final ThreadLocal<User> tl = new ThreadLocal<>();
    public static void saveUser(User user) {
        tl.set(user);
    }

    public static User getUser() {
        return tl.get();
    }
    public static void removeUser() {
        tl.remove();
    }
}
