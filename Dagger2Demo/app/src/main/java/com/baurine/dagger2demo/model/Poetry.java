package com.baurine.dagger2demo.model;

import javax.inject.Inject;

/**
 * Created by baurine on 1/30/17.
 */

public class Poetry {
    private String poem;

    @Inject
    public Poetry() {
        poem = "生活就像海洋";
    }

    public String getPoem() {
        return poem;
    }
}
