package com.example;

import akka.actor.AbstractActorWithTimers;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

public class Worker extends AbstractActorWithTimers {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final Logger LOGGER = LoggerFactory.getLogger(Worker.class);

    private int counter = 0;

    public static class McBomb {
        private final String value;

        McBomb() {
            this.value = null;
        }
        McBomb(String value) {
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, s -> {
                if("error".equals(s)){
                    throw new RuntimeException("Error completing task..");
                }
                else {
                    counter++;
                    System.out.println("Number of completed tasks " + counter);
                }
            })
            .match(McBomb.class, b -> {
                System.out.println("Intentional NPE test...");
                int length = b.getValue().length();
            })
            .matchAny(m -> {
                System.out.println("Unexpected message received: " + m);
                unhandled(m);
            })
            .build();
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) {
        System.out.println(self().path().name() + " preRestart called...");
        log.error("Akka Logged from preRestart: " + reason.getMessage());
        LOGGER.error("SLF4j Logged from preRestart: " + reason.getMessage());
    }

    @Override
    public void postRestart(Throwable reason) {
        System.out.println(self().path().name() + " has restarted");
    }

    @Override
    public void preStart() {
        System.out.println(self().path().name() + " is starting");
    }

    @Override
    public void postStop() {
        System.out.println("Stopped");
    }

}
