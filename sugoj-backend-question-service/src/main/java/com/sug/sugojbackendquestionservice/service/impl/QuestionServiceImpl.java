package com.sug.sugojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.exception.BusinessException;
import com.sug.sugojbackendquestionservice.mapper.QuestionMapper;
import com.sug.sugojbackendquestionservice.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 钱晨
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2026-05-15 15:37:50
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    @Override
    public void incrementSubmitNum(Long questionId) {
        LambdaUpdateWrapper<Question> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Question::getId, questionId)
                .setSql("submitNum = submitNum + 1");
        boolean success = this.update(wrapper);
        if (!success) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
    }
    @Override
    public void validQuestion(Question question, boolean isadd) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        if (isadd) {
            if (StringUtils.isAnyBlank(title, content, tags, answer, judgeCase, judgeConfig)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目输入不完整（标题、内容、标签、答案、判题用例、判题配置均为必填项）");
            }
        }

        if (StringUtils.isNotBlank(title) && title.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长，不能超过512个字符");
        }
        if (StringUtils.isNotBlank(tags) && tags.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标签总长度不能超过1024个字符");
        }

        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长，不能超过8192个字符");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长，不能超过8192个字符");
        }


    }
}




