package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;

/**
 * @author k, Created on 16. 2. 15.
 */
public interface IResultViewer extends IResultListener {

    void view();

    /**
     *
     * @return save interval in millis
     */
    long getViewInterval();

    /**
     * check {@link com.github.angelndevil2.loadt.common.ViewOptions} and view or not
     *
     */
    void viewOrNot(final ISample sample);
}
