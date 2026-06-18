package com.sug.sugojbackendjudgeservice.judge.codesandbox;


import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox{
    public CodeSandboxProxy(CodeSandbox codeSandbox)
    {
        this.codeSandbox=codeSandbox;
    }
    public CodeSandbox codeSandbox;
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        log.info(executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse=codeSandbox.executeCode(executeCodeRequest);
        log.info(executeCodeResponse.toString());
        return executeCodeResponse;
    }

}
