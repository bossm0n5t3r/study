# 아이템 1. 생성자 대신 정적 팩터리 메서드를 고려하라

객체의 인스턴스를 얻는 수단은?
-> public 생정자
-> 그리고?

정적 팩터리 메서드(static factory method)를 사용해서 만드는 방법!

```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```

이 방법에는 장점과 단점이 있다.

## 정적 팩터리 메서드가 생성자보다 좋은 장점

### 첫 번째, 이름을 가질 수 있다.

생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하지 못한다.

정적 팩터리는 이름만 잘 지으면 반환될 객체의 특성을 쉽게 묘사할 수 있다.

```java
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
```

```java
public BigInteger(int bitLength, int certainty, Random rnd) {
    BigInteger prime;

    if (bitLength < 2)
        throw new ArithmeticException("bitLength < 2");
    prime = (bitLength < SMALL_PRIME_THRESHOLD
                            ? smallPrime(bitLength, certainty, rnd)
                            : largePrime(bitLength, certainty, rnd));
    signum = 1;
    mag = prime.mag;
}
```

```java
public static BigInteger probablePrime(int bitLength, Random rnd) {
    if (bitLength < 2)
        throw new ArithmeticException("bitLength < 2");

    return (bitLength < SMALL_PRIME_THRESHOLD ?
            smallPrime(bitLength, DEFAULT_PRIME_CERTAINTY, rnd) :
            largePrime(bitLength, DEFAULT_PRIME_CERTAINTY, rnd));
}
```

하나의 시그니처로는 생성자를 하나만 만들 수 있다.

입력 매개변수들의 순서를 다르게 한 생성자를 추가하는 식으로 이 제한을 피해볼 수도 있지만, 좋지 않은 발상이다.

생성자가 어떠한 역할을 하는지 정확히 기억하기 어려워 엉뚱한 것을 호출하는 실수를 할 수 있다.

이름을 가질 수 있는 정적 팩터리 메서드에는 이런 제약이 없다.

한 클래스에 시그니처가 같은 생성자가 여러 개 필요 할 것 같으면,

생성자를 정적 팩터리 메서드로 바꾸고 각각의 차이를 잘 드러내는 이름을 지어주자.

### 두 번째, 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.

불변 클래스(immutable class)는 인스턴스를 미리 만들어 놓거나 새로 생성한 인스턴스를 캐싱하여 재활용하는 식으로

불필요한 객체 생성을 피할 수 있다.

플라이웨이트 패턴(Flyweight pattern)도 이와 비슷한 기법이라고 할 수 있다.

> 플라이웨이트 패턴
>
> '공유(Sharing)'을 통하여 대량의 객체들을 효과적으로 지원하는 방법
>
> 참고링크 : https://readystory.tistory.com/137

> 인스턴스 통제(instance-controlled) 클래스
>
> 반복되는 요청에 같은 객체를 반환하는 식으로 언제 어느 인스턴스를 살아 있게 할지를 철저히 통제하는 클래스<br>
> 인스턴스를 통제하면 클래스를 싱글턴(singleton)으로 만들 수도,<br>
> 인스턴스화 불가(noninstantiable)로 만들 수도 있다.<br>
> 또한 불변 값 클래스에서 동치인 인스턴스가 단 하나 뿐임을 보장할 수 있다.<br>
> 인스턴스 통제는 플라이웨이트 패턴의 근간이 되며, 열거 타입은 인스턴스가 하나만 만들어짐을 보장한다.<br>
