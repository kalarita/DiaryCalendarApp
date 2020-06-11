package com.example.diarycanlendar.utils.WebDav;

//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.StatusLine;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.AuthCache;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.client.protocol.HttpClientContext;
//import org.apache.http.entity.InputStreamEntity;
//import org.apache.http.impl.auth.BasicScheme;
//import org.apache.http.impl.client.BasicAuthCache;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.jackrabbit.webdav.DavConstants;
//import org.apache.jackrabbit.webdav.DavException;
//import org.apache.jackrabbit.webdav.MultiStatus;
//import org.apache.jackrabbit.webdav.MultiStatusResponse;
//import org.apache.jackrabbit.webdav.client.methods.HttpMkcol;
//import org.apache.jackrabbit.webdav.client.methods.HttpPropfind;
//import org.apache.jackrabbit.webdav.property.DavPropertyNameSet;
//import org.apache.jackrabbit.webdav.version.DeltaVConstants;


/**
 *  the tool class.<p>
 *
 *  <h1>the main usage:</h1>
 *  <h2>step 1: </h2>use the url username and password to create a new instance like this:<p>
 *  <span>WebDavJackRabbitUtil util = new WebDavJackRabbitUtil( url, user, psd);</span>
 *
 *  <h2>step 2: do what you want</h2>
 *  <ul>
 *  <li>delete </li>
 *  <li>uplode</li>
 *  <li>mkdir</li>
 *  <li>download</li>
 *  <li>exist </li>
 *  </ul>
 *
 *
 * @author flyfish
 *
 */
public class WebDavJackRabbitUtil {
//
//
//    public static final String WORK_PATH_1 = "diarycanlendar";
//    public static final String WORK_PATH= "diarycanlendar/backup/";
//    private String username, password;
//    private URI uri;
//    private String root;
//    private HttpClient client;
//    private HttpClientContext context;
//    private boolean isOk = false;
//
//
//    /**
//     *  use the server uri , userName and password to create an instance.
//     *
//     *  <p><b>for Example:</b>
//     *
//     * @param baseUri : the Server Server's uri.
//     * @param userName : the user id.
//     * @param passWord : the password.
//     *
//     *
//     */
//    public WebDavJackRabbitUtil(String baseUri, String userName, String passWord) {
//        this.uri = URI.create(baseUri);
//        this.root = this.uri.toASCIIString();
//        if (!this.root.endsWith("/")) {
//            this.root += "/" ;
//        }
//        this.username = userName;
//        this.password = passWord;
//
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort());
//
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials upc = new UsernamePasswordCredentials(this.username, this.password);
//        credsProvider.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()), upc);
//
//        AuthCache authCache = new BasicAuthCache();
//        // Generate BASIC scheme object and add it to the local auth cache
//        BasicScheme basicAuth = new BasicScheme();
//        authCache.put(targetHost, basicAuth);
//
//        // Add AuthCache to the execution context
//        this.context = HttpClientContext.create();
//        this.context.setCredentialsProvider(credsProvider);
//        this.context.setAuthCache(authCache);
//        this.client = HttpClients.custom().setConnectionManager(cm).build();
//        initCloud();
//    }
//
//
//    /**
//     * 在云盘建立根目录
//     * @return 如果因为账号密码等问题或者网络导致建立失败，则返回false，否则true
//     */
//    public boolean initCloud() {
//        if(!isOk) {
//            try {
//                mkdirF(root + WORK_PATH_1);
//                mkdirF(root + WORK_PATH);
//                isOk = true;
//            } catch (IOException e) {
//                isOk = false;
//            }
//        }
//        return isOk;
//    }
//
//    /**
//     *
//     * @param uri : 绝对路径
//     * @return
//     *  <ul>
//     *  <li>204 No Content  删除成功，没有返回内容</li>
//     *  <li>403 Forbidden 删除失败， 禁止删除云盘根目录下的子文件夹</li>
//     *  <li>404 Not Found 文件不存在，删除失败</li>
//     *  <li>409 Conflict 服务器创建发生冲突</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int deleteF(String uri) throws IOException {
//        HttpDelete delete = new HttpDelete(uri);
//        HttpResponse execRel = this.client.execute(delete, this.context);
//        int status = execRel.getStatusLine().getStatusCode();
//        return status;
//    }
//
//
//    /**
//     *
//     * @param path : 客户端相对路径
//     * @return
//     *  <ul>
//     *  <li>204 No Content  删除成功，没有返回内容</li>
//     *  <li>403 Forbidden 删除失败， 禁止删除云盘根目录下的子文件夹</li>
//     *  <li>404 Not Found 文件不存在，删除失败</li>
//     *  <li>409 Conflict 服务器创建发生冲突</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int delete(String path) throws IOException {
//        return deleteF(toFullUrl(path));
//    }
//
//
//    /**
//     *
//     * 上传到云盘
//     *
//     * @param uri
//     * @param fis
//     * @return
//     * <ul>
//     *  <li>201 : created 创建成功</li>
//     *  <li>409 : Conflict 服务器创建发生冲突</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int uploadF(String uri, FileInputStream fis) throws IOException {
//        HttpPut put = new HttpPut(uri);
//        InputStreamEntity requestEntity = new InputStreamEntity(fis);
//        put.setEntity(requestEntity);
//        HttpResponse a = this.client.execute(put, this.context);
//        int status = a.getStatusLine().getStatusCode();
//        return status;
//    }
//
//
//    /**
//     *
//     * 上传到云盘
//     *
//     * @param path : 相对路径
//     * @param fis  :
//     * @return
//     * <ul>
//     *  <li>201 : created 创建成功</li>
//     *  <li>409 : Conflict 服务器创建发生冲突</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int upload(String path, FileInputStream fis) throws IOException {
//        return uploadF(toFullUrl(path), fis);
//    }
//
//    /**
//     *  	创建文件夹， 该文件夹不存在则创建，不会嵌套创建，如果目标文件夹的父文件夹不存在则不创建<p>
//     *
//     *
//     * @param uri :  绝对路径
//     * @return
//     * *  <ul>
//     *  <li>201 : created 创建成功</li>
//     *  <li>409 : Conflict 服务器创建发生冲突</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int mkdirF(String uri) throws IOException {
//        HttpMkcol mkcol = new HttpMkcol(uri);
//        HttpResponse a = this.client.execute(mkcol, this.context);
//        int status = a.getStatusLine().getStatusCode();
//        return  status;
//    }
//
//
//    /**
//     *  	创建文件夹， 该文件夹不存在则创建，不会嵌套创建，如果目标文件夹的父文件夹不存在则不创建<p>
//     *
//     *
//     * @param path :  相对路径
//     * @return
//     * *  <ul>
//     *  <li>201 : created 创建成功</li>
//     *  <li>409 : Conflict 服务器创建发生冲突</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int mkdir(String path) throws IOException {
//        return mkdirF(toFullUrl(path));
//    }
//
//    /**
//     *
//     *  status:
//     *
//     * @param uri :  云端绝对路径
//     * @param filename ：要保存的目标文件
//     * @return
//     *  <ul>
//     *  <li>200 : OK 成功</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int downloadF(String uri, String filename) throws IOException {
//        HttpGet get = new HttpGet(uri);
//        HttpResponse execRel = this.client.execute(get, this.context);
//        StatusLine status = execRel.getStatusLine();
//        HttpEntity resp = execRel.getEntity();
//        transStream2File(resp.getContent(), filename);
//        return status.getStatusCode();
//    }
//
//
//    /**
//     *
//     *  status:
//     *
//     * @param path :  云端相对路径
//     * @param filename ：要保存的目标文件
//     * @return
//     *  <ul>
//     *  <li>200 : OK 成功</li>
//     *  </ul>
//     * @throws IOException
//     */
//    public int download(String path, String filename) throws IOException {
//        return download(toFullUrl(path), filename);
//    }
//
//
//    /**
//     *  检索文件
//     *  status:
//     *  <ul>
//     *  <li>502 : Bad Getway</li>
//     *  </ul>
//     *
//     * @param testuri
//     * @return
//     * <ul>
//     * <li> {@code null} if the path or file is not exist</li>
//     * <li> the file or the path itself. if the {@code testuri}
//     * is a path then all its son will alse be returned.
//     *
//     * @throws IOException
//     */
//    public MultiStatusResponse[] propfindF(String testuri) throws IOException {
//
//        MultiStatusResponse[] responses = null;
//        try {
//            DavPropertyNameSet names = new DavPropertyNameSet();
//            names.add(DeltaVConstants.COMMENT);
//            // DavConstants.DEPTH_1
//            HttpPropfind propfind = new HttpPropfind(testuri, DavConstants.PROPFIND_ALL_PROP, names,
//                    DavConstants.DEPTH_1);
//            HttpResponse resp = this.client.execute(propfind, this.context);
//            int status = resp.getStatusLine().getStatusCode();
//            // assertEquals(207, status);
//            MultiStatus multistatus;
//            multistatus = propfind.getResponseBodyAsMultiStatus(resp);
//            responses = multistatus.getResponses();
//        } catch (DavException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }catch (IllegalArgumentException e) {
//            return null;
//        }
//        return responses;
//
//    }
//
//
//    /**
//     *  检索文件
//     *  status:
//     *  <ul>
//     *  <li>502 : Bad Getway</li>
//     *  </ul>
//     *
//     * @param path
//     * @return
//     * <ul>
//     * <li> {@code null} if the path or file is not exist</li>
//     * <li> the file or the path itself. if the {@code path}
//     * is a path then all its son will alse be returned.
//     *
//     * @throws IOException
//     */
//    public MultiStatusResponse[] propfind(String path) throws IOException {
//        return propfindF(toFullUrl(path));
//    }
//
//
//    /**
//     * test if the path is exist or not
//     * @param url : 绝对路径
//     * @return true if exist, else false.
//     * @throws IOException check your connection.
//     */
//    public boolean existsF(String url) throws IOException {
//        MultiStatusResponse[] responses = propfindF(url);
//        return responses != null ;
//    }
//
//    /**
//     * test if the path is exist or not
//     * @param path : 相对路径
//     * @return true if exist, else false.
//     * @throws IOException check your connection.
//     */
//    public boolean exists(String path) throws IOException {
//        MultiStatusResponse[] responses = propfind(path);
//        return responses != null ;
//    }
//
//
//    /**
//     *  测试路径下的文件是否存在
//     * @param path 路径
//     * @param fileName 文件名
//     * @return
//     * @throws IOException 网络连接不成功，检查您的网络状况
//     */
//    public boolean exists(String path, String fileName) throws IOException {
//        return exists(path + "/" + fileName);
//    }
//
//    /**
//     *  测试路径下的文件是否存在
//     * @param path 路径
//     * @param fileName 文件名
//     * @return
//     * @throws IOException 网络连接不成功，检查您的网络状况
//     */
//    public boolean existsF(String path, String fileName) throws IOException {
//        return existsF(path + "/" + fileName);
//    }
//
//    /**
//     * 获取指定文件（夹）和  文件夹下的子目录
//     * @param a
//     * @return
//     */
//    public String[] getFiles(MultiStatusResponse[] a) {
//        String[] res = new String[a.length];
//        for(int i = 0; i < a.length; i++) {
//            res[i] = a[i].getHref().replace(WORK_PATH, "").replace("/dav/", "");
//        }
//        return res;
//    }
//
//    /**
//     * 获取指定文件（夹）和  文件夹下的子目录
//     * @param url
//     * @return
//     * @throws IOException
//     */
//    public String[] getFiles(String url) throws IOException {
//        MultiStatusResponse[] a = propfind(url);
//        return getFiles(a);
//    }
//
//
//    /**
//     * 将输入流存放到目标文件中
//     * @param is ：输入流
//     * @param fileName ： 输出文件名，务必使用绝对路径 + 文件名
//     * @throws IOException
//     */
//    public void transStream2File(InputStream is, String fileName) throws IOException {
//        BufferedInputStream in = null;
//        BufferedOutputStream out = null;
//        in = new BufferedInputStream(is);
//        out = new BufferedOutputStream(new FileOutputStream(fileName));
//        int len = -1;
//        byte[] b = new byte[1024];
//        while ((len = in.read(b)) != -1) {
//            out.write(b, 0, len);
//        }
//        in.close();
//        out.close();
//    }
//
//    /**
//     * 根据path获得实际url路径
//     * @param path
//     * @return
//     */
//    public String toFullUrl(String path) {
//        return root + WORK_PATH + path;
//    }
//
//    /**
//     * 根据path和filename获得实际url
//     * @param path
//     * @param filename
//     * @return
//     */
//    public String toFullUrl(String path, String filename) {
//        return toFullUrl(path + "/" + filename);
//    }

}