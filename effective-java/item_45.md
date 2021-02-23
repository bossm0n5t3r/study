# 아이템 45. 스트림은 주의해서 사용하라

- [아이템 45. 스트림은 주의해서 사용하라](#아이템-45-스트림은-주의해서-사용하라)
  - [스트림 API는?](#스트림-api는)
  - [스트림 API가 제공하는 추상 개념 중 핵심은 두 가지](#스트림-api가-제공하는-추상-개념-중-핵심은-두-가지)
  - [코드](#코드)
  - [References](#references)

## 스트림 API는?

- 다량의 데이터 처리 작업(순차적이든, 병렬적이든)을 돕고자 자바 8에 추가됨

## 스트림 API가 제공하는 추상 개념 중 핵심은 두 가지

1.  스트림(stream)은 데이터 원소의 유한 혹은 무한 시퀀스(sequence)를 뜻한다.
2.  스트림 파이프라인(stream pipeline)은 이 원소들로 수행하는 연산 단계를 표현하는 개념이다.

---

스트림의 원소는 어디로부터든 올 수 있다.

- 대표적으로는, `컬렉션`, `배열`, `파일`, `정규표현식 패턴 매처(matcher)`, `난수 생성기`, 혹은 다른 스트림

스트림 안의 데이터 원소들은 객체 참조나, 기본 타입 값

- 기본 타입 값으로는 `int`, `long`, `double` 이렇게 세 가지를 지원

스트림 파이프라인은

- 소스 스트림에서 시작해 종단 연단(terminal operation)으로 끝난다.
- 그 사이에 하나 이상의 중간 연산(intermediate operation)이 있을 수 있다.
  - 각 중간 연산은 스트림을 어떠한 방식으로 변환(transform)한다.
    - 각 원소에 함수를 적용하거나, 특정 조건을 만족 못하는 원소를 걸러낼 수 있다는 의미
  - 중간 연산들은 변환 전 스트림의 원소 타입과 같을 수도 있고, 다를 수도 있다.
- 종단 연산은 마지막 중간 연산이 내놓은 스트림에 최후 연산을 가한다.
  - 원소를 정렬해 컬렉션에 담거나, 특정 원소 하나를 선택하거나, 모든 원소를 출력하는 식
- 지연 평가(lazy evalutaion)된다.
  - 평가는 종단 연산이 호출될 때 이뤄짐
  - 종단 연산에 쓰이지 않는 데이터 원소는 계산에 쓰이지 않음
  - 이러한 지연 평가가 무한 스트림을 다룰 수 있게 해주는 열쇠
  - 종단 연산이 없는 스트림 파이프라인은 아무 일도 않는 명령어인 no-op과 같으니, 종단 연산을 빼먹는 일이 절대 없도록 하자.

스트림 API는 메서드 연쇄를 지원하는 플루언트 API(fluent API)다.

- 즉 파이프라인 하나를 구성하는 모든 호출을 연결하여, 단 하나의 표현식으로 완성할 수 있다.
- 파이프라인 여러 개를 연결해 표현식 하나로 만들 수도 있다.

기본적으로 스트림 파이프라인은 순차적으로 수행된다.

- 파이프라인을 병렬로 실행하려면 파이프라인을 구성하는 스트림 중 하나에서 parallel 메서드를 호출해주기만 하면 되나,
- 효과를 볼 수 있는 상황은 많지 않다.

스트림 API는 다재다능하여 사실상 어떠한 계산이라도 해낼 수 있다.

- 하지만 할 수 있다는 뜻이지, 해야 한다는 뜻은 아니다.
- 스트림을 제대로 사용하면 프로그램이 짧고 깔끔해지지만
- 잘못 사용하면 유지보수도 힘들어진다.
- 스트림을 언제 써야 하는지를 규정하는 확고부동한 규칙은 없지만, 참고할 만한 노하우는 있다.

## 코드

다음 코드는 단어를 읽어서 사용자가 지정한 문턱값보다 원소 수가 많은 아나그램(anagram) 그룹을 출력한다.

> 아나그램(anagram)이란?
>
> 철자를 구성하는 알파벳이 값고 순서만 다른 단어

```kotlin
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.Arrays
import java.util.Scanner
import java.util.TreeSet

class IterativeAnagrams(
    private val dictionary: File,
    private val minGroupSize: Int
) {
    fun run() {
        val groups = mutableMapOf<String, TreeSet<String>>()
        try {
            val s = Scanner(dictionary)
            while (s.hasNext()) {
                val word = s.next()
                groups.computeIfAbsent(
                    alphabetize(word)
                ) { TreeSet<String>() }.add(word)
            }
        } catch (e: IOException) { }

        for (group in groups.values) {
            if (group.size >= minGroupSize) {
                println(group.size.toString() + ": " + group)
            }
        }
    }

    private fun alphabetize(s: String): String {
        val a = s.toCharArray()
        Arrays.sort(a)
        return String(a)
    }
}


fun main() {
    println("Example : IterativeAnagrams")
    val dictionary = File(dictionaryFilePath)
    val minGroupSize = 1

    val iterativeAnagrams = IterativeAnagrams(dictionary, minGroupSize)
    iterativeAnagrams.run()
}

// 실행 결과
//
// 2: [petals, staple]
// 2: [eros, rose]
```

이 프로그램의 아래 단계에 주목하자.

```kotlin
groups.computeIfAbsent(
    alphabetize(word)
) { TreeSet<String>() }.add(word)
```

맵에 각 단어를 삽입할 때 자바 8에서 추가된 `computeIfAbsent`메서드를 사용했다.

`computeIfAbsent`메서드는 아래와 같다.

```java
default V computeIfAbsent(K key,
        Function<? super K, ? extends V> mappingFunction) {
    Objects.requireNonNull(mappingFunction);
    V v;
    if ((v = get(key)) == null) {
        V newValue;
        if ((newValue = mappingFunction.apply(key)) != null) {
            put(key, newValue);
            return newValue;
        }
    }

    return v;
}
```

이 메서드는 맵 안에 키가 있는지 찾은 다음, 있으면 단순히 그 키에 매핑된 값을 반환한다.

키가 없으면, 건네진 함수 객체를 키에 적용하여 값을 계산해낸 다음, 그 키와 값을 매핑해놓고, 계산된 값을 반환한다.

이처럼 `computeIfAbsent`를 사용하면 각 키에 다수의 값을 매핑하는 맵을 쉽게 구현할 수 있다.

이제 같은 기능을 하는 다음 프로그램을 보자.

```kotlin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors.groupingBy

class StreamAnagrams(
    private val dictionary: Path,
    private val minGroupSize: Int
) {
    fun run() {
        try {
            val words = Files.lines(dictionary)
            words.collect(
                groupingBy { word ->
                    word.chars().sorted()
                        .collect(
                            { StringBuilder() },
                            { sb, c -> sb.append(c.toChar()) },
                            StringBuilder::append
                        ).toString()
                }
            )
                .values.stream()
                .filter { group -> group.size >= minGroupSize }
                .map { group -> group.size.toString() + ": " + group }
                .forEach(System.out::println)
        } catch (e: IOException) {}
    }
}

fun main() {
    println("Example : StreamAnagrams")
    val dictionary: Path = Paths.get(dictionaryFilePath)
    val minGroupSize = 1

    val streamAnagrams = StreamAnagrams(dictionary, minGroupSize)
    streamAnagrams.run()
}
```

코드를 이해하기 어려운가? 걱정하지 말자. 다른 사람도 마찬가지다.

**스트림을 과용하면 프로그램이 읽거나 유지보수하기 어려워진다.**

다행히 절충 지점이 있다.

다음 프로그램도 앞서의 두 프로그램과 기능은 같지만 스트림을 적당히 사용했다.

그 결과 원래 코드보다 짧을 뿐 아니라 명확하기까지 하다.

```kotlin
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Arrays
import java.util.stream.Collectors.groupingBy

class HybridAnagrams(
    private val dictionary: Path,
    private val minGroupSize: Int
) {
    fun run() {
        try {
            val words = Files.lines(dictionary)
            words.collect(
                groupingBy { word -> alphabetize(word) }
            )
                .values.stream()
                .filter { group -> group.size >= minGroupSize }
                .forEach { group -> println(group.size.toString() + ": " + group) }
        } catch (e: IOException) {}
    }

    private fun alphabetize(s: String): String {
        val a = s.toCharArray()
        Arrays.sort(a)
        return String(a)
    }
}

fun main() {
    println("Example : HybridAnagrams")
    val dictionary: Path = Paths.get(dictionaryFilePath)
    val minGroupSize = 1

    val hybridAnagrams = HybridAnagrams(dictionary, minGroupSize)
    hybridAnagrams.run()
}

```

스트림을 전에 본 적이 없더라도 이 코드는 이해하기 쉬울 것이다.

## References

- [Java 8 Stream API Analogies in Kotlin](https://www.baeldung.com/kotlin/java-8-stream-vs-kotlin)
- [Kotlin sequences vs. Java streams](https://discuss.kotlinlang.org/t/kotlin-sequences-vs-java-streams/14415)
