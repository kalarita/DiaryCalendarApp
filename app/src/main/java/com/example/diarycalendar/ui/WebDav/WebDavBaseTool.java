package com.example.diarycalendar.ui.WebDav;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class WebDavBaseTool extends AbstractWebDavBaseTool {

    public static final String WORK_PATH_1 = "diarycalendar";
    public static final String WORK_PATH= "diarycalendar/";
    private String username, password;
    private URI uri;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getRoot() {
        return root;
    }


    private String root;
    private boolean isOk = false;

    public WebDavBaseTool(String baseUri, String userName, String passWord) {
            this.uri = URI.create(baseUri);
            this.root = this.uri.toASCIIString();
            if (!this.root.endsWith("/")) {
                this.root += "/" ;
            }
            this.username = userName;
            this.password = passWord;
    }

    /**
     * 将输入流存放到目标文件中
     * @param is ：输入流
     * @param fileName ： 输出文件名，务必使用绝对路径 + 文件名
     * @throws IOException
     */
    public void transStream2File(InputStream is, String fileName) throws IOException {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        in = new BufferedInputStream(is);
        out = new BufferedOutputStream(new FileOutputStream(fileName));
        int len = -1;
        byte[] b = new byte[1024];
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isOk() {
        return isOk;
    }

    @Override
    public int deleteF(String url) throws IOException {
        return 0;
    }

    @Override
    public int mkdirF(String url) throws IOException {
        return 0;
    }

    @Override
    public int uploadF(String url, FileInputStream fis) throws IOException {
        return 0;
    }

    @Override
    public boolean existsF(String url) throws IOException {
        return false;
    }

    @Override
    public int downloadF(String url, String filename) throws IOException {
        return 0;
    }

    @Override
    public String[] getFilesF(String url) throws IOException {
        return new String[0];
    }

    @Override
    public boolean initCloud() {
        if(!isOk) {
            try {
                mkdirF(root + WORK_PATH_1);
                mkdirF(root + WORK_PATH);
                isOk = true;
            } catch (IOException e) {
                isOk = false;
            }
        }
        return isOk;
    }

    @Override
    public String toFullUrl(String path) {
        if(path.startsWith("/")){
            path = path.substring(1);
        }
        String res = root + WORK_PATH + path;
        return  res;
    }
}
