package com.example.ad.service.impl;

import com.example.ad.model.Feedback;
import com.example.ad.repository.FeedbackRepository;
import com.example.ad.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl
        implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public Feedback saveFeedback(
            Feedback feedback) {

        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {

        return feedbackRepository.findAll();
    }
}