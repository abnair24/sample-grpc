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
            for (int i = 0; i <= 10; i++) {
                String result = "Hello " + firstName + " - " + i;

                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result)
                        .build();

                responseObserver.onNext(response);
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
    }
    /*
    Returns streamobserver as its called async
     */

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> requestObserver = new StreamObserver<LongGreetRequest>() {

            String result = "";
            @Override
            public void onNext(LongGreetRequest value) {
                result += "Hello " + value.getGreeting().getFirstName();
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

                //return response when client is done

                responseObserver
                        .onNext(LongGreetResponse
                                .newBuilder()
                                .setResult(result)
                                .build()
                        );

                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }
}
