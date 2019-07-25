#!/usr/bin/env bash

echo '{"number": 40} \n\n {"number": 50} \n\n {"number": 10}' | java -jar ./polyglot.jar \
  --proto_discovery_root=../src/main/proto \
  call \
  --endpoint=localhost:50050 \
  --full_method=calculator.CalculatorService/ComputeAverage