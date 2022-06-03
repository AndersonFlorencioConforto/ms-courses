package com.ead.course.client;

import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;


    public Page<UserDTO> getAllUsersByCourseId(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;
        String url = utilsService.createUrl(courseId,pageable);
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

}
