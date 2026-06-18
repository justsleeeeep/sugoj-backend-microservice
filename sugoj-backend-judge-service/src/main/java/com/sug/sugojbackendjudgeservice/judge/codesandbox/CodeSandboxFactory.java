package com.sug.sugojbackendjudgeservice.judge.codesandbox;


import com.sug.sugojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂模式
 */
public class CodeSandboxFactory {
    /**
     * 创建沙箱实例
     * @param type
     * @return
     */
    public static CodeSandbox newInstance(String type)
    {
        switch (type){
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdparty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }

}
