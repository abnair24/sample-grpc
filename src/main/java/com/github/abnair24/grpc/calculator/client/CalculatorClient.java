package com.github.abnair24.grpc.calculator.client;

import com.github.abnair24.grpc.calculator.stubs.CalcServiceConnection;
import com.proto.calculator.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;

@Getter
@Setter
public class CalculatorClient {

    private CalcServiceConnection calcServiceConnection;
    private CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub;
    private CalculatorServiceGrpc.CalculatorServiceStub nonBlockingStub;

    private double average;

    public CalculatorClient() {
        calcServiceConnection = new CalcServiceConnection();
        blockingStub = calcServiceConnection.getBlockingConnection();
        nonBlockingStub = calcServiceConnection.getNonBlockingConnection();
    }

//    public void computeAverage(Iterator<ComputeAverageRequest> computeAverageRequestIterator) {
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        StreamObserver<ComputeAverageRequest> requestStreamObserver =
//                nonBlockingStub.computeAverage(new StreamObserver<ComputeAverageResponse>() {
//                    @Override
//                    public void onNext(ComputeAverageResponse value) {
//                       average = value.getAverage();
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//                });
//
//        for (int i = 0; i < 1000; i++) {
//            requestStreamObserver.onNext(ComputeAverageRequest.newBuilder()
//                    .setNumber(i)
//                    .build());
//        }
//
//        requestStreamObserver.onCompleted();
//    }

    public SumResponse sum(SumRequest sumRequest){
        return  blockingStub.sum(sumRequest);
    }

    public Iterator<PrimeNumberDecompositionResponse> primeNumberDecomposition(PrimeNumberDecompositionRequest primeNumberDecompositionRequest){

        Iterator<PrimeNumberDecompositionResponse> primeNumberDecomposition = blockingStub.primeNumberDecomposition(primeNumberDecompositionRequest);

        return primeNumberDecomposition;
    }

}
