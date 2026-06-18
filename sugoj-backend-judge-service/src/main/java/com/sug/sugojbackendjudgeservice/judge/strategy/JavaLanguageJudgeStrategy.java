package com.sug.sugojbackendjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.sug.sugbackendmodel.model.codesandbox.JudgeInfo;
import com.sug.sugbackendmodel.model.dto.question.JudgeCase;
import com.sug.sugbackendmodel.model.dto.question.JudgeConfig;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugbackendmodel.model.enums.JudgeInfoMessageEnum;


import java.util.List;

public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo dojudge(JudgeContext judgeContext)
    {
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        List<String> outList = judgeContext.getOutputList();
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Question question = judgeContext.getQuestion();
        Long Time = judgeInfo.getTime();
        Long Memory = judgeInfo.getMemory();
        judgeInfoResponse.setTime(Time);
        judgeInfoResponse.setMemory(Memory);

        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
        if(outList==null)
        {
            if(judgeInfo.getMessage().equals("compile error"))
            {
                judgeInfoResponse.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.toString());
                return judgeInfoResponse;
            }
        }
        if (outList.size() != judgeCaseList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.toString());
            return judgeInfoResponse;
        }
        for (int i = 0; i < judgeCaseList.size(); i++) {
            if (!outList.get(i).equals(judgeCaseList.get(i).getOutput())) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.toString());
                return judgeInfoResponse;
            }
        }
        String judgeConfigstr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigstr, JudgeConfig.class);
        Long needTime = judgeConfig.getTimelimit()*2;
        Long needMemory = judgeConfig.getMemorylimit();
        if (Time > needTime) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.toString());
            return judgeInfoResponse;
        }
        if (Memory > needMemory) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.toString());
            return judgeInfoResponse;
        }
        judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.toString());


        return judgeInfoResponse;
    }
}
