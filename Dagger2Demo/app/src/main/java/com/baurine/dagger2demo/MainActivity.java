package com.baurine.dagger2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baurine.dagger2demo.container.Container;

public class MainActivity extends AppCompatActivity {

    private Container container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = new Container();
        container.init();

        initViews();
    }

    private void initViews() {
        TextView tvHello = (TextView) findViewById(R.id.tv_hello);
        tvHello.setText(container.getFruit().desc());
    }
}
