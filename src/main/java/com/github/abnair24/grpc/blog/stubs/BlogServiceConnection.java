package com.github.abnair24.grpc.blog.stubs;

import com.proto.blog.BlogServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlogServiceConnection {

    private ManagedChannel managedChannel;
    private BlogServiceGrpc.BlogServiceBlockingStub blockingStub;

    public BlogServiceConnection() {
        log.info("Creating stub");

        managedChannel = ManagedChannelBuilder.forAddress("localhost", 50055)
                .usePlaintext()
                .build();
        blockingStub = BlogServiceGrpc.newBlockingStub(managedChannel);
    }

    public BlogServiceGrpc.BlogServiceBlockingStub getConnection() {
        return blockingStub;
    }

    public void close() {
        managedChannel.shutdown();
    }
}
