package com.sug.sugojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sug.sugbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;


/**
* @author 钱晨
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2026-05-15 15:40:01
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交校验
     *
     * @param questionSubmit
     * @param isadd
     * @return
     */
    void validQuestionSubmit(QuestionSubmit questionSubmit, boolean isadd);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * CAS 原子更新提交状态
     * @return true=抢锁成功，false=已被抢占
     */
    boolean updateStatusCas(Long id, Integer expectedStatus, Integer newStatus);

}
