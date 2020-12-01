package com.example.demoeffectivejava.item01;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.Random;

public class Foo {
    public static void main(String[] args) {
        BigInteger primeByConstructor = new BigInteger(3, 100, new Random());
        System.out.println(primeByConstructor);
        BigInteger primeByStaticFactoryMethod = BigInteger.probablePrime(3, new Random());
        System.out.println(primeByStaticFactoryMethod);

        EnumSet<FooRegularEnum> regularEnumSet = EnumSet.noneOf(FooRegularEnum.class);
        System.out.println(regularEnumSet.getClass());      // class java.util.RegularEnumSet
        EnumSet<FooJumboEnum> jumboEnumSet = EnumSet.noneOf(FooJumboEnum.class);
        System.out.println(jumboEnumSet.getClass());        // class java.util.JumboEnumSet
    }
}

enum FooRegularEnum {
    FOO1, FOO2, FOO3, FOO4, FOO5, FOO6, FOO7
}

enum FooJumboEnum {
    FOO1, FOO2, FOO3, FOO4, FOO5, FOO6, FOO7, FOO8, FOO9, FOO10,
    FOO11, FOO12, FOO13, FOO14, FOO15, FOO16, FOO17, FOO18, FOO19, FOO20,
    FOO21, FOO22, FOO23, FOO24, FOO25, FOO26, FOO27, FOO28, FOO29, FOO30,
    FOO31, FOO32, FOO33, FOO34, FOO35, FOO36, FOO37, FOO38, FOO39, FOO40,
    FOO41, FOO42, FOO43, FOO44, FOO45, FOO46, FOO47, FOO48, FOO49, FOO50,
    FOO51, FOO52, FOO53, FOO54, FOO55, FOO56, FOO57, FOO58, FOO59, FOO60,
    FOO61, FOO62, FOO63, FOO64, FOO65, FOO66, FOO67, FOO68, FOO69, FOO70
}
