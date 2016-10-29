package com.example;

public class SomeCallback {
    final Bar bar;

    public SomeCallback(Bar bar){
        this.bar = bar;
    };

    public void onSuccess(String input) {
        System.out.println("The input is " + input);
        bar.bar("from callback with " + input);
    }
}
