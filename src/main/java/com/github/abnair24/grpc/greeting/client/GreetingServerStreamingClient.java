package com.github.abnair24.grpc.greeting.client;

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingServerStreamingClient {

    public static void main(String[] args) {

        System.out.println("Starting client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                //SSL deactivate for dev setup`
                .usePlaintext()
                .build();

        GreetServiceGrpc.GreetServiceBlockingStub client = GreetServiceGrpc.newBlockingStub(channel);

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("F_name")
                .setLastName("L_name")
                .build();

        GreetManyTimesRequest request = GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        client.greetManyTimes(request)
                .forEachRemaining(greetManyTimesResponse -> {
                    System.out.println(greetManyTimesResponse.getResult());
                });

        System.out.println("Shutting down");
        channel.shutdown();

    }
}
