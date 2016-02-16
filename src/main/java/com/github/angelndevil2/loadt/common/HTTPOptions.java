package com.github.angelndevil2.loadt.common;

import lombok.Data;

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
}
