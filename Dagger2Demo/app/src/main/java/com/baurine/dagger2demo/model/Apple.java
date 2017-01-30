package com.baurine.dagger2demo.model;

import android.graphics.Color;

/**
 * Created by baurine on 8/1/16.
 */
public class Apple extends Fruit {

    public Apple() {
        this(Color.RED, 50);
    }

    public Apple(int color, int size) {
        super(color, size);
    }

    @Override
    public String desc() {
        return "Apple";
    }
}
