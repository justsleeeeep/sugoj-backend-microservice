package com.sug.sugojbackendjudgeservice.judge.codesandbox.impl;


import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.CodeSandbox;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        System.out.println("ThirdParty");
        return null;
    };
}
