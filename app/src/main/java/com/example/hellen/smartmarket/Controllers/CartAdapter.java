package com.example.hellen.smartmarket.Controllers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellen.smartmarket.Models.Product;
import com.example.hellen.smartmarket.R;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>{

    private List<Product> listData;
    private LayoutInflater inflater;
    private Context c;

    public CartAdapter(List<Product> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.c = c;
        this.listData = listData;
    }
    @Override
    public CartAdapter.CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.productcardview, parent, false);
        return new CartHolder(v);
    }

    @Override
    public void onBindViewHolder(CartAdapter.CartHolder holder, int position) {

        Product item = listData.get(position);
        holder.prodDesc.setText(item.getProductDesc());
        holder.prodQuantity.setText("Cant: " + String.valueOf(item.getProductQuantity()));
        holder.prodPrice.setText("RD$ " + String.valueOf(item.getProductPrice()));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class CartHolder extends RecyclerView.ViewHolder{

        TextView prodDesc;
        TextView prodPrice;
        TextView prodQuantity;


        private CartHolder(View itemView) {
            super(itemView);

            prodDesc = (TextView) itemView.findViewById(R.id.productName);
            prodPrice = (TextView) itemView.findViewById(R.id.productPrice);
            prodQuantity = (TextView) itemView.findViewById(R.id.productQuantity);
        }
    }
}