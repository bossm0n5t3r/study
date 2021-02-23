# 아이템 45. 스트림은 주의해서 사용하라

- [아이템 45. 스트림은 주의해서 사용하라](#아이템-45-스트림은-주의해서-사용하라)
  - [스트림 API는?](#스트림-api는)
    - [스트림 API가 제공하는 추상 개념 중 핵심은 두 가지](#스트림-api가-제공하는-추상-개념-중-핵심은-두-가지)
    - [스트림의 원소는 어디로부터든 올 수 있다.](#스트림의-원소는-어디로부터든-올-수-있다)
    - [스트림 파이프라인은?](#스트림-파이프라인은)
    - [스트림 API의 특징](#스트림-api의-특징)
  - [아나그램(anagram) 그룹을 출력하기](#아나그램anagram-그룹을-출력하기)
    - [아나그램(anagram) 그룹을 출력하기 - 스트림 과용한 프로그램](#아나그램anagram-그룹을-출력하기---스트림-과용한-프로그램)
    - [아나그램(anagram) 그룹을 출력하기 - 적당히 스트림을 사용한 코드](#아나그램anagram-그룹을-출력하기---적당히-스트림을-사용한-코드)
  - [CharStream](#charstream)
  - [모든 반복문을 스트림으로 바꾸고 싶은 유혹이 일겠지만, 서두르지 않는 게 좋다.](#모든-반복문을-스트림으로-바꾸고-싶은-유혹이-일겠지만-서두르지-않는-게-좋다)
    - [함수 객체로는 할 수 없지만, 코드 블록으로는 할 수 있는 일들의 예](#함수-객체로는-할-수-없지만-코드-블록으로는-할-수-있는-일들의-예)
    - [그럼 스트림이 아주 안성맞춤인 일들은?](#그럼-스트림이-아주-안성맞춤인-일들은)
    - [스트림으로 처리하기 어려운 일은?](#스트림으로-처리하기-어려운-일은)
    - [메르센 소수(Mersenne prime) 구하기](#메르센-소수mersenne-prime-구하기)
    - [카드 덱을 초기화하는 작업](#카드-덱을-초기화하는-작업)
  - [결론](#결론)
  - [핵심 정리](#핵심-정리)
  - [References](#references)

## 스트림 API는?

- 다량의 데이터 처리 작업(순차적이든, 병렬적이든)을 돕고자 자바 8에 추가되었다.

### 스트림 API가 제공하는 추상 개념 중 핵심은 두 가지

1.  스트림(stream)은 데이터 원소의 유한 혹은 무한 시퀀스(sequence)를 뜻한다.
2.  스트림 파이프라인(stream pipeline)은 이 원소들로 수행하는 **연산 단계를 표현하는 개념**이다.

### 스트림의 원소는 어디로부터든 올 수 있다.

- 대표적으로는, `컬렉션`, `배열`, `파일`, `정규표현식 패턴 매처(matcher)`, `난수 생성기`, 혹은 다른 스트림

스트림 안의 데이터 원소들은 객체 참조나, 기본 타입 값

- 기본 타입 값으로는 `int`, `long`, `double` 이렇게 세 가지를 지원

### 스트림 파이프라인은?

- 소스 스트림에서 시작해 `종단 연단(terminal operation)`으로 끝난다.
- 그 사이에 하나 이상의 `중간 연산(intermediate operation)`이 있을 수 있다.
  - 각 중간 연산은 스트림을 어떠한 방식으로 `변환(transform)`한다.
    - 각 원소에 **함수를 적용**하거나, **특정 조건을 만족 못하는 원소를 걸러낼 수 있다**는 의미
  - 중간 연산들은 변환 전 스트림의 원소 타입과 **같을 수도 있고, 다를 수도 있다**.
- 종단 연산은 마지막 중간 연산이 내놓은 스트림에 최후 연산을 가한다.
  - 원소를 정렬해 컬렉션에 담거나, 특정 원소 하나를 선택하거나, 모든 원소를 출력하는 식
- **지연 평가(lazy evalutaion)**된다.
  - 평가는 종단 연산이 호출될 때 이뤄진다.
  - 종단 연산에 쓰이지 않는 데이터 원소는 계산에 쓰이지 않는다.
  - 이러한 지연 평가가 무한 스트림을 다룰 수 있게 해주는 열쇠
  - 종단 연산이 없는 스트림 파이프라인은 아무 일도 않는 명령어인 no-op과 같으니, 종단 연산을 빼먹는 일이 절대 없도록 하자.

### 스트림 API의 특징

스트림 API는 메서드 연쇄를 지원하는 **플루언트 API(fluent API)**다.

- 즉 파이프라인 하나를 구성하는 모든 호출을 연결하여, 단 하나의 표현식으로 완성할 수 있다.
- 파이프라인 여러 개를 연결해 표현식 하나로 만들 수도 있다.

기본적으로 스트림 파이프라인은 **순차적으로 수행**된다.

- 파이프라인을 병렬로 실행하려면 파이프라인을 구성하는 스트림 중 하나에서 parallel 메서드를 호출해주기만 하면 되나,
- 효과를 볼 수 있는 상황은 많지 않다. (아이템 48)

스트림 API는 다재다능하여 사실상 어떠한 계산이라도 해낼 수 있다.

- 하지만 할 수 있다는 뜻이지, **해야 한다는 뜻은 아니다**.
- 스트림을 제대로 사용하면 프로그램이 짧고 깔끔해지지만
- **잘못 사용하면 유지보수도 힘들어진다**.
- 스트림을 언제 써야 하는지를 규정하는 확고부동한 규칙은 없지만, **참고할 만한 노하우**는 있다.

---

## 아나그램(anagram) 그룹을 출력하기

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

### 아나그램(anagram) 그룹을 출력하기 - 스트림 과용한 프로그램

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

### 아나그램(anagram) 그룹을 출력하기 - 적당히 스트림을 사용한 코드

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

- try-with-resources 블록에서 사전 파일을 열고,
- 파일의 모든 라인으로 구성된 스트림을 얻고,
- 스트림 변수의 이름을 words로 지어 스트림 안의 각 **원소가 단어(word)임을 명확히 했다**.

이 스트림의 파이프라인에는 중간 연산은 없으며, 종단 연산에서는 모든 단어를 수집해 맵으로 모은다.

---

## CharStream

alphabetize 메서드도 스트림을 사용해 다르게 구현할 수 있다.

-> 하지만 그렇게 하면 명확성이 떨어지고, 잘못 구현할 가능성이 커진다.
-> 심지어 느려질 수 도 있다.
-> 자바가 기본 타입인 char 용 스트림을 지원하지 않기 때문이다.

다음은 문제를 직접 보여주는게 나을 것 같아 char 값들을 스트림으로 처리하는 코드를 준비 해보았다.

```kotlin
"Hello, world!".chars().forEach(System.out::print) // 72101108108111443211911111410810033
```

기대한 값은 `Hello, world!`였겠지만, 그렇지 않다 -> 스트림의 원소는 char 가 아닌 int 값이기 때문에

그러면 어떻게 하면 좋을까?

```kotlin
"Hello, world!".chars().forEach { x: Int -> print(x.toChar()) } // Hello, world!
```

위의 코드처럼 **형변환을 명시적으로** 해줘야 한다.

**하지만 char 값들을 처리할 때는 스트림을 삼가는 편이 낫다.**

---

## 모든 반복문을 스트림으로 바꾸고 싶은 유혹이 일겠지만, 서두르지 않는 게 좋다.

-> 스트림으로 바꾸는게 가능할지라도 코드 가독성과 유지보수 측면에서는 손해를 볼 수 있기 때문에
-> 중간 정도 복잡한 작업에도 (앞서의 아나그램 프로그램처럼) 스트림과 반복문을 적절히 조합하는게 최선이다.
-> 그러니, **기존 코드는 스트림을 사용하도록 리팩터링하되, 새 코드가 더 나아 보일 때만 반영하자.**
-> 그 기준은...? 🤔

스트림 파이프라인은 되풀이되는 계산을 함수 객체(주로 람다나 메서드 참조)로 표현한다.

반면 반복 코드에서는 코드 블록을 사용해 표현한다.

### 함수 객체로는 할 수 없지만, 코드 블록으로는 할 수 있는 일들의 예

- 코드 블록에서는 범위 안의 지역변수를 읽고 수정할 수 있다.
  - 하지만 람다에서는 final이거나 사실상 final인 변수만 읽을 수 있고, 지역변수를 수정하는 건 불가능하다.
- 코드 블록에서는 return 문을 사용해 메서드에서 빠져나가거나, break나 continue문으로 블록 바깥의 반복문을 종료하거나 반복을 한 번 건너뛸 수 있다. 또한 메서드 선언에 명시된 검사 예외를 던질 수 있다.
  - 하지만 람다로는 이 중 어떤 것도 할 수 없다.

계산 로직에서 이상의 일들을 수행해야 한다면 스트림과는 맞지 않는 것이다.

### 그럼 스트림이 아주 안성맞춤인 일들은?

다음 중 하나를 수행하는 로직이라면 스트림을 적용하기에 좋은 후보다.

- 원소들의
  - 시퀀스를 **일관되게 변환**한다.
  - 시퀀스를 **필터링**한다.
  - 시퀀스를 **하나의 연산을 사용해 결합**한다. (더하기, 연결하기, 최솟값 구하기 등)
  - 시퀀스를 **컬렉션에 모은다**.
  - 시퀀스에서 **특정 조건을 만족하는 원소를 찾는다**.

### 스트림으로 처리하기 어려운 일은?

대표적인 예로, 한 데이터가 파이프라인의 여러 단계(stage)를 통과할 때 이 데이터의 각 단계에서의 값들에 동시에 접근하기는 어려운 경우
-> 왜냐하면, 스트림 파이프라인은 일단 한 값을 다른 값에 매핑하고 나면, 원래의 값은 잃는 구조이기 때문에

원래 값과 새로운 값의 쌍을 저장하는 객체를 사용해 매핑하는 우회 방법도 있지만, 그리 만족스러운 해법은 아닐 것이다.

매핑 객체가 필요한 단계가 여러 곳이라면 특히 더 그렇다.

이런 방식은 코드 양도 많고, 지저분하여 스트림을 쓰는 주목적에서 완전히 벗어난다.

가능한 경우라면, 앞 단계의 값이 필요할 때 매핑을 거꾸로 수행하는 방법이 나을 것이다.

---

### [메르센 소수(Mersenne prime)](https://en.wikipedia.org/wiki/Mersenne_prime) 구하기

예를 들어 처음 20개의 [메르센 소수(Mersenne prime)](https://en.wikipedia.org/wiki/Mersenne_prime)을 출력하는 프로그램을 작성해보자.
-> 메르센 수는 2^n - 1, for some integer n
-> 메르센 소수는 2^p - 1, where p is a prime 형태의 소수

다음 코드는 스트림을 반환하는 메서드이고, BigInteger의 정적 멤버들은 정적 임포트하여 사용한다고 가정한다.

```kotlin
class MersennePrimes {
    fun primes(): Stream<BigInteger> {
        // 메서드 이름에서 스트림의 원소가 소수임을 말해준다.
        return Stream.iterate(TWO) { obj: BigInteger -> obj.nextProbablePrime() }
    }
}
```

스트림을 반환하는 메서더 이름은 원소의 정체를 알려주는 복수 명사로 쓰기를 강력히 추천한다.
-> 스트림 파이프라인의 가독성이 크게 좋아질 것이기 때문에

다음은 처음 20개의 메르센 소수를 출력하는 프로그램이다.

```kotlin
fun main() {
    MersennePrimes().primes().map { p -> TWO.pow(p.intValueExact()).subtract(ONE) } // 소수들을 사용해서 메르센 수를 계산
        .filter { mersenne -> mersenne.isProbablePrime(50) } // 계산한 메르센 수에 대해서 소수인 경우만 남긴다.
        .limit(20) // 결과 스트림의 원소 수를 20개로 제한
        .forEach(System.out::println) // 작업이 끝나면 출력
}

// 3
// 7
// 31
// 127
// 8191
// 131071
// 524287
// 2147483647
// 2305843009213693951
// 618970019642690137449562111
// ...
```

이제 여기서 메르센 소수(2^p - 1)의 p를 출력하기를 원한다고 해보자.

이 값은 초기 스트림에만 나타나므로 결과를 출력하는 종단 연산에서는 접근할 수 없다.

다행히, 첫 번째 중간 연산에서 수행한 매핑을 거꾸로 수행해 메르센 수의 지수를 쉽게 계산해낼 수 있다.

지수는 단순히 숫자를 이진수로 표현한 다음 몇 비트인지 세면 나오므로,

종단 연산을 다음처럼 작성하면 원하는 결과를 얻을 수 있다.

```kotlin
.forEach { mp -> println(mp.bitLength().toString() + ": " + mp) } // 작업이 끝나면 지수 p와 메르센 소수를 출력

// 2: 3
// 3: 7
// 5: 31
// 7: 127
// 13: 8191
// 17: 131071
// 19: 524287
// 31: 2147483647
// 61: 2305843009213693951
// 89: 618970019642690137449562111
// ...
```

---

### 카드 덱을 초기화하는 작업

스트림과 반복 중 어느 쪽을 써야 할지 바로 어려운 작업도 많다.

다음은 카드 덱을 초기화하는 작업이다.

`카드` : 숫자(rank)와 무늬(suit)를 묶은 불변 값 클래스

`숫자`, `무늬`: 열거 타입

이 작업은 두 집합의 원소들로 만들 수 있는 가능한 모든 조합을 계산하는 문제

-> 수학자들은 이를 두 집합의 데카르트 곱이라고 부른다.

```kotlin
// 데카르트 곱 계산을 반복 방식으로 구현
private fun newDeckLoop(): List<Card> {
    val result: MutableList<Card> = ArrayList()
    for (suit in Suit.values()) for (rank in Rank.values()) result.add(Card(suit, rank))
    return result
}

// 데카르트 곱 계산을 스트림 방식으로 구현
private fun newDeckStream(): List<Card> {
    return Suit.values()
        .flatMap { suit -> Rank.values().map { rank -> Card(suit, rank) } }
        .toList()
}
```

---

## 결론

어느 newDeck 이 더 좋아 보이는지는 결국 개인 취향과 프로그래밍 환경의 문제다.

처음 방식은 더 단순하고 아마 더 자연스러워 보일 것이다.

이해하고 유지보수하기에 처음 코드가 더 편한 프로그래머가 많겠지만, 두 번째인 스트림 방식을 편하게 생각하는 프로그래머도 있다.

## 핵심 정리

스트림, 반복방식 모두 각자에게 더 알맞은 방식이 있다.

수많은 작업이 이 둘을 조합했을 때 가장 멋지게 해결된다.

스트림과 반복 중 어느 쪽이 나은지 어렵다면 둘 다 해보고 더 나은 쪽을 택하자.

## References

- [Java 8 Stream API Analogies in Kotlin](https://www.baeldung.com/kotlin/java-8-stream-vs-kotlin)
- [Kotlin sequences vs. Java streams](https://discuss.kotlinlang.org/t/kotlin-sequences-vs-java-streams/14415)
