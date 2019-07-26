package com.github.abnair24.grpc.server;

import com.github.abnair24.grpc.blog.server.BlogServiceImpl;


import com.github.abnair24.grpc.calculator.server.CalculatorServiceImpl;
import com.github.abnair24.grpc.chat.server.ChatServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(50050)
                .addService(new ChatServiceImpl())
                .addService(new BlogServiceImpl())
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();
        Runtime.getRuntime().addShutdownHook( new Thread ( () -> {
            System.out.println("Received Shutdown Server");
            server.shutdown();
            System.out.println("Succesfully Shutdown Server");
        }));

        server.awaitTermination();
    }
}