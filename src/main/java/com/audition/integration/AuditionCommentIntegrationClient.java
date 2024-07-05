package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPostComment;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@NoArgsConstructor
public class AuditionCommentIntegrationClient {


    @Autowired
    private RestTemplate restTemplate;

    private final static String POSTS_ENDPOINT = "https://jsonplaceholder.typicode.com/posts";
    private final static String COMMENTS_ENDPOINT = "https://jsonplaceholder.typicode.com/comments";


    public List<AuditionPostComment> getCommentsForPost(final Integer postId) {
        try {
            final ResponseEntity<AuditionPostComment[]> responseEntity = restTemplate.getForEntity(
                POSTS_ENDPOINT + "/{id}/comments", AuditionPostComment[].class, postId);
            final AuditionPostComment[] auditionPostComment = responseEntity.getBody();

            if (auditionPostComment.length > 0) {
                return List.of(auditionPostComment);
            } else {
                throw new SystemException("Cannot find comments with Post id " + postId, "Resource Not found",
                    404);
            }

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find comments with Post id " + postId, "Resource Not Found", 404, e);
            } else {
                throw new SystemException(e.getMessage(), "System Error", e.getStatusCode().value(),
                    e);
            }
        }
    }


    public List<AuditionPostComment> getComments(final Map<String, Object> queryParams) {
        try {
            final URI uri = buildUriWithQueryParams(COMMENTS_ENDPOINT, queryParams);
            final ResponseEntity<AuditionPostComment[]> responseEntity = restTemplate.getForEntity(uri,
                AuditionPostComment[].class);
            final AuditionPostComment[] auditionPostComment = responseEntity.getBody();
            if (auditionPostComment != null && auditionPostComment.length > 0) {
                return List.of(auditionPostComment);
            } else {
                throw new SystemException("Cannot find comments with Post id ", "Resource Not Found", 404);
            }

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find comments Resource Not Found", "Resource Not Found", 404, e);
            } else {
                throw new SystemException(e.getMessage(), "System Error", e.getStatusCode().value(),
                    e);
            }
        }
    }


    private URI buildUriWithQueryParams(final String baseUrl, final Map<String, Object> queryParams) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        if (queryParams != null) {
            for (final Map.Entry<String, Object> entry : queryParams.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        return builder.build().toUri();
    }

}
