package com.baurine.dagger2demo.model;

import android.graphics.Color;

import javax.inject.Inject;

/**
 * Created by baurine on 8/1/16.
 */
public class FruitInfo {
    public int color;
    public int size;

    @Inject
    public FruitInfo() {
        this.color = Color.RED;
        this.size = 100;
    }
}
