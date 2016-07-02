package com.damosdesigns.sendkisses;

/**
 * Created by damosdesigns on 7/2/16.
 */
public class User {
    private static User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }
}
