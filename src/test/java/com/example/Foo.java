package com.example;

public interface Foo {
    void foo(String foo);
    void foo1(long parameter);
    void addCallback(SomeCallback someCallback);
    String addCallbackNonVoidResult(SomeCallback someCallback);
}
