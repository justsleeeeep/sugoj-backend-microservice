package com.sug.sugbackendmodel.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交状态枚举
 */
public enum QuestionSubmitStatusEnum {

    WAITING("待判题", 0),

    RUNNING("判题中", 1),

    SUCCEED("成功", 2),

    FAILED("失败", 3);

    /**
     * 文本
     */
    private final String text;

    /**
     * 值
     */
    private final Integer value;

    QuestionSubmitStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * 获取值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values())
                .map(item -> item.value)
                .collect(Collectors.toList());
    }
}