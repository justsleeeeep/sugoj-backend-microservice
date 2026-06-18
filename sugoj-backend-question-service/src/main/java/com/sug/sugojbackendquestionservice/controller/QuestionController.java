package com.sug.sugojbackendquestionservice.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sug.sugbackendmodel.model.dto.question.QuestionAddRequest;
import com.sug.sugbackendmodel.model.dto.question.QuestionQueryRequest;
import com.sug.sugbackendmodel.model.dto.question.QuestionUpdateRequest;
import com.sug.sugbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.sug.sugbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.sug.sugbackendmodel.model.entity.Question;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import com.sug.sugbackendmodel.model.entity.User;
import com.sug.sugbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.sug.sugbackendmodel.model.vo.QuestionVO;
import com.sug.sugojbackendcommon.annotation.AuthCheck;
import com.sug.sugojbackendcommon.common.BaseResponse;
import com.sug.sugojbackendcommon.common.DeleteRequest;
import com.sug.sugojbackendcommon.common.ErrorCode;
import com.sug.sugojbackendcommon.common.ResultUtils;
import com.sug.sugojbackendcommon.constant.CommonConstant;
import com.sug.sugojbackendcommon.exception.BusinessException;
import com.sug.sugojbackendquestionservice.service.QuestionService;
import com.sug.sugojbackendquestionservice.service.QuestionSubmitService;
import com.sug.sugojbackendserviceclient.service.JudgeFeignClient;
import com.sug.sugojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 问题接口
 *
 * @author sug
 */
@RestController
@RequestMapping("/")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private QuestionSubmitService questionSubmitService;


    // region 增删改查

    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        if (questionAddRequest.getTags() != null) {
            question.setTags(JSONUtil.toJsonStr(questionAddRequest.getTags()));
        }
        if (questionAddRequest.getJudgeCase() != null) {
            question.setJudgeCase(JSONUtil.toJsonStr(questionAddRequest.getJudgeCase()));
        }
        if (questionAddRequest.getJudgeConfig() != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(questionAddRequest.getJudgeConfig()));
        }
        // 校验
        System.out.println("👉 拷贝并转换后的 question 对象: " + question);
        questionService.validQuestion(question, true);
        User loginUser = userFeignClient.getLoginUser(request);
        question.setUserId(loginUser.getId());
        boolean result = questionService.save(question);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        if (oldQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param questionUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest,
                                                HttpServletRequest request) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        if (questionUpdateRequest.getTags() != null) {
            question.setTags(JSONUtil.toJsonStr(questionUpdateRequest.getTags()));
        }
        if (questionUpdateRequest.getJudgeCase() != null) {
            question.setJudgeCase(JSONUtil.toJsonStr(questionUpdateRequest.getJudgeCase()));
        }
        if (questionUpdateRequest.getJudgeConfig() != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(questionUpdateRequest.getJudgeConfig()));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        User user = userFeignClient.getLoginUser(request);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        if (oldQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldQuestion.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取脱敏
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionByIdVO(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        if (question.getTags() != null) {
            questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        }
        return ResultUtils.success(questionVO);
    }

    /**
     * 根据 id 获取全部
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        Question question = questionService.getById(id);
        if ((!question.getUserId().equals(loginUser.getId())) && (!userFeignClient.isAdmin(request))) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param questionQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<Question>> listQuestion(QuestionQueryRequest questionQueryRequest) {
        Question questionQuery = new Question();
        if (questionQueryRequest != null) {
            BeanUtils.copyProperties(questionQueryRequest, questionQuery);
        }
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>(questionQuery);
        List<Question> questionList = questionService.list(queryWrapper);
        return ResultUtils.success(questionList);
    }

    /**
     * 分页获取列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<QuestionVO>> listQuestionByPage(QuestionQueryRequest questionQueryRequest, HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question questionQuery = new Question();
        BeanUtils.copyProperties(questionQueryRequest, questionQuery);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        String content = questionQuery.getContent();
        // content 需支持模糊搜索
        questionQuery.setContent(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(questionQuery.getId() != null,
                "id",
                questionQuery.getId());

        queryWrapper.like(StringUtils.isNotBlank(questionQuery.getTitle()),
                "title",
                questionQuery.getTitle());

        queryWrapper.like(StringUtils.isNotBlank(questionQuery.getContent()),
                "content",
                questionQuery.getContent());

        queryWrapper.like(StringUtils.isNotBlank(questionQuery.getTags()),
                "tags",
                questionQuery.getTags());
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<Question> questionPage = questionService.page(new Page<>(current, size), queryWrapper);
        Page<QuestionVO> questionVOPage = new Page<>(
                questionPage.getCurrent(),
                questionPage.getSize(),
                questionPage.getTotal()
        );

        List<QuestionVO> questionVOList = questionPage.getRecords().stream().map(question -> {
            QuestionVO questionVO = new QuestionVO();
            BeanUtils.copyProperties(question, questionVO);
            if (question.getTags() != null) {
                questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
            }
            return questionVO;
        }).collect(Collectors.toList());

        questionVOPage.setRecords(questionVOList);
        return ResultUtils.success(questionVOPage);
    }

    // endregion


    @Resource
    private JudgeFeignClient judgeFeignClient;
    /**
     * 创建
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/questionsubmit/add")
    public BaseResponse<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest, questionSubmit);
        // 校验
        questionSubmitService.validQuestionSubmit(questionSubmit, true);
        User loginUser = userFeignClient.getLoginUser(request);
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean result = questionSubmitService.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        questionService.incrementSubmitNum(questionId);
        long newQuestionSubmitId = questionSubmit.getId();
        CompletableFuture.runAsync(()->{
            judgeFeignClient.doJudge(newQuestionSubmitId);
        });
        return ResultUtils.success(newQuestionSubmitId);
    }

    /**
     * 分页获取题目提交列表
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @GetMapping("/questionsubmit/list/page")
    public BaseResponse<Page<QuestionSubmit>> listQuestionSubmitByPage(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        if (questionSubmitQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(
                new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitPage);
    }

}
