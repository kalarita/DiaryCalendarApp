package com.example.diarycanlendar.ui.WebDav;

import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractWebDavBaseTool implements WebDavUtil {

    @Override
    public int delete(String path) throws IOException {
        return deleteF(toFullUrl(path));
    }

    @Override
    abstract public int deleteF(String url) throws IOException ;

    @Override
    public int mkdir(String path) throws IOException {
        return mkdirF(toFullUrl(path));
    }

    @Override
    public abstract int mkdirF(String url) throws IOException;

    @Override
    public int upload(String path, FileInputStream fis) throws IOException {
        return uploadF(toFullUrl(path), fis);
    }

    @Override
    public abstract int uploadF(String url, FileInputStream fis) throws IOException;

    @Override
    public boolean exists(String path) throws IOException {
        return existsF(toFullUrl(path));
    }

    @Override
    public abstract boolean existsF(String url) throws IOException;

    @Override
    public int download(String path, String filename) throws IOException {
        return downloadF(toFullUrl(path), filename);
    }

    @Override
    public abstract int downloadF(String url, String filename) throws IOException;

    @Override
    public String[] getFiles(String path) throws IOException {
        return getFilesF(toFullUrl(path));
    }

    @Override
    public abstract String[] getFilesF(String url) throws IOException;

    @Override
    public abstract boolean initCloud();

    @Override
    public abstract String toFullUrl(String path);

    @Override
    public String toFullUrl(String path, String filename) {
        return toFullUrl(path + "/" + filename);
    }
}
