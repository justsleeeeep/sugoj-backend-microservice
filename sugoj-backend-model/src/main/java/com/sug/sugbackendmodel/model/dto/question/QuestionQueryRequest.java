package com.sug.sugbackendmodel.model.dto.question;


import com.sug.sugojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author sug
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表(json数组)
     */
    private String tags;


    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}