# 아이템 43. 람바보다는 메서드 참조를 사용하라

람다가 익명 클래스보다 가장 큰 나은 점은? -> `간결함`

그런데, 자바에서는 함수 객체를 더 간결하게 만드는 방법이 있다 -> 바로 `메서드 참조 (method reference)`

---

```kotlin
// 키가 맵 안에 없다면 키와 숫자 1을 매핑하고, 이미 있다면 기존 매핑 값을 증가시키는 코드
for (s in args) frequencyTable.merge(s, 1) { count: Int, incr: Int -> count + incr }
```

위의 코드는 자바 8 때 Map에 추가된 merge 메서드를 사용했다.

merge 메서드는 키, 값, 함수를 인수로 받으며, 주어진 키가 맵 안에 아직 없다면,

주어진 {키, 값} 쌍을 그대로 저장한다.

반대로 키가 이미 있다면, (세 번째 인수로 받은) 함수를 현재 값과 주어진 값에 적용한 다음,

그 결과로 현재 값을 덮어쓴다.

즉, 맵에 {키, 함수의 결과} 쌍을 저장한다.

```java
default V merge(K key, V value,
        BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    Objects.requireNonNull(remappingFunction);
    Objects.requireNonNull(value);
    V oldValue = get(key);
    V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
    if (newValue == null) {
        remove(key);
    } else {
        put(key, newValue);
    }
    return newValue;
}
```

실제로 코드를 들어가보니 대충 이런 꼴이다...

깔끔해 보이는 코드지만, 사실 이 람다는 두 인수의 합을 단순히 반환할 뿐이다.

자바 8이 되면서 Integer 클래스(와 모든 기본 타입의 박싱 타입)는 이 람다와 기능이 같은 정적 메서드 sum을 제공하기 시작했다.

```kotlin
// Integer::sum
for (s in args) frequencyTable.merge(s, 1, Integer::sum)
```

위의 코드는 같은 결과를 더 보기 좋게 얻을 수 있고,

매개변수 수가 늘어날수록 메서드 참조로 제거할 수 있는 코드 양도 늘어난다.

하지만, 어떤 람다에서는 매개변수의 이름 자체가 프로그래머들에게 좋은 가이드가 되기도 한다.

-> 이런 람다는 길이는 더 길지만, 메서드 참조보다 읽기 쉽고 유지보수도 쉬울 수 있다.

일반적으로 람다로 할 수 없는 일이라면 메서드 참조로도 할 수 없다.
(단 하나의 예외는 아이템 마지막에서 소개하겠다.)

그렇더라도 메서드 참조를 사용하는 편이 보통은 더 짧고 간결하다.
-> 람다로 구현했을 때 너무 길거나 복잡하다면 메서드 참조가 좋은 대안
-> 람다로 작성할 코드를 새로운 메서드에 담은 다음, 람다 대신 그 메서드 참조를 사용하는 식

## 메서드 참조의 유형과 같은 기능을 하는 람다

### 1. 정적

가장 흔한 유형이다.

```kotlin
// 정적 메서드 참조 유형
val referenceToAStaticMethod = { str: String -> Integer.parseInt(str) }
```

### 2. 한정적 (인스턴스) 참조

근본적으로 정적 참조와 비슷하다.

즉, 함수 객체가 받는 인수와 참조되는 메소드가 받는 인수가 똑같다.

```kotlin
// 한정적 (인스턴스) 메서드 참조 유형
val then = Instant.now()
val referenceToAnInstanceMethodOfAParticularObject = { t: Instant -> then.isAfter(t) }
```

### 3. 비한정적 (인스턴스) 참조

함수 객체를 적용하는 시점에 수신 객체를 알려준다.

이를 위해 수신 객체 전달용 매개변수가 매개변수 목록의 첫 번째로 추가되며,

그 뒤로는 참조되는 메서드 선언에 정의된 매개변수들이 뒤따른다.

비한정적 참조는 주로 스트림 파이프라인에서의 매핑과 필터 함수에 쓰인다. (아이템 45)

```kotlin
// 비한정적 (인스턴스) 메서드 참조 유형
val referenceToAnInstanceMethodOfAnArbitraryObjectOfAParticularType = { str: String -> str.toLowerCase() }
```

### 4. 클래스 생성자

```kotlin
// 클래스 생성자를 가리키는 메서드 참조 유형
val referenceToAConstructor = { TreeMap<String, Int>() }
```

### 5. 배열생성자

```kotlin
// 배열 생성자를 가리키는 메서드 참조 유형
val referenceToAArrayConstructor = { len: Int -> IntArray(len) }
```

## 핵심 정리

메서드 참조는 람다의 간단명료한 대안이 될 수 있다.

**메서드 참조 쪽이 짧고 명확하다면 메서드 참조를 쓰고, 그렇지 않을 때만 람다를 사용하자.**

## References

- [Method References](https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html)
