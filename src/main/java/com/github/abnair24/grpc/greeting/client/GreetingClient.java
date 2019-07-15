package com.github.abnair24.grpc.greeting.client;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import com.proto.sample.SampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {

    public static void main(String[] args) {
        System.out.println("Starting client ");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",50051)
                //SSL deactivate for dev setup`
                .usePlaintext()
                .build();

        //SampleServiceGrpc.SampleServiceBlockingStub client = SampleServiceGrpc.newBlockingStub(channel);

       // SampleServiceGrpc.SampleServiceFutureStub asyncClient = SampleServiceGrpc.newFutureStub(channel);

        /*
        Create client
         */
        GreetServiceGrpc.GreetServiceBlockingStub client = GreetServiceGrpc.newBlockingStub(channel);

        /*
        Request creation
         */
        Greeting greeting = Greeting.newBuilder()
                .setFirstName("F_name")
                .setLastName("L_name")
                .build();

        GreetRequest request = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        /*
        Response
         */
        GreetResponse response = client.greeter(request);

        System.out.println(response.getResult());


        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
