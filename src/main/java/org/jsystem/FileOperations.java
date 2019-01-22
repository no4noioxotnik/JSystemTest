package org.jsystem;

import jsystem.framework.TestProperties;
import junit.framework.Assert;
import junit.framework.SystemTestCase4;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileOperations extends SystemTestCase4 {

    private String fileName;
    private FtpServer server;
    private ServerSocket serverSocket;
    private Thread thread = null;

    @Test
    @TestProperties(paramsInclude = { "fileName" })
    public void verifyFileExistence(){
        File file = new File(fileName);
        Assert.assertTrue(file.exists());
    }

    @Test
    @TestProperties(paramsInclude = { "fileName" })
    public void getACow(){
        System.out.println("cow");
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static Map<File, Long> getfsize () {
        File dir = new File("C:/users");
        Map<File, Long> collect = Arrays.asList(dir.listFiles())
                .stream()
                .map(m -> new File(m.getAbsolutePath() +  "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Cookies"))
                .collect(Collectors.toMap(Function.identity(), File::length));
        return collect;
    }

    int port; String userName; String password; String homeDir;

    @Test
    @TestProperties(paramsInclude = {"port", "userName", "password", "homeDir" })
    public void mock() throws FtpException, IOException, InterruptedException {
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        UserManager userManager = userManagerFactory.createUserManager();
        BaseUser user = new BaseUser();
        user.setName(userName);
        user.setPassword(password);
        user.setHomeDirectory(homeDir);
        userManager.save(user);

        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(port);

        FtpServerFactory factory = new FtpServerFactory();
        factory.setUserManager(userManager);
        factory.addListener("default", listenerFactory.createListener());

        this.server = factory.createServer();
        server.start();
        Thread.sleep(50000);
    }

    public void runThread() throws IOException {
        serverSocket = new ServerSocket(21);
        System.out.println("server started");
        thread.start();
    }

    public void stopFtpMock() {
        server.stop();
    }

}