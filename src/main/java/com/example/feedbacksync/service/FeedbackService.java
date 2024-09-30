package com.example.feedbacksync.service;

import com.example.feedbacksync.entity.Feedback;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.payloads.feedback.FeedbackRequest;
import com.example.feedbacksync.payloads.feedback.FeedbackResponse;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.repository.FeedbackRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


/**
 * Feedback Service class to handle feedback related Business logic and operations
 * @Service annotation is used to mark the class as a service provider in Spring
 * 
 */
@Service
public class FeedbackService {

    private final UserService userService;
    private final FeedbackRepository feedbackRepository;

    private final ProfileService profileService;

    public FeedbackService(UserService userService, FeedbackRepository feedbackRepository, ProfileService profileService) {
        this.userService = userService;
        this.feedbackRepository = feedbackRepository;
        this.profileService = profileService;
    }

    /**
     * Create a new feedback for a user
     * @param feedbackRequest  FeedbackRequest
     * @return FeedbackResponse
     * @throws Exception if user not found
     */
    public FeedbackResponse createFeedback(FeedbackRequest feedbackRequest) throws Exception {

        User giver = userService.findUserByUserId(feedbackRequest.getGiverId());
        if(giver == null){
            throw new Exception("User not found with id "+ feedbackRequest.getGiverId());
        }

        User receiver = userService.findUserByUserId(feedbackRequest.getReceiverId());
        if(receiver == null){
            throw new Exception("User not found with id "+ feedbackRequest.getGiverId());
        }

        Feedback feedback = new Feedback();
        feedback.setContent(feedbackRequest.getContent());
        feedback.setGiver(giver);
        feedback.setReceiver(receiver);

        Feedback saveFeedback = feedbackRepository.save(feedback);

        return getFeedbackResponse(saveFeedback);
    }

    /**
     * Get feedback by feedback id
     * @param feedbackId Long feedback id
     * @return Feedback or null
     */
    public FeedbackResponse getFeedbackById(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null) {
            return null;
        }
        return getFeedbackResponse(feedback);
    }

    /**
     * Get all feedbacks given by a user
     * @param giverId User
     * @return List of Feedback
     */
    public List<FeedbackResponse> getAllFeedbacksByGiver(Long giverId) {
        
        User giver = userService.findUserByUserId(giverId);
        if (giver == null) {
            throw new IllegalArgumentException("User not found with id " + giverId);
        }

        List<Feedback> feedbacks = feedbackRepository.findAllByGiver(giver);
        return getFeedbackResponses(feedbacks);
    }


    /**
     * Get all feedbacks received by a user
     * @param receiverId User
     * @return List of Feedback
     */
    public List<FeedbackResponse> getAllFeedbacksByReceiver(Long receiverId) {

        User receiver = userService.findUserByUserId(receiverId);
        if (receiver == null) {
            throw new IllegalArgumentException("User not found with id " + receiverId);
        }

        List<Feedback> feedbacks = feedbackRepository.findAllByReceiver(receiver);
        return getFeedbackResponses(feedbacks);
    }

    /**
     * Get all feedbacks shared with manager by a user
     * @param receiverId Long receiver id 
     * @return List of Feedback
     */
    public List<FeedbackResponse> getAllFeedbacksSharedWithManager(Long receiverId) {
        User receiver = userService.findUserByUserId(receiverId);
        if (receiver == null) {
            throw new IllegalArgumentException("User not found with id " + receiverId);
        }

        List<Feedback> feedbacks = feedbackRepository.findAllByReceiverAndIsSharedWithManager(receiver, true);
        return getFeedbackResponses(feedbacks);
    }

    /**
     * Update feedback 
     * @param feedbackId Long feedback id
     * @param feedbackRequest FeedbackRequest
     * @return FeedbackResponse
     */
    public FeedbackResponse updateFeedback(Long feedbackId, FeedbackRequest feedbackRequest) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null) {
            throw new IllegalArgumentException("Feedback not found with id " + feedbackId);
        }

        feedback.setContent(feedbackRequest.getContent());
        feedback.setIsSharedWithManager(feedbackRequest.getIsSharedWithManager());

        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return getFeedbackResponse(updatedFeedback);
    }


    /**
     * Convert List of Feedback to List of FeedbackResponse
     * @return List of Feedback Response
     */
    private List<FeedbackResponse> getFeedbackResponses(List<Feedback> feedbacks) {
        return feedbacks.stream().map(feedback -> {
            FeedbackResponse response = getFeedbackResponse(feedback);
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * Convert Feedback to FeedbackResponse
     * @param feedback Feedback
     * @return FeedbackResponse
     */
    private FeedbackResponse getFeedbackResponse(Feedback feedback) {
        ProfileResponse giverProfile = profileService.getUserProfile(feedback.getGiver().getUsername());
        ProfileResponse receiverProfile = profileService.getUserProfile(feedback.getReceiver().getUsername());

        FeedbackResponse response = new FeedbackResponse();
        response.setFeedbackId(feedback.getFeedbackId());
        response.setContent(feedback.getContent());
        response.setCreatedAt(feedback.getCreatedAt());
        response.setIsSharedWithManager(feedback.getIsSharedWithManager());
        response.setReceiverName(receiverProfile.getFullName().isEmpty()? feedback.getReceiver().getUsername(): receiverProfile.getFullName());
        response.setGiverName(giverProfile.getFullName().isEmpty()? feedback.getGiver().getUsername(): giverProfile.getFullName());

        return response;
    }
            
}