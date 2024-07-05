package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.service.AuditionService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

@RestController
@NoArgsConstructor
@Validated
public class AuditionController {

    @Autowired
    AuditionService auditionService;
    @Autowired
    private View error;

    /**
     * This endpoint provides all posts which can be filtered using userId or PostId.
     *
     * @param userId : Id of the user
     * @param id     : Id of the post
     * @return List of AuditionPost
     */
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(required = false) @Positive final Integer userId,
        @RequestParam(required = false) @Positive final Integer id) {

        final Map<String, Object> queryParam = new ConcurrentHashMap<>();
        if (userId != null) {
            queryParam.put("userId", userId);
        }
        if (id != null) {
            queryParam.put("id", id);
        }

        return auditionService.getPosts(queryParam);
    }

    /**
     * This endpoint returns a post for the given Id.
     *
     * @param postId : Id of the post
     * @return AuditionPost
     */
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostById(@PathVariable("id") final String postId) {

        if (!postId.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("postId must be numeric");
        }
        return auditionService.getPostById(postId);

    }

    /**
     * This method returns all comments associated with the given post.
     *
     * @param postId : Id of the post
     * @return List of AuditionPostComment
     */
    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPostComment> getCommentsForPost(@PathVariable("id") final Integer postId) {
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than zero");
        }
        return auditionService.getCommentsForPost(postId);
    }

    /**
     * This endpoint fetches all the comments, which can be filtered by postId.
     *
     * @param postId : Id of the post
     * @return List of AuditionPostComment
     */
    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPostComment> getComments(@RequestParam(required = false) final Integer postId) {

        final Map<String, Object> queryParams = new ConcurrentHashMap<>();
        if (postId != null) {
            queryParams.put("postId", postId);
        }
        return auditionService.getComments(queryParams);
    }
}
