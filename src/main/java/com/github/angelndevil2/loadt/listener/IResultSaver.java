package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.LoadTException;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 17.
 */
public interface IResultSaver extends IResultListener,  Runnable, Serializable {

    ResultSaverType getType();

    /**
     * start thread
     */
    void start();

    /**
     * save sample
     * @param sample
     */
    void save(final ISample sample) throws LoadTException;

    /**
     * save buffered sample
     */
    void flush();

    /**
     * finish saving and release resources
     */
    void close() throws LoadTException;

    /**
     *
     * @return save interval in millis
     */
    long getSaveInterval();

    /**
     * check {@link com.github.angelndevil2.loadt.common.SaveOptions} and save or not
     *
     */
    void saveOrNot(final ISample sample) throws LoadTException;
}
