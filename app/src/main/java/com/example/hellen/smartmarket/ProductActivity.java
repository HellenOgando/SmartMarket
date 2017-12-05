package com.example.hellen.smartmarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProductActivity extends AppCompatActivity {

    private String prodDesc;
    private int prodQuantity;
    private int prodPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle extras = getIntent().getExtras();
        prodDesc = extras.getString("prodDesc");
        prodPrice = extras.getInt("prodPrice");
        prodQuantity = extras.getInt("prodQuantity");


    }
}
