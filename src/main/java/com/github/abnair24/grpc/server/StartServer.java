package com.github.abnair24.grpc.server;

import com.github.abnair24.grpc.blog.server.BlogServiceImpl;
import com.github.abnair24.grpc.clientStreaming.CalculatorServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class StartServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(50050)
                .addService(new com.example.grpc.chat.ChatServiceImpl())
                .addService(new BlogServiceImpl())
                .addService(new CalculatorServiceImpl())
                .build();

        server.start();
        server.awaitTermination();
    }
}