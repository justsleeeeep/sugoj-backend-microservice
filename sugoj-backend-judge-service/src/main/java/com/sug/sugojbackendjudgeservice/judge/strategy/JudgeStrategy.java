package com.sug.sugojbackendjudgeservice.judge.strategy;


import com.sug.sugbackendmodel.model.codesandbox.JudgeInfo;

public interface JudgeStrategy {
    /**
     * 判题策略
     * @param judgeContext
     * @return
     */
    JudgeInfo dojudge(JudgeContext judgeContext);
}
