package com.sug.sugojbackendserviceclient.service;


import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 判题服务
 */
@FeignClient(name="sugoj-backend-judge-service",path="/api/judge/inner")
public interface JudgeFeignClient {
     /**
      *
      * @param questionSubmitId
      * @return
      */
     @PostMapping("/do")
     QuestionSubmit doJudge(@RequestBody Long questionSubmitId);
}
