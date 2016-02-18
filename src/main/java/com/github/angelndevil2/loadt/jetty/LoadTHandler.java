package com.github.angelndevil2.loadt.jetty;

import com.github.angelndevil2.loadt.common.LoadTInformation;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * master class for request handling.
 *
 * @author k, Created on 16. 2. 18.
 */
@Slf4j
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
    @SuppressWarnings("unchecked")
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {

        String result = null;
        final Map<String, String[]> paramMap = request.getParameterMap();
        // information requested
        if (paramMap.containsKey(ParamList.LOADT_INFO)) {
            if (paramMap.containsKey(ParamList.JSON_TYPE)) {
                result = LoadTInformation.toJSONString();
                response.setContentType("application/json");
            } else {
                result = LoadTInformation.toHtmlString();
                response.setContentType("text/html; charset=utf-8");
            }

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        PrintWriter out = response.getWriter();
        out.println(result);
        baseRequest.setHandled(true);
    }
}
