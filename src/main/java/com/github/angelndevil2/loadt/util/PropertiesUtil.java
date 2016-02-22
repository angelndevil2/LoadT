package com.github.angelndevil2.loadt.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author k, Created on 16. 1. 30.
 */
@Slf4j
public class PropertiesUtil {

    public static final String LogbackConfig = "logback.xml";
    public static final String AppProperties = "loadt.properties";
    public static final String JmeterProperties = "jmeter.properties";
    /**
     *  * @since 0.0.2
     */
    public static final String JettyProperties = "jetty.properties";
    @Setter @Getter
    private static String baseDir;
    @Setter @Getter
    private static String confDir;
    @Setter @Getter
    private static String binDir;
    @Setter @Getter
    /**
     * @since 0.0.2
     */
    private static String webBaseDir;
    /**
     * LoadT's global properties
     */
    @Getter
    private static final Properties properties = new Properties();

    /**
     * load loadt.properties file
     *
     * @throws IOException
     */
    private static void loadProperties() throws IOException {
        properties.load(new FileInputStream(confDir + File.separator + AppProperties));
    }

    /**
     * load logback.xml file and {@link #reloadLogger}
     */
    private static void loadLogbackConfiguration() {
        System.setProperty("logback.configurationFile", confDir+File.separator+LogbackConfig);
        reloadLogger();
    }

    public static String getJMeterPropertiesFile() { return confDir+File.separator+JmeterProperties; }

    /**
     * @since 0.0.2
     * @return
     */
    public static String getJettyPropertiesFile() { return confDir+File.separator+JettyProperties; }

    /**
     * <ol>
     *  <li>set directory structure for LoadT</li>
     *  <li>load loadt propertes</li>
     *  <li>if logback.use = true, load logback configuration, default file is conf/logback.xml</li>
     * </ol>
     *
     */
    private static void setDirs() throws IOException {
        if (baseDir == null) baseDir = ".";
        confDir = baseDir+File.separator+"conf";
        binDir = baseDir+File.separator+"bin";
        webBaseDir = baseDir+File.separator+"static_web";

        loadProperties();
        if (Boolean.valueOf(properties.getProperty("logback.use"))) loadLogbackConfiguration();

    }

    public static void reloadLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        ContextInitializer ci = new ContextInitializer(loggerContext);
        URL url = ci.findURLOfDefaultConfigurationFile(true);

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            loggerContext.reset();
            configurator.doConfigure(url);
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }

    public static void setDirs(@NonNull String bd) throws IOException {
        baseDir = bd;
        setDirs();
    }
}
