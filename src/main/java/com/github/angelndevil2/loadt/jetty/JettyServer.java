package com.github.angelndevil2.loadt.jetty;

import com.github.angelndevil2.loadt.util.PropertiesUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * <ol>
 *     <li>resource handler for static content</li>
 *     <li>default handler for not found</li>
 * </ol>
  * @author k, Created on 16. 2. 18.
 * @since 0.0.2
 */
@Data
@Slf4j
public class JettyServer implements Runnable, Serializable {

    private static final long serialVersionUID = 7346771122914495703L;

    private Server server;

    public void run() {

        Properties jettyProperties = new Properties();
        String pFileName = PropertiesUtil.getJettyPropertiesFile();
        try {
            jettyProperties.load(new FileInputStream(pFileName));
        } catch (IOException e) {
            log.error("load properties from {} error.", pFileName);
            return;
        }

        // crate server
        server = new Server(Integer.parseInt(jettyProperties.getProperty(PropList.HTTP_PORT)));

        // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
        // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
        ResourceHandler resource_handler = new ResourceHandler();
        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase(PropertiesUtil.getWebBaseDir());

        log.debug("static web base dir : {}",PropertiesUtil.getWebBaseDir());

        // Add a single handler on context "/LoadT"
        ContextHandler context = new ContextHandler();
        context.setContextPath( "/LoadT" );
        context.setHandler( new LoadTHandler() );

        // Add the ResourceHandler to the server.
        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);

        // make handler chain
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, context, new DefaultHandler() });
        gzip.setHandler(handlers);

        try {
            server.start();
        } catch (Exception e) {
            log.error("embedded jetty server start error.", e);
        }

        server.dumpStdErr();

        /*
        try {
            server.join();
        } catch (InterruptedException e) {
            log.info("jetty server interrupted", e);
        }*/
    }

    public void stop() throws Exception {
        if (server != null) server.stop();
    }

    public void join() throws InterruptedException {
        if (server != null) server.join();
    }
}
