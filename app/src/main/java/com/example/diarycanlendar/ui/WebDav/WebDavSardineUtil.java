package com.example.diarycanlendar.ui.WebDav;

import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class WebDavSardineUtil extends WebDavBaseTool {

    private Sardine sardine;

    public WebDavSardineUtil(String baseUri, String userName, String passWord){
        super(baseUri, userName, passWord);
        sardine = new OkHttpSardine();
        sardine.setCredentials(userName, passWord);
    }

    @Override
    public int deleteF(String url) throws IOException {
        sardine.delete(url);
        return 204;
    }

    @Override
    public int mkdirF(String url) throws IOException {
        sardine.createDirectory(url);
        return 201;
    }

    @Override
    public int uploadF(String url, FileInputStream fis) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(fis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] tp = new byte[1024];
        int len = -1;
        while((len = bis.read(tp)) != -1){
            bos.write(tp, 0, tp.length);
        }
        bis.close();
        bos.close();
        sardine.put(url, bos.toByteArray());
        return 201;
    }

    @Override
    public boolean existsF(String url) throws IOException {
        return sardine.exists(url);
    }

    @Override
    public int downloadF(String url, String filename) throws IOException {
        InputStream is = sardine.get(url);
        transStream2File(is, filename);
        return 200;
    }

    @Override
    public String[] getFilesF(String url) throws IOException {
        List<DavResource> resources = sardine.list(url);
        String[] res = new String[resources.size()];
        for (int i = 0; i < resources.size(); i ++)
        {
            DavResource curs = resources.get(i);
            res[i] = curs.toString().replace(super.getRoot(), "").replace(WORK_PATH, "");
        }
        return res;
    }

}
