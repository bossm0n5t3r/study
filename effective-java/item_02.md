# 아이템 2. 생성자에 매개변수가 많다면 빌더를 고려하라

static 팩터리 메서드와 생성자 모두 선택적 매개변수가 많을 때 적절히 대응하기 어렵다.

`Nutritionfacts`라는 클래스를 예로 들면,

필수 항목 몇 개와 선택 항목 여러 개로 이루어져있고, 선택항목의 대다수의 값은 0이다.

이런 클래스용 생성자 혹은 정적 팩터리는 어떤 모습일까?

## 대안 1. 점층적 생성자 패턴(telescoping constructor pattern)

필수 매개변수만 받는 생성자,

필수 매개변수 + 선택 매개변수 1개를 받는 생성자,

필수 매개변수 + 선택 매개변수 2개를 받는 생성자,

...의 형태로 선택 매개변수를 전부 다 받는 생성자까지 늘려가는 방식이다.

```java
NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27);
```

이런 생성자를 쓰다보면 필요없는 매개변수도 넘겨야 하는 경우가 발생하는데, 보통 0 같은 기본값을 넘긴다.

점층적 생성자 패턴도 쓸 수는 있지만, **매개변수 개수가 많아지면 코드를 작성하거나 읽기 어렵다.**

## 대안 2. 자바빈 패턴(JavaBeans pattern)

매개변수가 없는 생성자로 객체를 만든 후, setter 메서드들을 호출해 원하는 매개변수의 값을 설정하는 방식

```java
NutritionFacts cocaCola = new NutritionFacts();
cocaCola.setServingSize(240);
cocaCola.setServings(8);
cocaCola.setCalories(100);
cocaCola.setSodium(35);
cocaCola.setCarbohydrate(27);
```

**객체 하나를 만들려면 메서드를 여러 개 호출해야 하고,**
**객체가 완전히 생성되기 전까지 일관성(consistency)이 무너진 상태에 놓인다는 심각한 단점이 있다.**

일관성이 무너지는 문제 때문에 **클래스를 불변으로 만들 수 없고, 스레드 안정성을 얻으려면 추가 작업(locking)등을 해줘야 한다.**

이러한 단점을 보완하는 방법으로 [`freezing`](https://softwareengineering.stackexchange.com/questions/369571/how-do-we-freeze-an-object-while-constructing-an-object-using-javabeans-pattern) 이 있다.

하지만 freezing은 **다루기 어렵고**, 사용한다고 하더라도 프로그래머가 freeze 메서드를 확실히 호출해줬는지

컴파일러가 보증할 방법이 없어서 **런타임 오류에 취약**하다.

## 대안 3. 빌더 패턴(Builder pattern)

**점층적 생성자 패턴의 안전성**과 **자바빈 패턴의 가독성**을 겸비했다.

필수 매개변수만으로 빌더 객체를 얻은 뒤, 빌더 객체가 제공하는 setter 메서드들로 원하는 선택 매개변수들을 설정하고,

마지막으로 매개변수가 없는 build 메서드를 호출해서 우리에게 필요한 객체를 얻는다.

```java
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
    .calories(100).sodium(35).carbohydrate(27).build();
```

빌더의 setter 메서드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출할 수 있다.

이런 방식을 **플루언트 API**(fluent API) 혹은 **메서드 연쇄**(method chaining)이라고 한다.

**빌더 패턴으로 파이썬이나 스칼라가 제공하는 `Named Optional Parameter`를 모방할 수 있다.**

**빌더 패턴은 계층적으로 설계된 클래스와 함께 쓰기에 좋다.**

```java
public abstract class Pizza {
    public enum Topping { HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        // 하위 클래스는 이 메서드를 재정의(overriding)하여
        // "this"를 반환하도록 해야 한다.
        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone(); // 아이템 50 참조
    }
}
```

```java
public class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override public NyPizza build() {
            return new NyPizza(this);
        }

        @Override protected Builder self() { return this; }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    @Override public String toString() {
        return toppings + "로 토핑한 뉴욕 피자";
    }
}
```

```java
public class Calzone extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false; // 기본값

        public Builder sauceInside() {
            sauceInside = true;
            return this;
        }

        @Override public Calzone build() {
            return new Calzone(this);
        }

        @Override protected Builder self() { return this; }
    }

    private Calzone(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }

    @Override public String toString() {
        return String.format("%s로 토핑한 칼초네 피자 (소스는 %s에)",
                toppings, sauceInside ? "안" : "바깥");
    }
}
```

이때 추상 빌더는 재귀적인 타입 매개변수를 사용하고 `self`라는 추상 메소드를 사용해
하위 클래스에서는 캐스팅하지 않고도 연쇄를 지원할 수 있다.
 
self 타입이 없는 자바를 위한 이 우회 방법을 `시뮬레이트한 셀프 타입(simulated self-type)` 관용구라 한다.

하위 클래스에서는 build 메소드의 리턴 타입으로 해당 하위 클래스 타입을 리턴하는 `공변 반환 타이핑(covariant return typing)`을
사용하면 클라이언트 코드에서 타입 캐스팅을 할 필요가 없어진다.

```java
NyPizza pizza = new NyPizza.Builder(SMALL)
        .addTopping(SAUSAGE)
        .addTopping(ONION)
        .build();
Calzone calzone = new Calzone.Builder()
        .addTopping(HAM)
        .sauceInside()
        .build();
```

생성자나 팩터리는 가변인수를 맨 마지막 매개변수에 한번밖에 못쓰기때문에, 빌더는 가변인수(vargars)를 여러 개 사용할 수 있다.

빌더 패턴은 상당히 유연해서 빌더 하나로 여러 객체를 생성할 수 있고, 매번 생성하는 객체에 변화를 줄 수 있다.

### 빌더 패턴의 예시

- Protobuf 생성 패턴

### 빌더 패턴의 단점

객체를 만들려면, 빌더부터 만들어야 한다. 성능에 민감한 상황에서는 빌더 생성 비용이 문제가 될 수 있다.

또한 점층적 생성자 패턴보다는 코드가 장황해서 매개변수가 4개 이상은 되어야 값어치를 한다.

나중에 매개변수가 많아질 가능성이 있는 경우, 애초에 빌더로 시작하는 편이 좋다.

## 핵심 정리

**생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는 것이 좋다.**
