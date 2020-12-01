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

### 세 번째, 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

반환할 객체의 클래스를 자유롭게 선택하는 유연함이 있다.

API를 만들 때, 구현 클래스를 공개하지 않고도 그 객체를 반환할 수 있어 API를 작게 유지할 수 있다.

예를 들어, `java.util.Collections`는 인터페이스를 정적 팩터리 메서드의 반환 타입으로 사용하는 대표적인 예이다.

`java.util.Collections`는 45개 클래스를 공개하지 않기 때문에 API 외견을 훨씬 작게 만들었고,

이를 통해서 개념적인 무게(conceptual weight)를 줄였다.

> 개념적인 무게(conceptual weight)란?
>
> 프로그래머가 API를 사용하기 위해 익혀야 하는 개념의 수와 난이
>
> 참고링크 : https://stackoverflow.com/questions/3599008/what-is-power-to-weight-ratio-of-an-api

자바 8부터 인터페이스에 public static 메소드를 추가할 수 있게 되었지만 private static 메소드는 자바 9부터 추가할 수 있다.

### 네 번째, 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다.

EnumSet 클래스는 오직 정적 팩터리만 제공하는데,

원소의 수에 따라 64개 이하면 RegularEnumSet의 인스턴스를, 65개 이상이면 JumboEnumSet의 인스턴스를 반환한다.

```java
public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) {
    Enum<?>[] universe = getUniverse(elementType);
    if (universe == null)
        throw new ClassCastException(elementType + " not an enum");

    if (universe.length <= 64)
        return new RegularEnumSet<>(elementType, universe);
    else
        return new JumboEnumSet<>(elementType, universe);
}
```

### 다섯 번째, 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

이런 유연함은 서비스 제공자 프레임워크(service provider framework)를 만드는 근간이 된다. 대표적으로 `JDBC`가 있다.

서비스 제공자 프레임워크의 제공자(provider)는 서비스의 구현체이다.

서비스 제공자 프레임워크는 다음 3개의 핵심 컴포넌트로 이뤄진다.

1. 구현체의 동작을 정의하는 `서비스 인터페이스`(service interface)
2. 제공자가 구현체를 등록할 때 사용하는 `제공자 등록 API`(provider registration API)
3. 클라이언트가 서비스의 인스턴스를 얻을 때 사용하는 `서비스 접근 API`(service access API)

종종 서비스 제공자 인터페이스(service provider interface) 라는 네 번째 컴포넌트가 쓰이기도 한다.

`JDBC`의 경우에는

1. `Connection`이 `서비스 인터페이스`
2. `DriverManager.registerDriver`가 `제공자 등록 API`
3. `DriverManager.getConnection`이 `서비스 접근 API`
4. `Driver`가 `서비스 제공자 인터페이스`

역할을 수행한다.

## 정적 팩터리 메서드가 생성자보다 안좋은 단점

### 첫 번째, 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만 제공하면 하위 클래스를 만들 수 없다.

Collections 프레임워크에서 제공하는 유틸리티 구현 클래스들(`java.util.Collections`)은 상속할 수 없다.

이러한 제약은 상속 대신 컴포지션의 사용을 유도하고, 불변타입을 만들려면 제약을 지켜야 한다는 점에서 장점이라 말할지도 모르겠다.

### 두 번째, 정적 팩터리 메서드는 프로그래머가 찾기 힘들다.

생성자는 Javadoc 상단에 모아서 보여주지만 static 팩토리 메소드는 API 문서에서 특별히 다뤄주지 않는다.

따라서 클래스나 인터페이스 문서 상단에 정적 팩토리 메서드에 대한 문서를 제공하는 것이 좋겠다.

## 핵심 정리

장단점을 이해하고 사용하되, 정적 팩터리 메서드를 사용하는게 장점이 더 많으므로

무작정 public 생성자를 제공하던 습관이 있으면 고치자.
