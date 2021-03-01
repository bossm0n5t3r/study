# 아이템 46. 스트림에서는 부작용 없는 함수를 사용하라

- [아이템 46. 스트림에서는 부작용 없는 함수를 사용하라](#아이템-46-스트림에서는-부작용-없는-함수를-사용하라)
  - [스트림 패러다임의 핵심](#스트림-패러다임의-핵심)
  - [텍스트 파일에서 단어별 수를 세어 빈도표를 만드는 코드](#텍스트-파일에서-단어별-수를-세어-빈도표를-만드는-코드)
  - [Collector](#collector)
  - [빈도표에서 가장 흔한 단어 10개를 뽑아내는 스트림 파이프라인 코드](#빈도표에서-가장-흔한-단어-10개를-뽑아내는-스트림-파이프라인-코드)
  - [Collectors의 나머지 36개 메서드들](#collectors의-나머지-36개-메서드들)
  - [핵심 정리](#핵심-정리)

스트림을 처음 봐서는 이해하기 어려울 수 있다.

- 원하는 작업을 스트림 파이프라인으로 표현하는 것조차 어려울지 모르고
- 성공하여 프로그램이 동작하더라도 장점이 무엇인지 쉽게 와 닿지 않을 수도 있다.

왜냐하면, **스트림은 함수형 프로그래밍에 기초한 패러다임**이기 때문에

## 스트림 패러다임의 핵심

스트림 패터다임의 핵심은 계산을 **일련의 변환(transformation)**으로 재구성하는 부분이다.

-> 이때 각 변환 단계는 가능한 이전 단계의 결과를 받아 처리하는 순수함수여야한다.

> 순수함수란?
>
> 오직 입력만이 결과에 영향을 주는 함수 <br>
> 다른 가변 상태를 참조하지 않고, 함수 스스로도 다른 상태를 변경하지 않는다.

([Pure_function](https://en.wikipedia.org/wiki/Pure_function), [Function (mathematics)](<https://en.wikipedia.org/wiki/Function_(mathematics)>))

이렇게 하려면, (중간 단계든 종단 단계든) 스트림 연산에 건네는 함수 객체는 모두 부작용(side effect)이 없어야 한다.

## 텍스트 파일에서 단어별 수를 세어 빈도표를 만드는 코드

```kotlin
val freq = mutableMapOf<String, Long>() as HashMap
try {
    val words = Scanner(wordFile).tokens()
    words.forEach { word ->
        freq.merge(word.toLowerCase(), 1L, Long::plus)
    }
} catch (e: Exception) {
    e.printStackTrace()
}
```

<details>
<summary>위의 코드에서 무엇이 문제인지 보이는가?</summary>
<p>
스트림, 람다, 메서드 참조를 사용했고, 결과도 올바지만,

스트림 코드를 가장한 반복적 코드이기 때문에 절대 스트림 코드라 할 수 없다.

</p>
</details>

스트림 API의 이점을 살리지 못하여 같은 기능의 반복적 코드보다 (조금 더)

- 길고,
- 읽기 어렵고,
- 유지보수에도 좋지 않다.

이 코드의 모든 작업이 종단 연산인 `forEach`에서 일어나는데,

이때 외부 상태(빈도표)를 수정하는 람다를 실행하면서 문제가 생긴다.

`forEach`가 그저 스트림이 수행한 연산 결과를 보여주는 일 이상을 하는 것을 보니

나쁜 코드(?)일 것 같은 냄새(?)가 난다.

이제 올바르게 작성한 코드를 보자.

```kotlin
var freq : Map<String, Long>? = null
try {
    val words = Scanner(wordFile).tokens()
    freq = words
            .collect(groupingBy(String::toLowerCase, counting()))
} catch (e: Exception) {
    e.printStackTrace()
}
```

앞선 코드와 같은 일을 했지만, 스트림을 제대로 썼다. 뿐만 아니라 짧고 명확하다.

`forEach`연산은 대놓고 반복적이며 병렬화할 수 도 없기 때문에, **스트림 계산 결과를 보고할 때만 사용하고, 계산하는 데는 쓰지 말자.**

위 코드는 수집기(collector)를 사용하는데, 스트림을 사용하려면 반드시 배워야 하는 새로운 개념이다.

## Collector

`java.util.stream.Collectors`클래스는 메서드를 무려 39개(자바 10에서는 43개)나 가지고 있고,

그 중에는 타입 매개변수가 5개나 되는 것도 있다.

```java
public static <T, K, D, A, M extends Map<K, D>>
    Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> classifier,
                                  Supplier<M> mapFactory,
                                  Collector<? super T, A, D> downstream) {
        Supplier<A> downstreamSupplier = downstream.supplier();
        BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        BiConsumer<Map<K, A>, T> accumulator = (m, t) -> {
            K key = Objects.requireNonNull(classifier.apply(t), "element cannot be mapped to a null key");
            A container = m.computeIfAbsent(key, k -> downstreamSupplier.get());
            downstreamAccumulator.accept(container, t);
        };
        BinaryOperator<Map<K, A>> merger = Collectors.<K, A, Map<K, A>>mapMerger(downstream.combiner());
        @SuppressWarnings("unchecked")
        Supplier<Map<K, A>> mangledFactory = (Supplier<Map<K, A>>) mapFactory;

        if (downstream.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl<>(mangledFactory, accumulator, merger, CH_ID);
        }
        else {
            @SuppressWarnings("unchecked")
            Function<A, A> downstreamFinisher = (Function<A, A>) downstream.finisher();
            Function<Map<K, A>, M> finisher = intermediate -> {
                intermediate.replaceAll((k, v) -> downstreamFinisher.apply(v));
                @SuppressWarnings("unchecked")
                M castResult = (M) intermediate;
                return castResult;
            };
            return new CollectorImpl<>(mangledFactory, accumulator, merger, finisher, CH_NOID);
        }
    }

// ...
// Type parameters:
// <T> – the type of the input elements
// <K> – the type of the keys
// <D> – the result type of the downstream reduction
// <A> – the intermediate accumulation type of the downstream collector
// <M> – the type of the resulting Map
// ...

```

다행히 복잡한 세부 내용은 몰라도, 이 API의 장점을 대부분 활용할 수 있다.

그러니, 캡슐화한 블랙박스라 생각하면 편하다.

수집기가 생성하는 객체는 일반적으로 컬렉션이며, 그래서 "collector"라는 이름을 쓴다.

수집기를 사용하면 스트림의 원소를 손쉽게 컬렉션으로 모을 수 있다.

수집기는 총 세 가지로, `toList()`, `toSet()`, `toCollection(collectionFactory)`가 그 주인공이다.

이들은 차례로 리스트, 집합, 프로그래머가 지정한 컬렉션 타입을 반환한다.

## 빈도표에서 가장 흔한 단어 10개를 뽑아내는 스트림 파이프라인 코드

```kotlin
private fun topTen(freq: Map<String, Long>): List<String> {
    return freq.keys.stream()
            .sorted(comparing(freq::getValue).reversed())
            .limit(10)
            .toList()
}
```

마지막의 `.toList()`는 kotlin `Streams.kt` 안의 함수이며, 같은 기능을 제공한다.

```kotlin
@SinceKotlin("1.2")
public fun <T> Stream<T>.toList(): List<T> = collect(Collectors.toList<T>())
```

위 코드에서 어려운 부분은 sorted에 넘긴 비교자, 즉 `comparing(freq::getValue).reversed()`뿐이다.

comparing 메서드는 키 추출 함수를 받는 비교자 생성 메서드이다.

```java
public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
            Function<? super T, ? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
            (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }
```

여기서 키 추출 함수로 쓰인 `freq::getValue`(자바에서는 `freq::get`)은 입력받은 단어(키)를 빈도표에서 찾아(추출)

그 빈도를 반환한다.

```kotlin
// Returns the value for the given key or throws an exception if there is no such key in the map.
// If the map was created by withDefault, resorts to its defaultValue provider function instead of throwing an exception.
// Throws:
// NoSuchElementException - when the map doesn't contain a value for the specified key and no implicit default value was provided for that map.

@SinceKotlin("1.1")
public fun <K, V> Map<K, V>.getValue(key: K): V = getOrImplicitDefault(key)
```

그 다음 가장 흔한 단어가 위로 오도록 비교자(comparing)를 역순(reversed)으로 정렬한다(sorted).

## Collectors의 나머지 36개 메서드들

_여기서부터는 진짜로 [https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Collectors.html](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/stream/Collectors.html)의 문서를 따라가면서 간단히 정리한 내용이다._

_필요할 때 읽으면 될 것 같다. 🙏_

가장 중요한 수집기 팩터리는 `toList`, `toSet`, `toMap`, `groupingBy`, `joining`이다.

## 핵심 정리

- 스트림 파이프라인 프로그래밍의 핵심은 **부작용 없는 함수 객체**에 있다.
- 스트림뿐 아니라 스트림 관련 객체에 건네지는 모든 함수 객체가 부작용이 없어야 한다.
- 종단 연산 중 `forEach`는 스트림이 수행한 계산 결과를 보고할 때만 이용해야 한다.
  - 계산 자체에는 이용하지 말자.
- 가장 중요한 수집기 팩터리는 `toList`, `toSet`, `toMap`, `groupingBy`, `joining`이다.
