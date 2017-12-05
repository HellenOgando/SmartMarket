package com.example.hellen.smartmarket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hellen.smartmarket.Models.Product;
import com.example.hellen.smartmarket.Models.Singleton;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProductActivity extends AppCompatActivity {

    private String prodID;
    private String prodDesc;
    private int prodQuantity;
    private int prodPrice;
    Button btnAddCart;
    FancyButton btnMas;
    FancyButton btnMenos;
    TextView tvProdDesc;
    TextView tvProdPrice;
    TextView tvProdQuantity;
    int cartQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle extras = getIntent().getExtras();
        prodID = extras.getString("prodID");
        prodDesc = extras.getString("prodDesc");
        prodPrice = extras.getInt("prodPrice");
        prodQuantity = extras.getInt("prodQuantity");

        btnAddCart = (Button) findViewById(R.id.btnAddCart);
        btnMas = (FancyButton) findViewById(R.id.btnMas);
        btnMenos = (FancyButton) findViewById(R.id.btnMenos);
        tvProdDesc = (TextView) findViewById(R.id.productName);
        tvProdPrice = (TextView) findViewById(R.id.productPrice);
        tvProdQuantity = (TextView) findViewById(R.id.productQuantity);

        tvProdDesc.setText(prodDesc);
        tvProdPrice.setText("RD$ " + prodPrice);

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Singleton.getInnstance().orderList.add(new Product(prodID,cartQuantity, prodPrice));
                finish();
            }
        });

        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cartQuantity < prodQuantity) {
                    cartQuantity++;
                    tvProdQuantity.setText(String.valueOf(cartQuantity));
                }
            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cartQuantity > 1) {
                    cartQuantity--;
                    tvProdQuantity.setText(String.valueOf(cartQuantity));
                }
            }
        });

    }
}
