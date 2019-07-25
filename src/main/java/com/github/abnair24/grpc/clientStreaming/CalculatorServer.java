package com.github.abnair24.grpc.clientStreaming;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50052)
                .addService(new CalculatorServiceImpl())
//                .addService(ProtoReflectionService.newInstance()) // for reflection
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
