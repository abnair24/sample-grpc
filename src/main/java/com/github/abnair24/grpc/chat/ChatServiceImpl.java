
package com.github.abnair24.grpc.chat;

import com.google.protobuf.Timestamp;
import com.proto.chat.ChatMessage;
import com.proto.chat.ChatMessageFromServer;
import com.proto.chat.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    private static Set<StreamObserver<ChatMessageFromServer>> observers = ConcurrentHashMap.newKeySet();

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);

        return new StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage value) {
                System.out.println(value);
                ChatMessageFromServer message = ChatMessageFromServer.newBuilder()
                        .setMessage(value)
                        .setTimestamp(Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000))
                        .build();

                for (StreamObserver<ChatMessageFromServer> observer : observers) {
                    observer.onNext(message);
                }
            }

            @Override
            public void onError(Throwable t) {
                // do something;
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }
}
