package com.example.hellen.smartmarket.Tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellen.smartmarket.Models.Product;
import com.example.hellen.smartmarket.Models.Singleton;
import com.example.hellen.smartmarket.R;

import java.util.List;

public class CartTab extends Fragment {

    public CartTab(){

    }

    RecyclerView rvProducts;
    TextView tvEmptyCart;
    List<Product> myOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.cart_tab, container, false);


        rvProducts = (RecyclerView) v.findViewById(R.id.rvProducts);
        tvEmptyCart = (TextView) v.findViewById(R.id.tvEmptyCart);

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            myOrder = Singleton.getInnstance().orderList;
            if(myOrder.size() > 0){
                tvEmptyCart.setVisibility(View.GONE);
                rvProducts.setVisibility(View.VISIBLE);
            }
            
        }
    }

}
