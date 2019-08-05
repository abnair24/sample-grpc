package com.github.abnair24.grpc.calculator.client;

import com.github.abnair24.grpc.calculator.stubs.CalcServiceConnection;
import com.proto.calculator.CalculatorServiceGrpc;
import com.proto.calculator.ComputeAverageRequest;
import com.proto.calculator.ComputeAverageResponse;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

@Getter
@Setter
public class CalculatorClient {

    private CalcServiceConnection calcServiceConnection;
    private CalculatorServiceGrpc.CalculatorServiceStub stub;
    private double average;

    public CalculatorClient() {
        calcServiceConnection = new CalcServiceConnection();
        stub = calcServiceConnection.getConnection();
    }

    public void computeAverage(Iterator<ComputeAverageRequest> computeAverageRequestIterator) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestStreamObserver =
                stub.computeAverage(new StreamObserver<ComputeAverageResponse>() {
                    @Override
                    public void onNext(ComputeAverageResponse value) {
                       average = value.getAverage();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });

        for (int i = 0; i < 1000; i++) {
            requestStreamObserver.onNext(ComputeAverageRequest.newBuilder()
                    .setNumber(i)
                    .build());
        }

        requestStreamObserver.onCompleted();
    }

}
