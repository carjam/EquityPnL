package com.companyx.equity.repository;

import com.companyx.equity.dto.CandleDto;
import com.companyx.equity.dto.MarkDto;
import com.companyx.equity.error.ResponseVerificationException;
import com.companyx.equity.utility.DateUtils;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Configuration
public class FinhubRepository {
    private final int BACKOFF_DELAY = 30;
    private final String FINHUB_TOKEN_KEY = "X-Finnhub-Token";
    private String FINHUB_URL;
    private String FINHUB_KEY;

    private final String SYMBOL_KEY = "symbol";
    private final String RESOLUTION_KEY = "resolution";
    private final String FROM_KEY = "from";
    private final String TO_KEY = "to";
    private final String DAILY = "D";

    @Autowired
    FinhubRepository(
            @Value("${finhub.url}")String FINHUB_URL
            , @Value("${finhub.key}")String FINHUB_KEY
    ) {
        this.FINHUB_URL = FINHUB_URL;
        this.FINHUB_KEY = FINHUB_KEY;

        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\nFinhubRepository Constructed with  "
                + this.FINHUB_URL
        );
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = BACKOFF_DELAY))
    public MarkDto getMark(String symbol) throws JsonProcessingException {
        final String QUOTE = "/quote";

        Mono<String> result = WebClient.create(FINHUB_URL)
                .get()
                .uri(
                    uriBuilder -> uriBuilder
                    .path(QUOTE)
                    .queryParam(SYMBOL_KEY, symbol)
                    .build()
                )
                .headers(httpHeaders -> createHeaders(httpHeaders))
                .exchangeToMono(
                        response -> verifyStatusCode(response)
                );

        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\nCompleted request " + symbol
        );
        JsonNode response = verifyResponse(result.block());
        String sResponse = new ObjectMapper().writeValueAsString(response);
        return new ObjectMapper().readValue(sResponse, MarkDto.class);
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = BACKOFF_DELAY))
    public CandleDto getCandle(String symbol, Date from, Date to) throws JsonProcessingException {
        final String CANDLE = "/stock/candle";

        Mono<String> result = WebClient.create(FINHUB_URL)
                .get()
                .uri(
                        uriBuilder -> uriBuilder
                                .path(CANDLE)
                                .queryParam(SYMBOL_KEY, symbol)
                                .queryParam(RESOLUTION_KEY, DAILY)
                                .queryParam(FROM_KEY, DateUtils.epochFromDate(from))
                                .queryParam(TO_KEY, DateUtils.epochFromDate(to))
                                .build()
                )
                .headers(httpHeaders -> createHeaders(httpHeaders))
                .exchangeToMono(
                        response -> verifyStatusCode(response)
                );

        log.info(new Timestamp(System.currentTimeMillis()) + " "
                + this.getClass() + ":"
                + new Throwable().getStackTrace()[0].getMethodName()
                + "\nCompleted request " + symbol
        );
        JsonNode response = verifyResponse(result.block());
        String sResponse = new ObjectMapper().writeValueAsString(response);
        return new ObjectMapper().readValue(sResponse, CandleDto.class);
    }

    ///
    private Mono<String> verifyStatusCode(ClientResponse response){
        String defaultMsg = "Error occurred contacting external API.";
        if(response.statusCode().equals(HttpStatus.OK)) {
            return response.bodyToMono((String.class));
        } else {
            return response.createException().flatMap(Mono::error);
        }
    }

    private JsonNode verifyResponse(String response) {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        String defaultMsg = "Error occurred contacting external API.";
        try {
            JsonParser parser = factory.createParser(response);
            JsonNode res = mapper.readTree(parser);
            return res;
        } catch(IOException | NullPointerException e) {
            log.error(new Timestamp(System.currentTimeMillis()) + " "
                    + this.getClass() + ":"
                    + new Throwable().getStackTrace()[0].getMethodName()
                    + "\n" + defaultMsg + "\n" + response
            );
            throw new ResponseVerificationException(defaultMsg);
        }
    }

    private HttpHeaders createHeaders(HttpHeaders httpHeaders) {
        httpHeaders.set(FINHUB_TOKEN_KEY, FINHUB_KEY);
        return httpHeaders;
    }
}
