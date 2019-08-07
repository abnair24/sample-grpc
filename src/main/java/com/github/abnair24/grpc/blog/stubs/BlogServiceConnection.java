package com.github.abnair24.grpc.blog.stubs;

import com.proto.blog.BlogServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogServiceConnection {

    private ManagedChannel managedChannel;
    private BlogServiceGrpc.BlogServiceBlockingStub blockingStub;

    public BlogServiceConnection() {
        System.out.println("Creating stub");

        managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 50050)
                .usePlaintext()
                .build();



        blockingStub = BlogServiceGrpc.newBlockingStub(managedChannel);
    }

    public BlogServiceGrpc.BlogServiceBlockingStub getConnection() {
        return blockingStub;
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
