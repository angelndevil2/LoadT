package com.github.angelndevil2.loadt;

import com.github.angelndevil2.loadt.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

/**
 * @author k, Created on 16. 2. 5.
 */
@Slf4j
public class Launcher {

    public static void main(String[] args) throws ParseException {

        CmdOptions options = new CmdOptions();

        try {
            options.setArgs(args);
        } catch (MissingOptionException e) {
            log.error("required option is missing", e);
            System.err.println(e.getMessage());
            exit(-1);
        }

        CommandLine cmd = options.getCmd();

        if (cmd.hasOption("h")) {
            options.printUsage();
            return;
        }

        if (cmd.hasOption("d")) {

            try {
                PropertiesUtil.setDirs(cmd.getOptionValue("d").trim());

            } catch (IOException e) {

                System.err.println(PropertiesUtil.getConfDir() + File.separator + PropertiesUtil.AppProperties + " not found. may use -d option" + e);
            }
        }
    }
}
