package com.github.angelndevil2.loadt.jetty;

import lombok.Data;
import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * rest get request uri parsed request
 *
 * @author k, Created on 16. 2. 19.
 */
@Data
public class HttpGetRequest implements Serializable, IRequest {

    private static final long serialVersionUID = 4780607070143431652L;

    /**
     * target for request
     */
    private String target;
    /**
     * target object id, if target is multi objects.
     */
    private String id;
    /**
     * information for concern
     */
    private String what;

    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject ret = new JSONObject();
        ret.put("target", target);
        ret.put("id", id);
        ret.put("what", what);

        return ret;
    }

    @SuppressWarnings("unchecked")
    public String toJSONString() {
        return toJSONObject().toJSONString();
    }
}
