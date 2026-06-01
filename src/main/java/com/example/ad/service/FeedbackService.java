package com.example.ad.service;

import com.example.ad.model.Feedback;
import java.util.List;

public interface FeedbackService {

    Feedback saveFeedback(Feedback feedback);
    
    List<Feedback> getAllFeedbacks();
}