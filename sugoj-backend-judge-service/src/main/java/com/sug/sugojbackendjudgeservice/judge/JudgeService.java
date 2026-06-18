package com.sug.sugojbackendjudgeservice.judge;


import com.sug.sugbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {
     /**
      *
      * @param questionSubmitId
      * @return
      */
     QuestionSubmit doJudge(Long questionSubmitId);
}
