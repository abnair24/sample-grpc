package com.github.abnair24.grpc.calculator.stubs;


import com.proto.calculator.CalculatorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalcServiceConnection {

    private ManagedChannel managedChannel;
    private CalculatorServiceGrpc.CalculatorServiceStub calcStub;

    public CalcServiceConnection() {
        managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 50050)
                .usePlaintext()
                .build();
        calcStub = CalculatorServiceGrpc.newStub(managedChannel);
    }

    public  CalculatorServiceGrpc.CalculatorServiceStub getConnection() {
        return calcStub;
    }

    private void close() {
        System.out.println("Closing down channel");
        managedChannel.shutdown();
    }

    public void shutdown() {
        if (managedChannel.isShutdown()) {
            System.out.println("Channel shutdown");
        } else {
            close();
        }
    }
}
