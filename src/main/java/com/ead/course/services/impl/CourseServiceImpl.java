package com.ead.course.services.impl;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;


    @Transactional
    @Override
    public void delete(UUID courseId) {
        Optional<CourseModel> findById = courseRepository.findById(courseId);
        CourseModel courseModel = findById.orElseThrow(() -> new ResourceNotFoundException("Course Not found"));
        List<ModuleModel> modules = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!modules.isEmpty()) {
            for (ModuleModel module : modules) {
                List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessons.isEmpty()) {
                    lessonRepository.deleteAll(lessons);
                }
                moduleRepository.deleteAll(modules);
            }
        }
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseDTO courseDTO) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        Optional<CourseModel> course = courseRepository.findById(courseId);
        course.orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return course;
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public CourseModel update(UUID courseId, CourseDTO courseDTO) {
        Optional<CourseModel> findById = courseRepository.findById(courseId);
        CourseModel course = findById.orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setImageUrl(courseDTO.getImageUrl());
        course.setCourseStatus(courseDTO.getCourseStatus());
        course.setCourseLevel(courseDTO.getCourseLevel());
        return courseRepository.save(course);
    }
}
