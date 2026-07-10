package com.sug.sugojbackendquestionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sug.sugbackendmodel.model.entity.Question;

/**
* @author 钱晨
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2026-05-15 15:37:50
*/
public interface QuestionService extends IService<Question> {

    /** 带缓存的单题查询 */
    Question getQuestionById(Long id);

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

    /** 清除题目缓存（写操作后调用） */
    void evictCache(Long id);

    /** 访问题目时热度+1 */
    void incrementHotScore(Long questionId);

    /** 获取热门题目 ID Top N */
    java.util.List<Long> getHotQuestionIds(int topN);

    /** 新增题目时把 ID 加入布隆过滤器 */
    void addToBloomFilter(Long id);

}
