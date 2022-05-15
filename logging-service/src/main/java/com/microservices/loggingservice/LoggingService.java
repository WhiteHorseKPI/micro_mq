package com.microservices.loggingservice;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoggingService {

    private HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
    private IMap<String, String> messages = hzInstance.getMap("logging_map");

    public void addToLog(final String uuid, final String message) {
        messages.put(uuid, message);
    }

    public Map<String, String> log() {
        return messages;
    }
}
