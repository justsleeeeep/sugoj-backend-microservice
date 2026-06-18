package com.sug.sugojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sug.sugbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.sug.sugbackendmodel.model.dto.user.UserQueryRequest;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugbackendmodel.model.entity.User;
import com.sug.sugbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.exception.BusinessException;
import com.sug.sugojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.sug.sugojbackendquestionservice.service.QuestionSubmitService;
import com.sug.sugojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 钱晨
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2026-05-15 15:40:01
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String language = questionSubmitQueryRequest.getLanguage();
        String userAccount = questionSubmitQueryRequest.getUserAccount();
        queryWrapper.eq(questionId != null, "questionId", questionId);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        if (StringUtils.isNotBlank(userAccount))
        {
//            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//            userQueryWrapper.eq("userAccount", userAccount);
//            User user = userFeignClient.getOne(userQueryWrapper);
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserAccount(userAccount);
            User user = userFeignClient.getOne(userQueryRequest);
            if (user == null) {
                queryWrapper.eq("userId", -1);
            } else {
                queryWrapper.eq("userAccount", userAccount);
                queryWrapper.eq("userId", user.getId());
            }
        }
        queryWrapper.orderByDesc("createTime");
        return queryWrapper;
    }

    @Override
    public void validQuestionSubmit(QuestionSubmit questionSubmit, boolean isadd) {
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        Long questionid = questionSubmit.getQuestionId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言不存在");
        }
        if (isadd) {
            if (questionid == null || questionid <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目 id 不合法");
            }
            if (StringUtils.isAnyBlank(language, code)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目输入不完整（语言、代码均为必填项）");
            }
        }
        if (StringUtils.isNotBlank(code) && code.length() > 8096) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签总长度不能超过1024个字符");
        }


    }


}




