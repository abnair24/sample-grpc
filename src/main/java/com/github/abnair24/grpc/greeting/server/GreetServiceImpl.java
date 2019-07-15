package com.github.abnair24.grpc.greeting.server;

import com.proto.greet.*;
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

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {

        String firstName = request.getGreeting().getFirstName();

        try {
            for(int i = 0; i<= 10; i++ ) {
                String result = "Hello " + firstName + " - " + i;

                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();

                responseObserver.onNext(response);
                Thread.sleep(1000);
            }
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }finally {
            responseObserver.onCompleted();
        }

    }
}
