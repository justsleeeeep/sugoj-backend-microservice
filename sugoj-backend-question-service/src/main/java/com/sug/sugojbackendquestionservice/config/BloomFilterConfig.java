package com.sug.sugojbackendquestionservice.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugojbackendquestionservice.mapper.QuestionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 布隆过滤器配置
 * 启动时从 MySQL 加载全部题目 ID，用于防缓存穿透
 *
 * @author sug
 */
@Configuration
@Slf4j
public class BloomFilterConfig {

    @Resource
    private QuestionMapper questionMapper;

    /**
     * 预期题目数 10000，误判率 1%
     * 内存占用约 12 KB
     */
    @Bean
    public BloomFilter<Long> questionBloomFilter() {
        BloomFilter<Long> filter = BloomFilter.create(
                Funnels.longFunnel(),
                10000,
                0.01);

        List<Long> ids = questionMapper.selectList(null)
                .stream()
                .map(Question::getId)
                .collect(Collectors.toList());

        for (Long id : ids) {
            filter.put(id);
        }
        log.info("布隆过滤器初始化完成，已加载 {} 个题目 ID", ids.size());
        return filter;
    }
}
