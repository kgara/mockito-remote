package com.mf.mockito.remote;

import static com.mf.mockito.remote.BDDRemoteMockito.given;
import static com.mf.mockito.remote.BDDRemoteMockito.verify;
import static com.mf.mockito.remote.BDDRemoteMockito.verifyZeroInteractions;
import static com.mf.mockito.remote.BDDRemoteMockito.willAnswer;
import static com.mf.mockito.remote.BDDRemoteMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.example.Bar;
import com.example.Foo;
import com.example.SomeCallback;

@RunWith(MockitoJUnitRunner.class)
public class RemoteMockitoIT {

    @Mock
    Foo foo;

    @Mock
    Bar bar;

    RemoteMockitoClient fooServer = new RemoteMockitoClient("localhost", 8081);

    @Before
    public void remoteControl() {
        fooServer.remoteControl(foo, bar);
    }

    @Test
    public void shouldBeAbleToStubAndVerifyOnRemoteApplication() throws Exception {
        given(bar.bar(anyString())).willReturn("mock response for bar");
        verifyZeroInteractions(foo);
        new URL("http://localhost:8090/one").getContent();

        verify(foo).foo("mock response for bar");
        verify(foo).foo1(500L);
    }

    @Test(expected = IOException.class)
    public void willThrowTest() throws Exception {
        given(bar.bar(anyString())).willReturn("fail");
        willThrow(IllegalArgumentException.class).given(foo).foo("fail");

        new URL("http://localhost:8090/one").getContent();
    }

    @Test
    public void doAnswerStubbingInner() throws Exception {
        given(foo.addCallbackNonVoidResult(any(SomeCallback.class))).willAnswer(new SomeAnswerInner());
        new URL("http://localhost:8090/other").getContent();

        verify(bar).bar("from callback with blabla");
    }

    // Should not work with named or anonymous non-static inner classes,
    // because it also sends upper class (test class with mocks in our case) state
    // and fail to deserialize it on server side.
    @Test(expected = RuntimeException.class)
    public void doAnswerStubbingInlined() throws Exception {
        given(foo.addCallbackNonVoidResult(any(SomeCallback.class))).willAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                SomeCallback callback = (SomeCallback) invocationOnMock.getArguments()[0];
                callback.onSuccess("blabla");
                return "method response";
            }
        });
    }

    @Test
    public void doAnswerVoidMethod() throws Exception {
        willAnswer(new VoidAnswerInner()).given(foo).addCallback(any(SomeCallback.class));
        new URL("http://localhost:8090/other").getContent();

        verify(bar).bar("from callback with blabla");
    }

    public static class SomeAnswerInner implements Answer<String> {

        @Override
        public String answer(InvocationOnMock invocationOnMock) throws Throwable {
            SomeCallback callback = (SomeCallback) invocationOnMock.getArguments()[0];
            callback.onSuccess("blabla");
            return "method response";
        }
    }

    public static class VoidAnswerInner implements Answer<Void> {

        @Override
        public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            SomeCallback callback = (SomeCallback) invocationOnMock.getArguments()[0];
            callback.onSuccess("blabla");
            return null;
        }
    }
}


