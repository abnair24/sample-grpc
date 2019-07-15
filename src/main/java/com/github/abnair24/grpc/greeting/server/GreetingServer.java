package com.github.abnair24.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class GreetingServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting server");

        Server server = ServerBuilder.forPort(50052)
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
