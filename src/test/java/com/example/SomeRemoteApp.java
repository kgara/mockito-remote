package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/someRemoteApplication/endpoint")
public class SomeRemoteApp extends HttpServlet {

    final private MockingContext mockingContext;

    public SomeRemoteApp(MockingContext mockingContext) {
        this.mockingContext = mockingContext;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Foo foo = mockingContext.getFoo();
        Bar bar = mockingContext.getBar();
        foo.foo(bar.bar("Some input"));
        foo.foo1(500L);
        response.setContentType("text/plain");
    }
}
