package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final AuditionLogger logger = new AuditionLogger();

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
        final ClientHttpRequestExecution execution)
        throws IOException {
        final ClientHttpResponse response = execution.execute(request, body);
        logReqRes(request, body, response);

        return response;
    }

    private void logReqRes(final HttpRequest request, final byte[] body, final ClientHttpResponse response)
        throws IOException {

        if (LOG.isInfoEnabled()) {
            // Logging request
            logger.info(LOG, "Method: {}", request.getMethod().toString());
            logger.info(LOG, "URI: {}", request.getURI().toString());
            logger.info(LOG, "Request Body: {}", new String(body));

            // Logging response
            try (InputStreamReader inputStreamReader = new InputStreamReader(response.getBody(),
                StandardCharsets.UTF_8);) {
                final String responseBody = new BufferedReader(inputStreamReader)
                    .lines()
                    .collect(Collectors.joining("\n"));
                logger.info(LOG, "Response body: {}", responseBody);
            }
        }

    }
}
