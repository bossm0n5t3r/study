package com.example.demoeffectivejava.item01;

import java.math.BigInteger;
import java.util.Random;

public class Foo {
    public static void main(String[] args) {
        BigInteger primeByConstructor = new BigInteger(3, 100, new Random());
        System.out.println(primeByConstructor);
        BigInteger primeByStaticFactoryMethod = BigInteger.probablePrime(3, new Random());
        System.out.println(primeByStaticFactoryMethod);
    }
}
