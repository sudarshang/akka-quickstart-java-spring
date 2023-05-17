package com.example;

import akka.actor.ActorRef;
import akka.actor.Kill;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.Terminated;
import akka.actor.AbstractActor;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;

public class Manager extends AbstractActor {

    private final SupervisorStrategy supervisorStrategy;
    private final ActorRef worker;

    public Manager(){

        worker = context().actorOf(Props.create(Worker.class), "worker");

        context().watch(worker);

        this.supervisorStrategy = new OneForOneStrategy(
            3,
            Duration.ofSeconds(5),
            DeciderBuilder
                .match(NullPointerException.class, e -> SupervisorStrategy.restart())
                .match(RuntimeException.class, e -> SupervisorStrategy.restart())
                .build());

    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return this.supervisorStrategy;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, s -> {
                if ("work".equals(s)) {
                    System.out.println("Adding work request...");
                    worker.tell("request", self());
                }
                else if ("throw".equals(s)) {
                    System.out.println("Forcing the worker to mess up...");
                    worker.tell("error", self());
                }
                else if ("bomb".equals(s)) {
                    System.out.println("Forcing the worker to blow up...");
                    worker.tell(new Worker.McBomb(), self());
                }
                else if ("kill".equals(s)) {
                    System.out.println("Forcing the worker to stop current tasks...");
                    worker.tell(Kill.getInstance(), self());
                }
            })
            .match(Terminated.class, t -> {
                System.out.println("Matched Terminated...");
                System.out.println(t.getActor().path().name());
            })
            .matchAny(m -> {
                System.out.println("Unrecognized message...");
                unhandled(m);
            })
            .build();
    }


}
