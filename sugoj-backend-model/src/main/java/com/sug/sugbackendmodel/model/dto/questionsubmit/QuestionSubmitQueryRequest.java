package com.sug.sugbackendmodel.model.dto.questionsubmit;


import com.sug.sugojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目提交查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户账号
     */
    private String userAccount;

    private static final long serialVersionUID = 1L;
}
