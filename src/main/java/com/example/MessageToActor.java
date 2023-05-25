package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.sharding.ClusterSharding;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageToActor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageToActor.class);

    private final ObjectMapper mapper;
    private final ActorSystem actorSystem;


    public MessageToActor(ObjectMapper mapper, ActorSystem actorSystem) {
        this.mapper = mapper;
        this.actorSystem = actorSystem;
    }

    public void sendMessageToActor(Object object) {
        ActorRef manager = ClusterSharding.get(actorSystem).shardRegion(Manager.class.getSimpleName());
        manager.tell(object, ActorRef.noSender());
    }
}
