package com.sug.sugojbackendserviceclient.service;


import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
* @author 钱晨
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2026-05-15 15:37:50
*/
@FeignClient(name="sugoj-backend-question-service",path="/api/question/inner")
public interface QuestionFeignClient {

//      questionSubmitService.getById()
//      questionSubmitService.updateById()
//      questionService.getById()
//      questionService.updateById()
    @GetMapping("get/id")
    Question getQuestionById(@RequestParam("questionid") long questionid);

    @GetMapping("question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("update/id")
    boolean updateQuestionById(@RequestBody Question question);

    @PostMapping("question_submit/update/id")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    /**
     * CAS 原子更新提交状态（乐观锁）
     * 只有当前 status == expectedStatus 时才更新为 newStatus
     */
    @PostMapping("question_submit/update/status/cas")
    boolean updateQuestionSubmitStatusCas(@RequestParam("id") Long id,
                                          @RequestParam("expectedStatus") Integer expectedStatus,
                                          @RequestParam("newStatus") Integer newStatus);
}
