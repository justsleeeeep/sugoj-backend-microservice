package com.sug.sugojbackendjudgeservice.judge;


import com.sug.sugbackendmodel.model.codesandbox.JudgeInfo;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.sug.sugojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.sug.sugojbackendjudgeservice.judge.strategy.JudgeContext;
import com.sug.sugojbackendjudgeservice.judge.strategy.JudgeStrategy;
import org.springframework.stereotype.Service;

@Service
public class JudgeManager {
    /**
     *
     * @return
     */
    JudgeInfo dojudge(JudgeContext judgeContext)
    {
        QuestionSubmit questionSubmit=judgeContext.getQuestionSubmit();
        JudgeStrategy judgeStrategy=new DefaultJudgeStrategy();
        if(questionSubmit.getLanguage().equals("java"))
        {
            judgeStrategy=new JavaLanguageJudgeStrategy();
        }
        JudgeInfo judgeInfo =judgeStrategy.dojudge(judgeContext);
        return judgeInfo;
    }
}
