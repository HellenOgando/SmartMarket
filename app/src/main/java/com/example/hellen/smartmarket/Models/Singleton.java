package com.example.hellen.smartmarket.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyip on 12/5/2017.
 */

public class Singleton {

    private static Singleton singleton = null;

    public List<Product> orderList;
    public List<Product> recentScans;

    private Singleton(){
        orderList = new ArrayList<>();
        recentScans = new ArrayList<>();
    }

    public static Singleton getInnstance(){

        if(singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }

}
