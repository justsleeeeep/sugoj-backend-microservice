package com.sug.sugojbackendjudgeservice.judge.codesandbox.impl;



import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.sug.sugbackendmodel.model.codesandbox.JudgeInfo;
import com.sug.sugbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.sug.sugbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.CodeSandbox;

import java.util.List;

public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        System.out.println("Example");
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    };
}



