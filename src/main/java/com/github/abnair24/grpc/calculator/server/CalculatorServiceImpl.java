package com.github.abnair24.grpc.calculator.server;

import com.proto.calculator.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        SumResponse sumResponse = SumResponse.newBuilder()
                .setSumResult(request.getFirstNumber() + request.getSecondNumber())
                .build();

        responseObserver.onNext(sumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void primeNumberDecomposition(PrimeNumberDecompositionRequest request,
        StreamObserver<PrimeNumberDecompositionResponse> responseObserver) {
            Integer number = request.getNumber();

            for(int i = 2; i< number; i++) {
                while(number%i == 0) {
                    responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                            .setPrimeFactor(i)
                            .build());
                    number = number/i;
                }
            }

            if(number >2) {
                responseObserver.onNext(PrimeNumberDecompositionResponse.newBuilder()
                        .setPrimeFactor(number)
                        .build());
            }


            responseObserver.onCompleted();
        }

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        StreamObserver<ComputeAverageRequest> requestObserver = new StreamObserver<ComputeAverageRequest>() {
            int sum = 0;
            int count = 0;

            @Override
            public void onNext(ComputeAverageRequest value) {
                sum += value.getNumber();
                count += 1;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                // compute average
                double average = (double) sum/count;
                responseObserver.onNext(
                        ComputeAverageResponse.newBuilder()
                                .setAverage(average)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public StreamObserver<FindMaximumRequest> findMaximum(StreamObserver<FindMaximumResponse> responseObserver) {
        return new StreamObserver<FindMaximumRequest>() {
            int currentMaximum = 0;
            @Override
            public void onNext(FindMaximumRequest value) {
                int currentNumber = value.getNumber();
                if(currentNumber > currentMaximum ){
                    currentMaximum = currentNumber;
                    responseObserver.onNext(
                            FindMaximumResponse.newBuilder()
                                    .setMaximum(currentMaximum)
                                    .build()
                    );
                }
                else{
                    //nothing
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                // send the current last maximum
                responseObserver.onNext(
                        FindMaximumResponse.newBuilder()
                                .setMaximum(currentMaximum)
                                .build()
                );
                // the server is done sending data
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {
        Integer number = request.getNumber();
        if(number > 0){
            double numberRoot = Math.sqrt(number);
            responseObserver.onNext(
                    SquareRootResponse.newBuilder()
                            .setNumberRoot(numberRoot).build()
            );
            responseObserver.onCompleted();
        } else{
            // we construct the exception
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("The number being sent is not positive")
                            .augmentDescription("Number sent : " + number)
                            .asRuntimeException()
            );
        }
    }
}
