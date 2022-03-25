package com.ead.course.repositories;

import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

//    @EntityGraph(attributePaths = {"course"})
//    ModuleModel findByTitle(String title);
    @Query(nativeQuery = true,value = "SELECT * FROM TB_MODULES WHERE course_course_id = :courseId")
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);
}