package com.example.hellen.smartmarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hellen.smartmarket.Models.Product;
import com.example.hellen.smartmarket.Models.Singleton;

public class ProductActivity extends AppCompatActivity {

    private String prodID;
    private String prodDesc;
    private int prodQuantity;
    private int prodPrice;
    Button btnPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle extras = getIntent().getExtras();
        prodID = extras.getString("prodID");
        prodDesc = extras.getString("prodDesc");
        prodPrice = extras.getInt("prodPrice");
        prodQuantity = extras.getInt("prodQuantity");

        btnPrueba = (Button) findViewById(R.id.btnPrueba);

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton.getInnstance().orderList.add(new Product(prodID,1));
                Log.d("onClick: ", String.valueOf(Singleton.getInnstance().orderList.size()));
            }
        });


    }
}
