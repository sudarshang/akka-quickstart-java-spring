package com.example;

import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;

import com.typesafe.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
@Component
public class AkkaActorSystem {

    private static final Logger LOGGER =  LoggerFactory.getLogger(AkkaActorSystem.class);

    @Bean(name = "akkaConf")
    public Config akkaStaticAppConf(){
        Map<String, Object> staticConfigMap = new HashMap<>();
        String remotePort = getRemotePort();
        staticConfigMap.put("akka.remote.artery.canonical.port", remotePort);
        staticConfigMap.put("akka.management.http.port", getManagementPort());
        LOGGER.info("Akka Cloud config map: {}", staticConfigMap);
        return ConfigFactory.parseMap(staticConfigMap).withFallback(ConfigFactory.load());
    }

    @Bean
    public ActorSystem actorSystem(@Qualifier("akkaConf") Config appConfig, ApplicationContext applicationContext,
                                   ShardActorResolver shardResolver) {

        LOGGER.info("Creating actor system with Akka Conf: {}", appConfig);
        ActorSystem system = ActorSystem.create(getActorSystemName(), appConfig);
        AkkaManagement.get(system).start();

        ClusterBootstrap.get(system).start();
        ClusterShardingSettings clusterSettings = ClusterShardingSettings.create(system);
        ClusterSharding.get(system).start(Manager.class.getSimpleName(),
                Props.create(Manager.class, applicationContext),clusterSettings, shardResolver);
        return system;
    }

    private String getRemotePort(){
        return "9551";
    }

    private String getActorSystemName(){
        return "test-actor-system";
    }

    private String getManagementPort(){
        return "9558";
    }

}
