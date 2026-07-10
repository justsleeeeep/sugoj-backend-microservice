package com.sug.sugojbackendquestionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sug.sugbackendmodel.model.entity.QuestionSubmit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author 钱晨
* @description 针对表【question_submit(题目提交)】的数据库操作Mapper
* @createDate 2026-05-15 15:40:01
* @Entity com.sug.project.model.entity.QuestionSubmit
*/
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {

    /**
     * CAS 原子更新状态（乐观锁）
     * 只有当前 status == expectedStatus 时才更新为 newStatus
     * @return 影响行数（1=抢锁成功，0=已被别人抢先）
     */
    @Update("UPDATE question_submit SET status = #{newStatus} WHERE id = #{id} AND status = #{expectedStatus}")
    int updateStatusCas(@Param("id") Long id,
                        @Param("expectedStatus") Integer expectedStatus,
                        @Param("newStatus") Integer newStatus);

}




