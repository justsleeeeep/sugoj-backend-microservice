package com.sug.sugojbackendjudgeservice.controller.inner;

import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugojbackendjudgeservice.judge.JudgeService;
import com.sug.sugojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.tools.JavaFileObject;
@RestController()
@RequestMapping("/inner")

public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    JudgeService judgeService;
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestBody Long questionSubmitId) {
       return judgeService.doJudge(questionSubmitId);
    }
}
