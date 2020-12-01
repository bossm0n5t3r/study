# 아이템 3. private 생성자나 열거 타입으로 싱글턴임을 보증하라

**싱글턴(singleton)** 이란?

**인스턴스를 오직 하나만 생성할 수 있는 클래스**를 가리킨다.

> 싱글턴의 전형적인 예
>
> 함수와 같은 무상태(stateless) 객체<br>
> 설계상 유일해야 하는 시스템 컴포넌트 등

하지만, **싱글턴을 사용하는 클라이언트 코드를 테스트하기 어렵다.**

싱글턴이 인터페이스를 구현한게 아니라면, mock으로 대체할 수 없기 때문이다.

싱글턴을 만드는 방식은 두 가지 방식이 있는데, 공통적으로
**생성자는 private으로 감춰두고, public static 멤버를 사용해서 유일한 인스턴스를 제공**한다

## public static 멤버가 final 필드

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() { }
}
```

리플랙션 API인 `AccessibleObject.setAccessible`을 사용해 private 생성자를 호출하는 경우를 제외하면

private 생성자는 public static final 필드인 **Elvis.INSTANCE**를 초기화할 때 딱 한 번만 호출된다.

### 장점

해당 클래스가 싱글턴임이 API에 명백히 드러나고 간결하다.

## public static 멤버가 정적 팩터리 메서드

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    private Elvis() { }
    public static Elvis getInstance() { return INSTANCE; }
}
```

### 장점

1. API를 바꾸지 않고도 `싱글턴이 아니게 변경할 수 있다.`

    - 싱글턴으로 쓰다가 쓰레드별로 다른 인스턴스를 넘겨주도록 할 수 있다.

2. 원한다는 정적 팩터리를 `제네릭 싱글턴 팩터리`로 만들 수 있다.

3. 정적 팩터리의 메서드 참조를 `공급자(supplier)로 사용할 수 있다는 점`이다.

    - `Elvis::getInstance`를 `Supplier<Elvis>`로 사용하는 방식

### 직렬화(Serialization)

위의 두 가지 방법 모두, 직렬화된 인스턴스를 `역직렬화할 때마다 새로운 인스턴스`가 생기므로

이를 해결하려면 모든 인스턴스 필드를 일시적(transient)이라고 선언하고 다음과 같이 `readResolve` 메서드를 제공해야 한다.

> Java transient이란?
>
> transient는 Serialize하는 과정에 제외하고 싶은 경우 선언하는 키워드

```java
private Object readResolve() {
    return INSTANCE;
}
```

## ENUM

싱글턴을 만드는 세 번째 방법은 원소가 하나인 열거 타입을 선언하는 것이다.

```java
public enum Elvis {
    INSTANCE;
}
```

직렬화/역직렬화 할 때 코딩으로 문제를 해결할 필요도 없고, 리플렉션으로 호출되는 문제도 고민할 필요없는 방법이다.

**대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.**

단, 만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면, 이 방법은 사용할 수 없다.
(열거 타입이 다른 인터페이스를 구현하도록 선언할 수는 있다.)
