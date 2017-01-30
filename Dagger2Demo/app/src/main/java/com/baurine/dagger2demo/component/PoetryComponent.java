package com.baurine.dagger2demo.component;

import com.baurine.dagger2demo.MainActivity;

import dagger.Component;

/**
 * Created by baurine on 1/30/17.
 */

@Component
public interface PoetryComponent {
    void inject(MainActivity activity);
}
