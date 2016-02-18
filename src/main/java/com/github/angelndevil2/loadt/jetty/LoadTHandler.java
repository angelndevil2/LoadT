package com.github.angelndevil2.loadt.jetty;

import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * master class for request handling.
 *
 * @author k, Created on 16. 2. 18.
 */
public class LoadTHandler extends AbstractHandler {
    /**
     * Handle a request.
     *
     * @param target      The target of the request - either a URI or a name.
     * @param baseRequest The original unwrapped request object.
     * @param request     The request either as the {@link Request}
     *                    object or a wrapper of that request. The {@link AbstractHttpConnection#getCurrentConnection()}
     *                    method can be used access the Request object if required.
     * @param response    The response as the {@link Response}
     *                    object or a wrapper of that request. The {@link AbstractHttpConnection#getCurrentConnection()}
     *                    method can be used access the Response object if required.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();

        out.println("<h1>test</h1>");
        baseRequest.setHandled(true);
    }
}
