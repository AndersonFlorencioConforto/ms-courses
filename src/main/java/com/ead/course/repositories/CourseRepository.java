package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    @Query(nativeQuery = true,value = "select case when count(tcu) > 0 THEN true ELSE false END FROM tb_courses_uses tcu WHERE tcu.course_id = :courseId and tcu.user_id = :userId")
    boolean existsByCourseAndUser(@Param("courseId") UUID courseId,@Param("userId") UUID userId);

    @Modifying
    @Query(nativeQuery = true,value = "insert into tb_courses_uses values (:courseId,:userId)")
    void saveCourseUser(@Param("courseId") UUID courseId,@Param("userId") UUID userId);

}