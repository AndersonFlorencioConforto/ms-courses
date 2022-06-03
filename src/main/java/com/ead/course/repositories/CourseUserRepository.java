package com.ead.course.repositories;

import com.ead.course.models.CourseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {

}
