package com.ead.course.client;

import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserCourseDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
import com.ead.course.services.exceptions.ClientException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;


    public Page<UserDTO> getAllUsersByCourseId(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;
        String url = utilsService.getAllUsersByCourseId(courseId,pageable);
        log.debug("Request URL : {}" , url);
        log.info("Request URL : {}" , url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};
            result = restTemplate.exchange(url, HttpMethod.GET,null,responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {}" , searchResult.size());
        }catch (HttpStatusCodeException e) {
            log.error("Error request /courses {}", e);
        }
        log.info("Ending request /courses userId {}" , courseId);
        return result.getBody();
    }


    public UserDTO findUserById(UUID userId) {
        String url = utilsService.findUserById(userId);
        ResponseEntity<UserDTO> result = restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
        return result.getBody();
    }


    public void postSubscriptionUserInCourse(UUID courseId,UUID userId){
        try {
            String url = utilsService.postSubscriptionUserInCourse(userId);
            UserCourseDTO userCourseDTO = new UserCourseDTO();
            userCourseDTO.setCourseId(courseId);
            userCourseDTO.setUserId(userId);
            restTemplate.postForObject(url,userCourseDTO,String.class);
        } catch (HttpClientErrorException e) {
            throw new ClientException(e.getMessage());
        }
    }

}
