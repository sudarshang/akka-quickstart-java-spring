package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class SpringRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(SpringRestController.class);

    private ActorSystem system;
    private ActorRef manager;

    @GetMapping("/command")
    public String commandEndpoint(@RequestParam(value = "exec", defaultValue = "work") String exec) {
        LOGGER.info("Received request to execute work: " + exec);
        manager.tell(exec, null);
        return "ok";
    }

    @PostConstruct
    private void init(){
        system = ActorSystem.create("SimpleActorSystem");
        manager = system.actorOf(Props.create(Manager.class), "manager");
    }

}
