package com.example.diarycalendar.ui.WebDav;

import java.io.FileInputStream;
import java.io.IOException;

public interface WebDavUtil {

    /**
     *
     * @param path : 客户端相对路径
     * @return
     *  <ul>
     *  <li>204 No Content  删除成功，没有返回内容</li>
     *  <li>403 Forbidden 删除失败， 禁止删除云盘根目录下的子文件夹</li>
     *  <li>404 Not Found 文件不存在，删除失败</li>
     *  <li>409 Conflict 服务器创建发生冲突</li>
     *  </ul>
     * @throws IOException
     */
    public int delete(String path) throws IOException;


    /**
     *
     * @param url : 绝对路径
     * @return
     *  <ul>
     *  <li>204 No Content  删除成功，没有返回内容</li>
     *  <li>403 Forbidden 删除失败， 禁止删除云盘根目录下的子文件夹</li>
     *  <li>404 Not Found 文件不存在，删除失败</li>
     *  <li>409 Conflict 服务器创建发生冲突</li>
     *  </ul>
     * @throws IOException
     */
    public int deleteF(String url) throws IOException;


    /**
     *  	创建文件夹， 该文件夹不存在则创建，不会嵌套创建，如果目标文件夹的父文件夹不存在则不创建<p>
     *
     *
     * @param path :  相对路径
     * @return
     * *  <ul>
     *  <li>201 : created 创建成功</li>
     *  <li>409 : Conflict 服务器创建发生冲突</li>
     *  </ul>
     * @throws IOException
     */
    public int mkdir(String path) throws IOException;


    /**
     *  	创建文件夹， 该文件夹不存在则创建，不会嵌套创建，如果目标文件夹的父文件夹不存在则不创建<p>
     *
     *
     * @param url :  绝对路径
     * @return
     * *  <ul>
     *  <li>201 : created 创建成功</li>
     *  <li>409 : Conflict 服务器创建发生冲突</li>
     *  </ul>
     * @throws IOException
     */
    public int mkdirF(String url) throws IOException;


    /**
     *
     * 上传到云盘
     *
     * @param path : 相对路径
     * @param fis  :
     * @return
     * <ul>
     *  <li>201 : created 创建成功</li>
     *  <li>409 : Conflict 服务器创建发生冲突</li>
     *  </ul>
     * @throws IOException
     */
    public int upload(String path, FileInputStream fis) throws IOException;


    /**
     *
     * 上传到云盘
     *
     * @param url
     * @param fis
     * @return
     * <ul>
     *  <li>201 : created 创建成功</li>
     *  <li>409 : Conflict 服务器创建发生冲突</li>
     *  </ul>
     * @throws IOException
     */
    public int uploadF(String url, FileInputStream fis) throws IOException;


    /**
     * test if the path is exist or not
     * @param path : 相对路径
     * @return true if exist, else false.
     * @throws IOException check your connection.
     */
    public boolean exists(String path) throws IOException;


    /**
     * test if the path is exist or not
     * @param url : 绝对路径
     * @return true if exist, else false.
     * @throws IOException check your connection.
     */
    public boolean existsF(String url) throws IOException;


    /**
     *
     *  status:
     *
     * @param path :  云端相对路径
     * @param filename ：要保存的目标文件
     * @return
     *  <ul>
     *  <li>200 : OK 成功</li>
     *  </ul>
     * @throws IOException
     */
    public int download(String path, String filename) throws IOException;


    /**
     *
     *  status:
     *
     * @param url :  云端绝对路径
     * @param filename ：要保存的目标文件
     * @return
     *  <ul>
     *  <li>200 : OK 成功</li>
     *  </ul>
     * @throws IOException
     */
    public int downloadF(String url, String filename) throws IOException;


    /**
     * 获取指定文件（夹）和  文件夹下的子目录
     * @param path
     * @return
     */
    public String[] getFiles(String path) throws IOException;


    /**
     * 获取指定文件（夹）和  文件夹下的子目录
     * @param url
     * @return
     * @throws IOException
     */
    public String[] getFilesF(String url) throws IOException;

    /**
     * 在云盘建立根目录
     * @return 如果因为账号密码等问题或者网络导致建立失败，则返回false，否则true
     */
    public abstract boolean initCloud();


    /**
     * 根据path获得实际url路径
     * @param path
     * @return
     */
    public String toFullUrl(String path);


    /**
     * 根据path和filename获得实际url
     * @param path
     * @param filename
     * @return
     */
    public String toFullUrl(String path, String filename);


}
