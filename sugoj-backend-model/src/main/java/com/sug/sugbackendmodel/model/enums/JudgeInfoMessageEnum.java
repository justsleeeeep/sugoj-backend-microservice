package com.sug.sugbackendmodel.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息枚举
 */
public enum JudgeInfoMessageEnum {
    ACCEPTED("Accepted", "成功"),

    WRONG_ANSWER("Wrong Answer", "答案错误"),

    COMPILE_ERROR("Compile Error", "编译错误"),

    MEMORY_LIMIT_EXCEEDED("Memory Limit Exceeded", "内存溢出"),

    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时"),

    PRESENTATION_ERROR("Presentation Error", "展示错误"),

    WAITING("Waiting", "等待中"),

    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出超限"),

    DANGEROUS_OPERATION("Dangerous Operation", "危险操作"),

    RUNTIME_ERROR("Runtime Error", "运行错误"),

    SYSTEM_ERROR("System Error", "系统错误");
    /**
     * 文本
     */
    private final String text;

    /**
     * 值
     */
    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    /**
     * 获取值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(item -> item.value)
                .collect(Collectors.toList());
    }
}