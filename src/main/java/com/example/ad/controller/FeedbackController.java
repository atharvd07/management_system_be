package com.example.ad.controller;

import com.example.ad.model.Feedback;
import com.example.ad.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/save")
    public Feedback saveFeedback(
            @RequestBody Feedback feedback) {

        return feedbackService.saveFeedback(feedback);
    }
}