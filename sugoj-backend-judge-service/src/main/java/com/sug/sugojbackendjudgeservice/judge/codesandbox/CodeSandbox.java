package com.sug.sugojbackendjudgeservice.judge.codesandbox;


import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
