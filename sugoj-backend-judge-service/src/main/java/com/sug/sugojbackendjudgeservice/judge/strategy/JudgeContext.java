package com.sug.sugojbackendjudgeservice.judge.strategy;


import com.sug.sugbackendmodel.model.codesandbox.JudgeInfo;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {

    List<String> outputList;
    JudgeInfo judgeInfo;
    Question question;
    QuestionSubmit questionSubmit;
}
