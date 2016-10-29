package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyLauncher {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8090);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        MockingContext mockingContext = new MockingContext();
        context.addServlet(new ServletHolder(new SomeRemoteApp(mockingContext)), "/one");
        context.addServlet(new ServletHolder(new OtherRemoteApp(mockingContext)), "/other");

        server.start();
        server.join();
    }
}
