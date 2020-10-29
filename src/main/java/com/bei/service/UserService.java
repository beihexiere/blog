package com.bei.service;

import com.bei.po.User;

/**
 * @author Administrator
 * @date 2020/8/18 0018 - 19:27
 */
public interface UserService {

    User checkUser(String username,String password);
}
