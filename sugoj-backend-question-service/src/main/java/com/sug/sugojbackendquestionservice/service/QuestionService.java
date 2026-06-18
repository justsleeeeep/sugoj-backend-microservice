package com.sug.sugojbackendquestionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sug.sugbackendmodel.model.entity.Question;

/**
* @author 钱晨
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2026-05-15 15:37:50
*/
public interface QuestionService extends IService<Question> {

    /**
     * 问题校验
     *
     * @param question
     * @param isadd
     * @return
     */
    void validQuestion(Question question, boolean isadd);
    /**
     * 增加题目提交数
     *
     * @param questionId;
     * @return
     */
    void incrementSubmitNum(Long questionId);

}
