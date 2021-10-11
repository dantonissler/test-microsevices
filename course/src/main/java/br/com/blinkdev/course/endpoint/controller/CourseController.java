package br.com.blinkdev.course.endpoint.controller;

import br.com.blinkdev.core.model.Course;
import br.com.blinkdev.course.endpoint.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/admin/course")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "Endpooint to manage course")
public class CourseController {
    private final CourseService courseService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all avaliable courses", response = Course[].class)
    public ResponseEntity<Iterable<Course>> list(Pageable pageable) {
        return new ResponseEntity<>(courseService.list(pageable), HttpStatus.OK);
    }
}
