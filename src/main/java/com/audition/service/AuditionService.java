package com.audition.service;

import com.audition.integration.AuditionCommentIntegrationClient;
import com.audition.integration.AuditionPostIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class AuditionService {

    @Autowired
    private AuditionPostIntegrationClient auditionPostIntegrationClient;

    @Autowired
    private AuditionCommentIntegrationClient auditionCommentIntegrationClient;


    public List<AuditionPost> getPosts(final Map<String, Object> queryParams) {
        return auditionPostIntegrationClient.getPosts(queryParams);
    }

    public AuditionPost getPostById(final String postId) {
        return auditionPostIntegrationClient.getPostById(postId);
    }

    public List<AuditionPostComment> getCommentsForPost(final Integer postId) {
        return auditionCommentIntegrationClient.getCommentsForPost(postId);
    }

    public List<AuditionPostComment> getComments(final Map<String, Object> filters) {
        return auditionCommentIntegrationClient.getComments(filters);
    }
}
