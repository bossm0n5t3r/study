package com.example.demoeffectivejava.item02.HierarchicalBuilder;

import static com.example.demoeffectivejava.item02.HierarchicalBuilder.NyPizza.Size.*;
import static com.example.demoeffectivejava.item02.HierarchicalBuilder.Pizza.Topping.*;

public class PizzaTest {
    public static void main(String[] args) {
        NyPizza pizza = new NyPizza.Builder(SMALL)
                .addTopping(SAUSAGE).addTopping(ONION).build();
        Calzone calzone = new Calzone.Builder()
                .addTopping(HAM).sauceInside().build();

        System.out.println(pizza);
        System.out.println(calzone);
    }
}
