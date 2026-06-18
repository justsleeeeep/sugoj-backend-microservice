package com.sug.sugojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sug.sugbackendmodel.model.dto.user.UserQueryRequest;
import com.sug.sugbackendmodel.model.entity.User;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.exception.BusinessException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import javax.servlet.http.HttpServletRequest;

import static com.sug.sugojbackendcommon.constant.UserConstant.ADMIN_ROLE;
import static com.sug.sugojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author sug
 */
@FeignClient(name="sugoj-backend-user-service",path="/api/user/inner")
public interface UserFeignClient {
//        userService.getLoginUser()
//        userService.isAdmin()
//        userService.getOne()




    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request)
    {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
//        // 从数据库查询（追求性能的话可以注释，直接走缓存）
//        long userId = currentUser.getId();
//        currentUser = this.getById(userId);
//        if (currentUser == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
//        }
        return currentUser;
    };

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    default boolean isAdmin(HttpServletRequest request)
    {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && ADMIN_ROLE.equals(user.getUserRole());
    };



    @PostMapping("/getUser")
    User getOne(@RequestBody UserQueryRequest userQueryRequest);
}
