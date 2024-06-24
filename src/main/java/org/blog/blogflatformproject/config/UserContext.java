package org.blog.blogflatformproject.config;

import lombok.Getter;
import lombok.Setter;


public class UserContext {

    private static final ThreadLocal<String> userHolder = new ThreadLocal<>();

    public static void setUserId(String userId){
        userHolder.set(userId);
    }

    public static String getUserId(){
        return userHolder.get();
    }

    public static void clear(){
        userHolder.remove();
    }

}
