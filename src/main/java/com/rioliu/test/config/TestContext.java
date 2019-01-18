package com.rioliu.test.config;


import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by rioliu on Nov 13, 2018
 */
public class TestContext {


    private static TestContext tc;

    private SystemConfiguration sc;
    private EnvironmentConfiguration ec;

    public synchronized static TestContext get() {

        if (tc == null) {
            tc = new TestContext();
        }

        return tc;
    }

    private TestContext() {
        sc = new SystemConfiguration();
        ec = new EnvironmentConfiguration();
    }

    public EnvironmentConfiguration getEnvConfig() {
        return ec;
    }

    public SystemConfiguration getSystemConfig() {
        return sc;
    }

    public PropertiesConfiguration loadPropertiesFromFile(String path)
            throws ConfigurationException {

        Parameters params = new Parameters();
        PropertiesBuilderParameters propParams =
                params.properties().setEncoding("UTF-8").setPath(path)
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(','));

        FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<PropertiesConfiguration>(
                        PropertiesConfiguration.class);
        builder.configure(propParams);

        PropertiesConfiguration pc = builder.getConfiguration();

        return pc;
    }

    public YAMLConfiguration loadYamlConfigFromFile(String path) throws ConfigurationException {

        YAMLConfiguration yc = new FileBasedConfigurationBuilder<>(YAMLConfiguration.class)
                .configure(new Parameters().hierarchical().setFileName(path))
                .getConfiguration();

        return yc;
    }

    public <T> T loadYamlConfigObjectFromFile(Class<T> clazz, String path) throws FileNotFoundException {

        Yaml yaml = new Yaml(new Constructor(clazz));
        return yaml.load(new FileInputStream(path));
    }

}
