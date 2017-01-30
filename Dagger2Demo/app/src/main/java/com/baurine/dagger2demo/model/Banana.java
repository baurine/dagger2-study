package com.baurine.dagger2demo.model;

import android.graphics.Color;

/**
 * Created by baurine on 8/1/16.
 */
public class Banana extends Fruit {

    public Banana() {
        this(Color.YELLOW, 30);
    }

    public Banana(int color, int size) {
        super(color, size);
    }

    @Override
    public String desc() {
        return "Banana";
    }
}
