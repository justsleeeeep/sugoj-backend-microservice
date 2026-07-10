package com.sug.sugojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.exception.BusinessException;
import com.sug.sugojbackendquestionservice.mapper.QuestionMapper;
import com.sug.sugojbackendquestionservice.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import com.google.common.hash.BloomFilter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
* @author 钱晨
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2026-05-15 15:37:50
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private BloomFilter<Long> bloomFilter;

    private static final String HOT_QUESTION_KEY = "hot:questions";

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

    // ======================== Redis 缓存 ========================

    /**
     * 带缓存的单题查询
     * 第一次查走 MySQL 并自动存入 Redis（key = "question::<id>"）
     * 之后 10 分钟内再查直接走 Redis，不碰 MySQL
     */
    @Cacheable(value = "question", key = "#id", unless = "#result == null")
    public Question getQuestionById(Long id) {
        // 布隆过滤器：绝对不存在 → 直接返回 null，不查 MySQL
        if (!bloomFilter.mightContain(id)) {
            return null;
        }
        return super.getById(id);
    }

    /**
     * 写操作后清除该题缓存
     */
    @CacheEvict(value = "question", key = "#id")
    public void evictCache(Long id) {
        // 空方法，注解自动清除 Redis 中的 "question::<id>"
    }

    /**
     * 新增题目时把 ID 加入布隆过滤器
     */
    public void addToBloomFilter(Long id) {
        bloomFilter.put(id);
    }

    // ======================== 热门题目排行（ZSet）========================

    /**
     * 题目被访问时调用，Redis ZSet 里分数 +1
     */
    public void incrementHotScore(Long questionId) {
        redisTemplate.opsForZSet().incrementScore(HOT_QUESTION_KEY, questionId.toString(), 1);
    }

    /**
     * 获取热门题目 Top N
     * ZSet 按分数降序取前 N 个
     */
    public java.util.List<Long> getHotQuestionIds(int topN) {
        Set<Object> set = redisTemplate.opsForZSet()
                .reverseRange(HOT_QUESTION_KEY, 0, topN - 1);
        if (set == null || set.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<Long> ids = new java.util.ArrayList<>();
        for (Object obj : set) {
            ids.add(Long.valueOf(obj.toString()));
        }
        return ids;
    }
}




