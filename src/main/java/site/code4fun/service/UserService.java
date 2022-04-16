package site.code4fun.service;

import site.code4fun.entity.User;

import java.util.List;

public interface UserService{
    List<User> getAll();
    User getOne(Long id);
}
