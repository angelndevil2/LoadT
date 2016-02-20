package com.github.angelndevil2.loadt.jetty;

import com.github.angelndevil2.loadt.common.LoadTException;
import lombok.Data;
import lombok.NonNull;

/**
 * <h1>uri parsing base class for rest</h1>
 *
 * uri must be started with /LoadT.<br />
 *
 * ex) uri = /LoadT/info
 *
 * @author k, Created on 16. 2. 18.
 * @since 0.0.2
 */
@Data
public abstract class UriParser {

    /**
     *
     * @param uri uri being parser
     *
     * @return String[2] : split by first "/", first part String[0], remains String[1]
     */
    public String[] parseOne(@NonNull final String uri) throws LoadTException {
        String[] parsed = new String[2];

        int pos = uri.indexOf("/");
        if (pos < -1 || uri.length() == pos+1) throw new LoadTException("invalid uri");

        parsed[0] = uri.substring(0,pos);
        parsed[1] = uri.substring(pos+1);

        return parsed;
    }

    public abstract IRequest getRequest(final String uri) throws LoadTException;
}
