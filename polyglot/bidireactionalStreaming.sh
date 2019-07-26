#!/usr/bin/env bash


echo '{"from": "Aswathy","message": "Hello"} \n\n {"from": "Amar","message": "Hello"}' | java -jar ./polyglot.jar \
  --proto_discovery_root=../src/main/proto \
  call \
  --endpoint=localhost:50050 \
  --full_method=chat.ChatService/chat