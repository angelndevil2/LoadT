package com.github.angelndevil2.loadt.jetty;

import org.json.simple.JSONObject;

/**
 * rest Request interface
 *
 * @author k, Created on 16. 2. 19.
 * @since 0.0.2
 */
public interface IRequest {
    /**
     * target for request
     */
    String getTarget();
    void setTarget(String target);
    /**
     * target object id, if target is multi objects.
     */
    String getId();
    void setId(String id);
    /**
     * information or operation for concern
     */
    String getWhat();
    void setWhat(String what);

    @SuppressWarnings("unchecked")
    JSONObject toJSONObject();

    @SuppressWarnings("unchecked")
    String toJSONString();
}
