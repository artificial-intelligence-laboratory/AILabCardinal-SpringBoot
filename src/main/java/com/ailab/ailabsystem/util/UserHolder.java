package com.ailab.ailabsystem.util;


import com.ailab.ailabsystem.model.vo.UserVo;

public class UserHolder {
    private static final ThreadLocal<UserVo> tl = new ThreadLocal<>();

    public static void saveUser(UserVo user){
        tl.set(user);
    }

    public static UserVo getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
