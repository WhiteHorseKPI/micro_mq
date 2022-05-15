package com.microservices.messagesservice;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessagesService implements Runnable {

    Logger logger = LoggerFactory.getLogger(MessagesService.class);
    private final List<String> messages = new ArrayList<>();
    private HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
    private IQueue<String> queue = hzInstance.getQueue("my-queue");

    public void run() {
        try {
            writeMessage();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void writeMessage() throws InterruptedException {
        while (true) {
            if (queue.size() != 0) {
                String message = queue.take();
                messages.add(message);
                logger.info("Message: " + message);
            }
            Thread.sleep( 500 );
        }
    }

    public String getMessages() {
        return messages.toString();
    }
}
