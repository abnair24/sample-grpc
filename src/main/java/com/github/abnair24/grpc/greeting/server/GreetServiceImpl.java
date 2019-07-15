package com.github.abnair24.grpc.greeting.server;

import com.proto.greet.Greet.*;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greeter(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        String first_name = greeting.getFirstName();

        String result = "Hello "+ first_name;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        //send response
        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }
}
