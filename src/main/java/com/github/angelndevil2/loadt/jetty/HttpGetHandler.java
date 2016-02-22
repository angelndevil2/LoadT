package com.github.angelndevil2.loadt.jetty;

import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.common.LoadTInformation;
import com.github.angelndevil2.loadt.loadmanager.ILoadManager;
import com.github.angelndevil2.loadt.util.ContextUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;

/**
 * GET method handler
 *
 * @since 0.0.2
 * @author k, Created on 16. 2. 18.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class HttpGetHandler extends AbstractHandler implements Serializable {
    private static final long serialVersionUID = 8377459901054654768L;

    @Getter(AccessLevel.NONE)
    private final HttpGetRequestParser uriParser = new HttpGetRequestParser();

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

        IRequest parserRequest;
        try {
            parserRequest = uriParser.getRequest(request.getRequestURI().substring(1)); // remove first "/"
        } catch (LoadTException e) {
            log.error("uri parsing exception",e);
            throw new ServletException(e);
        }

        try {

            String req = parserRequest.getTarget();
            if (ServiceUri.Uri4Get.INFO.equals(req)) {

                responseOk(response, LoadTInformation.toJSONString());

            } else if (ServiceUri.Uri4Get.LOAD_MANAGERS.equals(req)) {

                responseOk(response, getLoadManagers().toJSONString());

            } else if (ServiceUri.Uri4Get.LOAD_MANAGER.equals(req)) {

                if (parserRequest.getId() == null || parserRequest.getWhat() == null) throw new LoadTException("invalid uri "+request.getRequestURI());

                if (ServiceUri.Uri4Get.LoadManager.HTTP_OPTIONS.equals(parserRequest.getWhat())) {
                    JSONObject options = getHttpOptionsWithJSON(parserRequest.getId());
                    responseOk(response, options.toJSONString());
                }  else {
                    throw new LoadTException("invalid uri "+request.getRequestURI());
                }

            } else {
                throw new LoadTException("invalid uri "+request.getRequestURI());
            }
        } catch (LoadTException e) {
            responseBadRequest(response, e.toString());
        }

        baseRequest.setHandled(true);
    }

    /**
     * send ok response with result json string
     *
     * @param resp
     * @param result result json string
     */
    private void responseOk(HttpServletResponse resp, String result) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = resp.getWriter();
        out.println(result);
    }

    /**
     * send bad request
     *
     * @param resp
     * @param result
     * @throws IOException
     */
    private void responseBadRequest(HttpServletResponse resp, String result) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = resp.getWriter();
        out.println(result);
    }

    /**
     *
     * @return registered load manager's names json array
     */
    @SuppressWarnings("unchecked")
    private JSONArray getLoadManagers() {
        Set<String> managers = ContextUtil.getLoadManagers();
        JSONArray loadManagers = new JSONArray();
        if (managers != null) {
            loadManagers.addAll(managers);
        }

        return loadManagers;
    }

    /**
     *
     * @param loadManagerName load manager name
     * @return load manager's httpOptioins json object
     */
    private JSONObject getHttpOptionsWithJSON(@NonNull String loadManagerName) throws LoadTException {
        ILoadManager manager = ContextUtil.getLoadManager(loadManagerName);
        if (manager != null) {
            return manager.getContext().getHttpOptions().toJSONObject();
        }
        throw new LoadTException("load manager "+loadManagerName+" is not exist.");
    }
}
