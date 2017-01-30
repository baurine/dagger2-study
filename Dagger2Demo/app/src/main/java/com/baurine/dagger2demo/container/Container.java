package com.baurine.dagger2demo.container;

import com.baurine.dagger2demo.component.DaggerFruitComponent;
import com.baurine.dagger2demo.model.Fruit;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Created by baurine on 1/30/17.
 */

public class Container {
    // 6. 用 @Inject 声明要注入的成员变量
    // @Inject
    // Fruit fruit;

    @Named("typeA")
    @Inject
    Fruit fruit;

    @Named("typeB")
    @Inject
    Fruit fruitB;

    public Fruit getFruit() {
        return fruit;
    }

    public void init() {
        // 7. 使用 FruitComponent 的实现类进行注入
        DaggerFruitComponent.create().inject(this);
    }
}
