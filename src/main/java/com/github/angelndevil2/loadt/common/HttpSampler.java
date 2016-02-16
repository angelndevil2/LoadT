package com.github.angelndevil2.loadt.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 13.
 */
@Data
@AllArgsConstructor
public class HttpSampler implements Serializable, IHttpSampler {

    private static final long serialVersionUID = -8442400981248187020L;
    private final String name;
    private final String domain;
    private final int port;
    private final String path;
    private final HTTPMethod method;
    private final String systemInfoCollectorDomain;

}
