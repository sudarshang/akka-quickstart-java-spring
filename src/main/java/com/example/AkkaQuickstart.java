package com.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.IOException;

public class AkkaQuickstart {

  public static void main(String[] args) {

    ActorSystem system = ActorSystem.create("SimpleActorSystem");

    ActorRef manager = system.actorOf(Props.create(Manager.class), "manager");

    System.out.println("Sending test sequence");
    manager.tell("work", null);
    manager.tell("work", null);
    manager.tell("work", null);
    manager.tell("bomb", null);
    manager.tell("work", null);
    manager.tell("work", null);
    manager.tell("work", null);

    try {
      System.out.println(">>> Press enter to exit <<<");
      System.in.read();
    }
    catch (IOException e) {
      //ignored...
    }
    finally {
      system.terminate();
    }

  }

}
