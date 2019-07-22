package com.github.abnair24.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        log.info("Starting server");

        Server server = ServerBuilder.forPort(50051)
                .addService(new GreetServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(()-> {
            System.out.println("Received shutdown request");
            server.shutdown();
            System.out.println("Stopped server!!");
        }));

        server.awaitTermination();
    }
}
