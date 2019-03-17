package com.exercice.spring.demo.utils;

import com.exercice.spring.demo.dto.UserDto;

public class UserUtils {

    public static boolean isPasswordNotValid(UserDto userDto) {
        if (userDto.getPassword() == null) {
            return true;
        }

        return userDto.getPassword().equals(userDto.getRetypePassword()) ? false : true;
    }
}
