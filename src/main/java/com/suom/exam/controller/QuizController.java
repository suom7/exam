package com.suom.exam.controller;

import com.suom.exam.domain.Quiz;
import com.suom.exam.services.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/1.0/quiz")
public class QuizController {

    private QuizService service;

    public QuizController(QuizService service) {
        this.service = service;
    }

    @GetMapping("/{area}")
    public ResponseEntity<List<Quiz>> findAll(@PathVariable String area) throws IOException {
        List<Quiz> results = new ArrayList<>();
        switch (area) {
            case "area-i": results = service.getAreaI(); break;
            case "area-ii": results = service.getAreaII(); break;
            case "area-iii": results = service.getAreaIII(); break;
            case "area-iv":  results = service.getAreaIV(); break;
            case "area-i-iii-iv": results = service.mergeArea123(); break;
        }
        return ResponseEntity.ok(results);
    }
}
