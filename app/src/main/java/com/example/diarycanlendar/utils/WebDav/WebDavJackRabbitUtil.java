package com.example.diarycanlendar.utils.WebDav;

public class WebDavJackRabbitUtil{

}

//import java.io.FileInputStream;
//import java.io.IOException;
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
//
//
///**
// *  the tool class.<p>
// *
// *  <h1>the main usage:</h1>
// *  <h2>step 1: </h2>use the url username and password to create a new instance like this:<p>
// *  <span>WebDavJackRabbitUtil util = new WebDavJackRabbitUtil( url, user, psd);</span>
// *
// *  <h2>step 2: do what you want</h2>
// *  <ul>
// *  <li>delete </li>
// *  <li>uplode</li>
// *  <li>mkdir</li>
// *  <li>download</li>
// *  <li>exist </li>
// *  </ul>
// *
// *
// * @author flyfish
// *
// */
//public class WebDavJackRabbitUtil  extends WebDavBaseTool{
//
//    private HttpClient client;
//    private HttpClientContext context;
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
//        super(baseUri, userName, passWord);
//        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//        HttpHost targetHost = new HttpHost(super.getUri().getHost(), super.getUri().getPort());
//
//        CredentialsProvider credsProvider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials upc = new UsernamePasswordCredentials(super.getUsername(), super.getPassword());
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
//    @Override
//    public int deleteF(String uri) throws IOException {
//        HttpDelete delete = new HttpDelete(uri);
//        HttpResponse execRel = this.client.execute(delete, this.context);
//        int status = execRel.getStatusLine().getStatusCode();
//        return status;
//    }
//
//    @Override
//    public int uploadF(String uri, FileInputStream fis) throws IOException {
//        HttpPut put = new HttpPut(uri);
//        InputStreamEntity requestEntity = new InputStreamEntity(fis);
//        put.setEntity(requestEntity);
//        HttpResponse a = this.client.execute(put, this.context);
//        int status = a.getStatusLine().getStatusCode();
//        return status;
//    }
//
//    @Override
//    public int mkdirF(String uri) throws IOException {
//        HttpMkcol mkcol = new HttpMkcol(uri);
//        HttpResponse a = this.client.execute(mkcol, this.context);
//        int status = a.getStatusLine().getStatusCode();
//        return  status;
//    }
//
//    @Override
//    public int downloadF(String uri, String filename) throws IOException {
//        HttpGet get = new HttpGet(uri);
//        HttpResponse execRel = this.client.execute(get, this.context);
//        StatusLine status = execRel.getStatusLine();
//        HttpEntity resp = execRel.getEntity();
//        transStream2File(resp.getContent(), filename);
//        return status.getStatusCode();
//    }
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
//    @Override
//    public boolean existsF(String url) throws IOException {
//        MultiStatusResponse[] responses = propfindF(url);
//        return responses != null ;
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
//   @Override
//    public String[] getFiles(String url) throws IOException {
//        MultiStatusResponse[] a = propfind(url);
//        return getFiles(a);
//    }
//}