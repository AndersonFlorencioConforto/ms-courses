package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query(nativeQuery = true,value = "SELECT * FROM TB_LESSONS WHERE module_module_id = :moduleId")
    List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);
}