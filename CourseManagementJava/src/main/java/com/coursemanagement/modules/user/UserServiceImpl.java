package com.coursemanagement.modules.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coursemanagement.modules.user.domain.User;
import com.coursemanagement.modules.user.mapper.UserMapper;
import com.coursemanagement.modules.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
