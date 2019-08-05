package com.github.abnair24.grpc.blog;

import com.github.abnair24.grpc.calculator.client.CalculatorClient;
import com.proto.calculator.PrimeNumberDecompositionRequest;
import com.proto.calculator.PrimeNumberDecompositionResponse;
import com.proto.calculator.SumRequest;
import com.proto.calculator.SumResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public class CalculatorTest {

    @Test
    public  void sumTest(){
        CalculatorClient calculatorClient=new CalculatorClient();

        SumRequest request= SumRequest.newBuilder()
                .setFirstNumber(10)
                .setSecondNumber(20)
                .build();

        SumResponse response = calculatorClient.sum(request);
        Assert.assertEquals(response.getSumResult(),30);
    }

    @Test
    public void primeDecompositionTest(){
        CalculatorClient calculatorClient=new CalculatorClient();

        PrimeNumberDecompositionRequest request= PrimeNumberDecompositionRequest
                .newBuilder()
                .setNumber(60)
                .build();

        Iterator<PrimeNumberDecompositionResponse> response = calculatorClient.primeNumberDecomposition(request);

        response.forEachRemaining(
                result -> System.out.println(result.toString())
        );
    }
}
