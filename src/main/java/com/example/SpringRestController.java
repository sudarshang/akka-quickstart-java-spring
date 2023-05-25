package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class SpringRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(SpringRestController.class);

    @Autowired
    MessageToActor messageToActor;


    private ActorSystem system;
    private ActorRef manager;

    @GetMapping("/command")
    public String commandEndpoint(@RequestParam(value = "exec", defaultValue = "work") String exec) {
        LOGGER.info("Received request to execute work: " + exec);
        messageToActor.sendMessageToActor(exec);
        return "ok";
    }
}
