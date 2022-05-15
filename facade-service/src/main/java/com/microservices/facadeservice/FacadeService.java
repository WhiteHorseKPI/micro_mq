package com.microservices.facadeservice;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;


@Service
public class FacadeService {

    static Logger logger = LoggerFactory.getLogger(FacadeService.class);

    static private HazelcastInstance hzInstance;
    static List<WebClient> loggingWebClients;
    static List<WebClient> messagesWebClients;


    public FacadeService() {
        hzInstance = Hazelcast.newHazelcastInstance();
        loggingWebClients = List.of(
                WebClient.create("http://localhost:8890"),
                WebClient.create("http://localhost:8891"),
                WebClient.create("http://localhost:8892")
        );
        messagesWebClients = List.of(
                WebClient.create("http://localhost:8880"),
                WebClient.create("http://localhost:8881")
        );
    }

    public static String messages() {

        WebClient loggingWebClient = getRandomLoggingClient();
        var cachedValues = loggingWebClient
                .get()
                .uri("/log")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        WebClient messagesWebClient = getRandomMessagesClient();
        var message = messagesWebClient
                .get()
                .uri("/message")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return String.format("%s: %s", cachedValues, message);
    }

    static Mono<Void> addMessage(@RequestBody String text) {

        IQueue<String> queue = hzInstance.getQueue("my-queue");
        try {
            queue.put(text);
        } catch (InterruptedException e) {
            logger.error(String.valueOf(e));
        }

        Map<String, Object> message = new HashMap<>();
        message.put("UUID", UUID.randomUUID());
        message.put("Text", text);

        WebClient loggingWebClient = getRandomLoggingClient();
        logger.info(loggingWebClient.toString());

        return loggingWebClient.post()
                .uri("/log")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(message), Map.class)
                .retrieve()
                .bodyToMono(Void.class);
    }


    private static WebClient getRandomLoggingClient() {

        final int numberOfLoggingServices = loggingWebClients.size();
        return loggingWebClients.get(new Random().nextInt(numberOfLoggingServices));
    }

    private static WebClient getRandomMessagesClient() {
        final int numberOfMessagesServices = messagesWebClients.size();
        return messagesWebClients.get(new Random().nextInt(numberOfMessagesServices));
    }
}
