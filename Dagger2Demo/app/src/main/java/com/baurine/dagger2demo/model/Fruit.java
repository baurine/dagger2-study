package com.baurine.dagger2demo.model;

/**
 * Created by baurine on 8/1/16.
 */
public abstract class Fruit {
    protected int color;
    protected int size;

    public Fruit(int color, int size) {
        this.color = color;
        this.size = size;
    }

    public abstract String desc();
}
