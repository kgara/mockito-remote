package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OtherRemoteApp extends HttpServlet {

    final private MockingContext mockingContext;

    public OtherRemoteApp(MockingContext mockingContext) {
        this.mockingContext = mockingContext;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Foo foo = mockingContext.getFoo();
        foo.addCallbackNonVoidResult(new SomeCallback(mockingContext.getBar()));
        foo.addCallback(new SomeCallback(mockingContext.getBar()));
        response.setContentType("text/plain");
    }
}
