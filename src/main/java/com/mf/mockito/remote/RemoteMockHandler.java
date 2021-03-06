package com.mf.mockito.remote;

import java.util.List;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

class RemoteMockHandler<T> implements InternalMockHandler<T> {

    private static final long serialVersionUID = -2501811136817887864L;

    private final InternalMockHandler<T> handler;
    private RemoteMockitoClient client;

    RemoteMockHandler(MockHandler handler) {
        this(handler, null);
    }

    @SuppressWarnings("unchecked")
    RemoteMockHandler(MockHandler handler, RemoteMockitoClient client) {
        this.handler = (InternalMockHandler<T>) handler;
        this.client = client;
    }

    @Override
    public MockCreationSettings getMockSettings() {
        return handler.getMockSettings();
    }
    
    @Override
    public void setAnswersForStubbing(List<Answer<?>> answers) {
        handler.setAnswersForStubbing(answers);
    }

    @Override
    public InvocationContainer getInvocationContainer() {
        return handler.getInvocationContainer();
    }

    @Override
    public Object handle(Invocation invocation) throws Throwable {
        Object handled = null;
        boolean shouldSendAfterHandling = ((InvocationContainerImpl) handler.getInvocationContainer()).hasAnswersForStubbing();

        if (invocation instanceof SerialisableInvocation) {
            handled = handler.handle(invocation);
        } else {
            handled = handler.handle(new SerialisableInvocation(invocation));
        }

        if (shouldSendAfterHandling) {
            client.sendStubbedInvocationsFor(invocation.getMock());
        }

        return handled;
    }

    RemoteMockitoClient getClient() {
        return client;
    }

    void setClient(RemoteMockitoClient client) {
        this.client = client;
    }
}
