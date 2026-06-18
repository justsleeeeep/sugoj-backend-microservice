package com.sug.sugojbackendquestionservice.controller.inner;

import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugojbackendquestionservice.service.QuestionService;
import com.sug.sugojbackendquestionservice.service.QuestionSubmitService;
import com.sug.sugojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
@RestController()
@RequestMapping("/inner")

public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    QuestionService questionService;
    @Resource
    QuestionSubmitService questionSubmitService;
    @Override
    @GetMapping("get/id")
    public Question getQuestionById(@RequestParam long questionid) {
        return questionService.getById(questionid);
    }

    @Override
    @GetMapping("question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("update/id")
    public boolean updateQuestionById(@RequestBody Question question) {
        return questionService.updateById(question);
    }

    @Override
    @PostMapping("question_submit/update/id")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }
}
