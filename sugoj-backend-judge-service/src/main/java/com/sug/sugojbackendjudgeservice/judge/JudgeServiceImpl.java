package com.sug.sugojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.sug.sugbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.sug.sugbackendmodel.model.codesandbox.JudgeInfo;
import com.sug.sugbackendmodel.model.dto.question.JudgeCase;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.sug.sugbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.exception.BusinessException;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.sug.sugojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.sug.sugojbackendjudgeservice.judge.strategy.JudgeContext;
import com.sug.sugojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    QuestionFeignClient questionSubmitService;

    @Resource
    QuestionFeignClient questionFeignClient;

    @Resource
    JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;
    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit=questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if(questionSubmit==null) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        Long questionid=questionSubmit.getQuestionId();
        Question question=questionFeignClient.getQuestionById(questionid);
        if(question==null) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");

        // CAS 原子抢占：只有 status == WAITING 时才更新为 RUNNING
        boolean grabbed = questionFeignClient.updateQuestionSubmitStatusCas(
                questionSubmitId,
                QuestionSubmitStatusEnum.WAITING.getValue(),
                QuestionSubmitStatusEnum.RUNNING.getValue());
        if (!grabbed) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题");
        }


        CodeSandbox codeSandbox= CodeSandboxFactory.newInstance(type);
        codeSandbox=new CodeSandboxProxy(codeSandbox);

        String code=questionSubmit.getCode();
        String language= questionSubmit.getLanguage();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList=judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest=ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse=codeSandbox.executeCode(executeCodeRequest);
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfoResponse =judgeManager.dojudge(judgeContext);

        QuestionSubmit questionSubmitUpdateJudgeInfo= new QuestionSubmit();
        questionSubmitUpdateJudgeInfo.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdateJudgeInfo.setId(questionSubmitId);
        questionSubmitUpdateJudgeInfo.setJudgeInfo(judgeInfoResponse.toString());
        boolean updateJudgeInfo=questionFeignClient.updateQuestionSubmitById(questionSubmitUpdateJudgeInfo);
        if(!updateJudgeInfo)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交结果更新失败");
        }
        QuestionSubmit newQuestionSubmit=questionFeignClient.getQuestionSubmitById(questionSubmitId);
        String Test= JudgeInfoMessageEnum.ACCEPTED.toString();
        if(judgeInfoResponse.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.toString()))
        {
            question.setAcceptNum(question.getAcceptNum()+1);
            boolean updateAcceptNum = questionFeignClient.updateQuestionById(question);
            if(!updateAcceptNum)
            {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目AC数量更新失败");
            }
        }
        return newQuestionSubmit;
    }
}
