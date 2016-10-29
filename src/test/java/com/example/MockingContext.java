package com.example;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.mf.mockito.remote.RemoteMockitoServer;

public class MockingContext {
    private Foo foo = mock(Foo.class);
    private Bar bar = mock(Bar.class);
    private RemoteMockitoServer stubServer = new RemoteMockitoServer(8081, foo, bar);

    public MockingContext() throws IOException {
        stubServer.start();
    }

    public Foo getFoo() {
        return foo;
    }

    public void setFoo(Foo foo) {
        this.foo = foo;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public RemoteMockitoServer getStubServer() {
        return stubServer;
    }

    public void setStubServer(RemoteMockitoServer stubServer) {
        this.stubServer = stubServer;
    }
}
