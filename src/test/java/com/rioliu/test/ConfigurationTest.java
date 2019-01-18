package com.rioliu.test;

import com.rioliu.test.config.TestContext;
import com.rioliu.test.config.YamlConfig;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: rioliu
 * Date: 2019-01-18
 * To change this template use Preferences | File and Code Templates.
 */

public class ConfigurationTest {

    @Test
    public void testLoadYamlConfigByApacheAPI() {

        System.out.println("load YAML config by apache API");
        // load yaml config by apache interface
        try {
            YAMLConfiguration yc = TestContext.get().loadYamlConfigFromFile("src/test/resources/test.yml");

            System.out.println(yc.getString("info.name"));
            System.out.println(yc.getString("info.age"));
            System.out.println(yc.getString("info.city"));
            System.out.println(yc.getString("info.version"));

            assertThat(yc.getString("info.name"), equalToIgnoringCase("Peppa Pig"));
            assertThat(yc.getString("info.age"), equalToIgnoringCase("4"));
            assertThat(yc.getString("info.city"), equalToIgnoringCase("London"));
            assertThat(yc.getString("info.version"), equalToIgnoringCase("1.0"));

        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadYamlConfigObjectBySnakeYamlAPI() {

        System.out.println("load YAML config bean by snakeyaml API");
        // load yaml config by snakeyaml interface
        try {
            YamlConfig config = TestContext.get().loadYamlConfigObjectFromFile(YamlConfig.class, "src/test/resources/test.yml");

            System.out.println(config.getInfo().getName());
            System.out.println(config.getInfo().getAge());
            System.out.println(config.getInfo().getCity());
            System.out.println(config.getInfo().getVersion());

            assertThat(config.getInfo().getName(), equalToIgnoringCase("Peppa Pig"));
            assertThat(config.getInfo().getAge(), equalTo(4));
            assertThat(config.getInfo().getCity(), equalToIgnoringCase("London"));
            assertThat(config.getInfo().getVersion(), equalToIgnoringCase("1.0"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
