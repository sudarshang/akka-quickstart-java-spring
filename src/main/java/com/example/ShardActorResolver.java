package com.example;

import akka.cluster.sharding.ShardRegion;
import org.springframework.stereotype.Component;

@Component
public class ShardActorResolver implements ShardRegion.MessageExtractor {

    @Override
    public String entityId(Object message) {
        return (String) message;
    }

    @Override
    public Object entityMessage(Object message) {
        return message;
    }

    @Override
    public String shardId(Object message) {
        return "0";
    }
}
