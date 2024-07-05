package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class AuditionPostIntegrationClient {


    @Autowired
    private RestTemplate restTemplate;

    private final static String POSTS_ENDPOINT = "https://jsonplaceholder.typicode.com/posts";
    private final static String COMMENTS_ENDPOINT = "https://jsonplaceholder.typicode.com/comments";

    public List<AuditionPost> getPosts(final Map<String, Object> queryParams) {
        try {
            final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(POSTS_ENDPOINT);
            if (queryParams != null) {
                for (final Map.Entry<String, Object> entry : queryParams.entrySet()) {
                    builder.queryParam(entry.getKey(), entry.getValue());
                }
            }

            final URI uri = builder.build().toUri();
            final ResponseEntity<AuditionPost[]> responseEntity = restTemplate.getForEntity(uri,
                AuditionPost[].class);
            final AuditionPost[] auditionPost = Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new SystemException("No post available", "Resource Not Found",
                    404));

            return List.of(auditionPost);

        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("No post available", "Resource Not Found",
                    404, e);
            } else {
                throw new SystemException(e.getMessage(), "System error ", e.getStatusCode().value(), e);
            }

        }

    }


    public AuditionPost getPostById(final String id) {
        try {
            final ResponseEntity<AuditionPost> responseEntity = restTemplate.getForEntity(POSTS_ENDPOINT + "/{id}",
                AuditionPost.class, id);
            return responseEntity.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with given id " + id, "Resource Not Found",
                    404, e);
            } else {
                throw new SystemException(e.getMessage(), "System error ", e.getStatusCode().value(), e);
            }
        }
    }


}
