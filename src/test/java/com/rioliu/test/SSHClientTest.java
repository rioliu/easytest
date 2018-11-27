package com.rioliu.test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.testng.annotations.Test;

import com.jcabi.ssh.Shell;
import com.jcabi.ssh.SshByPassword;
import com.rioliu.test.base.AbstractTestBase;
import com.rioliu.test.logging.Logger;
import com.rioliu.test.logging.LoggerFactory;

/**
 * Created by rioliu on Nov 27, 2018
 */
public class SSHClientTest extends AbstractTestBase {

    private static Logger logger = LoggerFactory.getConsoleLogger(SSHClientTest.class);

    public SSHClientTest() {}

    @Test
    public void testSSHLogin() {

        // start a docker container test_sshd with image rastasheep/ubuntu-sshd:14.04
        // docker run -d -p 8888:22 --name test_sshd rastasheep/ubuntu-sshd:14.04
        // $ ssh root@localhost -p 8888, pwd is root
        
        try {
            Shell shell = new SshByPassword("localhost", 8888, "root", "root");
            String out = new Shell.Plain(shell).exec("ls -l /");
            logger.info(out);
            assertThat(out, containsString("bin"));
        } catch (UnknownHostException e) {
            logger.error("SSH connection failed:", e);
        } catch (IOException e) {
            logger.error("Execute command via SSH session failed:", e);
        } 


    }

}
