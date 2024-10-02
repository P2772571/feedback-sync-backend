package com.example.feedbacksync.service;

import org.springframework.stereotype.Service;

import com.example.feedbacksync.entity.FeedbackRequest;
import com.example.feedbacksync.entity.User;
import com.example.feedbacksync.entity.enums.RequestStatus;
import com.example.feedbacksync.payloads.feedbackrequest.RequestFeedbackRequest;
import com.example.feedbacksync.payloads.feedbackrequest.RequestFeedbackResponse;
import com.example.feedbacksync.payloads.profile.ProfileResponse;
import com.example.feedbacksync.repository.FeedbackRequestRepository;
import com.example.feedbacksync.repository.UserRespository;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class FeedbackRequestService {

    private final FeedbackRequestRepository feedbackRequestRepository;
    private final UserRespository userRepository;
    private final ProfileService profileService;
    /**
     * Constructor-based dependency injection to inject the FeedbackRequestRepository
     */
    public FeedbackRequestService(FeedbackRequestRepository feedbackRequestRepository, UserRespository userRepository, ProfileService profileService) {
        this.feedbackRequestRepository = feedbackRequestRepository;
        this.userRepository = userRepository;
        this.profileService = profileService;
    }

    /**
     * Method to Create a new feedback request
     * @param requestFeedbackRequest object
     * @return RequestFeedbackResponse object
     * @throws Exception if the requestee or requester is not found
     */
    public RequestFeedbackResponse createFeedbackRequest(RequestFeedbackRequest requestFeedbackRequest) throws Exception {
        // Check if the requester and requestee exist
        User requester = userRepository.findById(requestFeedbackRequest.getRequesterId())
                .orElseThrow(() -> new Exception("Requester not found"));
        User requestee = userRepository.findById(requestFeedbackRequest.getRequesteeId())
                .orElseThrow(() -> new Exception("Requestee not found"));

        // Create a new feedback request
        FeedbackRequest feedbackRequest = new FeedbackRequest();
        feedbackRequest.setRequester(requester);
        feedbackRequest.setRequestee(requestee);
        feedbackRequest.setMessage(requestFeedbackRequest.getMessage());

        // Save the feedback request
        feedbackRequest = feedbackRequestRepository.save(feedbackRequest);
        return getRequestFeedbackResponse(feedbackRequest); 
    }

    /**
     * Method to Get all feedback requests by a user
     * @param userId ID of the user
     * @return list of RequestFeedbackResponse objects
     */
    public List<RequestFeedbackResponse> getFeedbackRequestOfUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ArrayList<>();
        }
        List<FeedbackRequest> feedbackRequests = feedbackRequestRepository.findAllByRequestee(user);
        return feedbackRequests.stream().map(this::getRequestFeedbackResponse).collect(Collectors.toList());
    }

    /**
     * Method to Get all requested feedbacks 
     * @param userId ID of the user
     * @return list of RequestFeedbackResponse objects
     */
    public List<RequestFeedbackResponse> getFeedbackRequestMadeByUser(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            return new ArrayList<>();
        }

        List<FeedbackRequest> feedbackRequests = feedbackRequestRepository.findAllByRequester(user);
        return feedbackRequests.stream().map(this::getRequestFeedbackResponse).collect(Collectors.toList());
    }

    /**
     * Update feedback
     * @param id    ID of the feedback request
     * @return  RequestFeedbackResponse
     */
    public RequestFeedbackResponse update(Long id){
        FeedbackRequest feedbackRequest = feedbackRequestRepository.findById(id).orElse(null);
        if (feedbackRequest == null ){
            throw new IllegalArgumentException("Request is found with the given id "+ id);
        }

        feedbackRequest.setStatus(RequestStatus.COMPLETED);
        return getRequestFeedbackResponse(feedbackRequestRepository.save(feedbackRequest));
    }

    /**
     * Method to Get all feedback requests with a specific status
     * @param status status of the feedback request
     * @return list of RequestFeedbackResponse objects
     */
    public List<RequestFeedbackResponse> getFeedbackRequestsByStatus(RequestStatus status) {
        List<FeedbackRequest> feedbackRequests = feedbackRequestRepository.findAllByStatus(status);
        return feedbackRequests.stream().map(this::getRequestFeedbackResponse).collect(Collectors.toList());
    }

    /**
     * Get Feedback Request by Id
     * @param requestId
     * @return RequestFeedbackResponse
     */
    public RequestFeedbackResponse getFeedbackRequestById(Long requestId){
        FeedbackRequest feedbackRequest = feedbackRequestRepository.findById(requestId).orElse(null);
        if (feedbackRequest == null){
            throw new IllegalArgumentException("Feedback Request not found with given id "+ requestId);
        }

        return getRequestFeedbackResponse(feedbackRequest);
    }
    

    /**
     * Method to Convert list of FeedbackRequest entities to list of RequestFeedbackResponse objects
     * @param feedbackRequests list of FeedbackRequest entities
     * @return list of RequestFeedbackResponse objects
     */
    @SuppressWarnings("unused")
    private List<RequestFeedbackResponse> getRequestFeedbackResponse(List<FeedbackRequest> feedbackRequests) {
        return feedbackRequests.stream().map(this::getRequestFeedbackResponse).collect(Collectors.toList());
    }

    /**
     * Method to convert FeedbackRequest entity to RequestFeedbackResponse object
     * @param feedbackRequest entity
     * @return RequestFeedbackResponse object
     */
    private RequestFeedbackResponse getRequestFeedbackResponse(FeedbackRequest feedbackRequest) {
        ProfileResponse requesteeProfile = profileService.getUserProfile(feedbackRequest.getRequestee().getUsername());
        ProfileResponse requesterProfile = profileService.getUserProfile(feedbackRequest.getRequester().getUsername());

        RequestFeedbackResponse response = new RequestFeedbackResponse();
        response.setRequestId(feedbackRequest.getRequestId());
        response.setMessage(feedbackRequest.getMessage());
        response.setRequesterName(requesterProfile.getFullName() != null ? requesterProfile.getFullName() : requesterProfile.getUsername());
        response.setRequesteeName(requesteeProfile.getFullName() != null ? requesteeProfile.getFullName() : requesteeProfile.getUsername());
        response.setStatus(feedbackRequest.getStatus().name());
        response.setCreatedAt(feedbackRequest.getCreatedAt());
        response.setRequesterId(feedbackRequest.getRequester().getId());
        return response;
    }
    
}
