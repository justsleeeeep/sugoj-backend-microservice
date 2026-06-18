package com.sug.sugbackendmodel.model.dto.question;
import lombok.Data;

@Data
public class JudgeConfig {
    /*
    * 时间限制ms
    * */
    private Long timelimit;
    /*
     * 空间限制kb
     * */
    private Long memorylimit;
    /*
     * 堆栈限制kb
     * */
    private Long stacklimit;
}
