package com.github.angelndevil2.loadt.listener;

import com.github.angelndevil2.loadt.common.ISample;
import com.github.angelndevil2.loadt.common.LoadTException;
import com.github.angelndevil2.loadt.common.ResultType;
import com.github.angelndevil2.loadt.common.StatisticSample;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author k, Created on 16. 2. 17.
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class CSVFileSaver extends ResultSaver {

    private static final long serialVersionUID = 3895178069631620151L;
    private final String filename;
    @Getter(AccessLevel.NONE)
    private final PrintWriter out;

    public CSVFileSaver(final String filename) throws FileNotFoundException, UnsupportedEncodingException {
        this.filename = filename;
        out = getFileWriter(filename);
    }

    @Override
    public ResultSaverType getType() {
        return ResultSaverType.FILE;
    }

    public void save(@NonNull final StatisticSample r) {
        out.println(r.toCSVString());
    }

    /**
     * save sample
     *
     * @param sample
     */
    @Override
    public void save(@NonNull final ISample sample) throws LoadTException {
        if (sample instanceof StatisticSample) save((StatisticSample) sample);
        else throw new LoadTException("sample type("+sample.getClass().getName()+") is not suitable.");
    }

    /**
     * save buffered sample
     */
    @Override
    public void flush() {
        out.flush();
    }

    @Override
    public void close() throws LoadTException {
        if (!out.checkError()) out.close();
        else throw new LoadTException(filename + " has error.");
    }

    private PrintWriter getFileWriter(@NonNull final String filename) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writer;
        File pdir = (new File(filename)).getParentFile();
        if (pdir != null) {
            if (pdir.mkdirs()) {
                log.info("Folder " + pdir.getAbsolutePath() + " was created");
            }

            if (!pdir.exists()) {
                log.warn("Error creating directories for " + pdir.toString());
            }
        }

        writer = new PrintWriter(
                new OutputStreamWriter(
                        new BufferedOutputStream(
                                new FileOutputStream(filename, false)), "UTF-8"), true);
        log.debug("Opened file: " + filename);

        return writer;
    }

    @Override
    public ResultType getResultType() {
        return ResultType.STATISTIC;
    }
}
