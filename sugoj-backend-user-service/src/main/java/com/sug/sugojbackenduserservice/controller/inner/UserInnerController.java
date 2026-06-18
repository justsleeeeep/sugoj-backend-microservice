package com.sug.sugojbackenduserservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sug.sugbackendmodel.model.dto.user.UserQueryRequest;
import com.sug.sugbackendmodel.model.entity.User;
import com.sug.sugojbackendserviceclient.service.UserFeignClient;
import com.sug.sugojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.management.Query;

@RestController()
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {
    @Resource
    UserService userService;
    @Override
    @PostMapping("/getUser")
    public User getOne(@RequestBody UserQueryRequest userQueryRequest)
    {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("userAccount", userQueryRequest.getUserAccount());
        return userService.getOne(queryWrapper);
    }
}
