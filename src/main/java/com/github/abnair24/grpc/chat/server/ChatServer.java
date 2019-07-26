package com.github.abnair24.grpc.chat.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ChatServer {
  public static void main(String[] args) throws InterruptedException, IOException {
    Server server = ServerBuilder.forPort(50050)
            .addService(new ChatServiceImpl())
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
