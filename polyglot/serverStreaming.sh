#!/usr/bin/env bash

request_params_hash='{
"number": 60
}'

echo $request_params_hash | java -jar ./polyglot.jar \
  --proto_discovery_root=../src/main/proto \
  call \
  --endpoint=localhost:50050 \
  --full_method=calculator.CalculatorService/PrimeNumberDecomposition