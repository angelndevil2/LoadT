package com.github.angelndevil2.loadt.common;

import lombok.Data;
import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 *
 * Http options for HTTP samplers
 *
 * @author k, Created on 16. 2. 5.
 */
@Data
public class HTTPOptions implements Serializable {

    private static final long serialVersionUID = -8728337250754952072L;

    private boolean keepAlive = false;
    private boolean followRedirect = false;

    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject ret = new JSONObject();

        ret.put("keepAlive", keepAlive);
        ret.put("followRedirect", followRedirect);

        return ret;
    }

    public String toJSONString() {
        return toJSONObject().toJSONString();
    }
}
