package com.baurine.dagger2demo.module;

import android.graphics.Color;

import com.baurine.dagger2demo.model.Apple;
import com.baurine.dagger2demo.model.Fruit;

import dagger.Module;
import dagger.Provides;

/**
 * Created by baurine on 1/30/17.
 */

// 1. @Module 注明本类是 Module 类
@Module
public class FruitModule {
    // 2. @Provides 注明本方法是用来提供依赖对象的特殊方法
    @Provides
    public Fruit provideFruit(int color) {
        return new Apple(color, 60);
    }

    // 8. provideColor() provides color for provideFruit() method's parameter: int color
    @Provides
    public int provideColor() {
        return Color.RED;
    }
}
