package com.sug.sugojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.exception.BusinessException;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import org.apache.commons.lang3.StringUtils;

public class RemoteCodeSandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER="auth";
    private static final String AUTH_REQUEST_SECRET="secret";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest)
    {
        System.out.println("Remote");
        String jsonStr= JSONUtil.toJsonStr(executeCodeRequest);
        String url="192.168.40.135:7528/executeCode";
        String resultStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)
                .body(jsonStr)
                .execute()
                .body();
        if(StringUtils.isBlank(resultStr))
        {
            throw new BusinessException(ErrorCode.API_REQUEST_EARROR,resultStr);
        }
        return JSONUtil.toBean(resultStr,ExecuteCodeResponse.class);
    };
}
