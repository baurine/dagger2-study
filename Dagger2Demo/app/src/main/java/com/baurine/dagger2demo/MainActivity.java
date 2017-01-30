package com.baurine.dagger2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baurine.dagger2demo.component.DaggerPoetryComponent;
import com.baurine.dagger2demo.container.FruitContainer;
import com.baurine.dagger2demo.model.Poetry;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private FruitContainer fruitContainer;

    @Inject
    Poetry poetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fruitContainer = new FruitContainer();
        fruitContainer.init();

        DaggerPoetryComponent.create().inject(this);

        initViews();
    }

    private void initViews() {
        TextView tvHello = (TextView) findViewById(R.id.tv_hello);
        tvHello.setText(fruitContainer.getFruit().desc());

        TextView tvPoem = (TextView) findViewById(R.id.tv_poem);
        tvPoem.setText(poetry.getPoem());
    }
}
