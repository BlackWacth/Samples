package com.hua.okhttp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hua.okhttp.base.BaseActivity;
import com.hua.okhttp.global.C;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.BufferedSink;

public class BaseOkHttpActivity extends BaseActivity implements View.OnClickListener{

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private static final String IMGUR_CLIENT_ID = "123456";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private TextView mResponseInfo;
    private Button mRequst;

    private int position;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_ok_http);
        mResponseInfo = (TextView) findViewById(R.id.tv_base_okhttp_respone_info);
        mRequst = (Button) findViewById(R.id.btn_base_okhttp_request);
        mRequst.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        position = getIntent().getIntExtra(C.EXTRA_BASE_OKHTTP, 0);
        String requestText = "";
        switch (position) {
            case 0:
                requestText = "同步Get";
                break;

            case 1:
                requestText = "异步Get";
                break;

            case 2:
                requestText = "Post请求";
                break;

            case 3:
                requestText = "提取响应头";
                break;

            case 4:
                requestText = "Post提交String";
                break;

            case 5:
                requestText = "Post提交Stream";
                break;

            case 6:
                requestText = "Post提交文件";
                break;

            case 7:
                requestText = "Post提交表单";
                break;

            case 8:
                requestText = "Post提交分块请求";
                break;

            case 9:
                requestText = "Gson解析";
                break;

            case 10:
                requestText = "响应缓存";
                break;

            case 11:
                requestText = "超时";
                break;

            case 12:
                requestText = "Call配置";
                break;

            case 13:
                requestText = "验证处理";
                break;
        }
        mRequst.setText(requestText);
        setTitle(requestText);
    }

    private void request() {
        switch (position) {
            case 0:
                synchronousGet();
                break;

            case 1:
                asynchronousGet();
                break;

            case 2:
                postRequest();
                break;

            case 3:
                getAccessingHeaders();
                break;

            case 4:
                submitStringByPost();
                break;

            case 5:
                submitStreamByPost();
                break;

            case 6:
                submitFileByPost();
                break;

            case 7:
                submitFormByPost();
                break;

            case 8:
                multipartRequst();
                break;

            case 9:
                parseJsonWithGson();
                break;

            case 10:
                cacheResponse();
                break;

            case 11:
                timeout();
                break;

            case 12:
                callConfig();
                break;

            case 13:
                handingAuthenticate();
                break;
        }
    }

    /**
     * 验证处理
     */
    private void handingAuthenticate() {
        append("--------------Handing Authenticate--------------");
        showProgressDialog();
        OkHttpClient client = mOkHttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        append("\nAuthenticating for response : " + response);
                        append("Chllenges : " + response.challenges());

                        String credential = Credentials.basic("jesse", "password1");

                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }
                })
                .build();

        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();

        client.newCall(request).enqueue(new CallbackAdapter(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append("\n" + response.body().string());
            }
        });
    }

    /**
     * 配制call
     */
    private void callConfig() {
        append("--------------Call Config--------------");
        showProgressDialog();
        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/1") // This URL is served with a 1 second delay.
                .build();

        append("\norg readTimeout : " + mOkHttpClient.readTimeoutMillis());
        append("org writeTimeout : " + mOkHttpClient.writeTimeoutMillis());
        OkHttpClient client = mOkHttpClient.newBuilder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        append("\nclient readTimeout : " + client.readTimeoutMillis());
        append("client writeTimeout : " + client.writeTimeoutMillis());
        client.newCall(request).enqueue(new CallbackAdapter(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append("response1 : " + response.body().string());
            }
        });

        OkHttpClient copy = client.newBuilder()
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .build();
        append("\ncopy readTimeout : " + copy.readTimeoutMillis());
        append("copy writeTimeout : " + copy.writeTimeoutMillis());

        copy.newCall(request).enqueue(new CallbackAdapter(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append("response2 : " + response.body().string());
            }
        });
    }

    /**
     * 超时
     */
    private void timeout() {
        append("--------------Timeouts--------------");

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                .build();

        showProgressDialog();

        client.newCall(request).enqueue(new CallbackAdapter(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append(response.body().string());
            }
        });
    }

    /**
     * 响应缓存
     */
    private void cacheResponse() {
        append("--------------Cache Response--------------");
        int cacheSize = 10 * 1024 * 1024;
        File cacheDir = getCacheDir();
        Cache cache = new Cache(cacheDir, cacheSize);

        final OkHttpClient client = new OkHttpClient.Builder().cache(cache).build();

        final Request request = new Request.Builder().url("http://publicobject.com/helloworld.txt").build();

        showProgressDialog();
        client.newCall(request).enqueue(new CallbackAdapter(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);

//                append("one ----> " + response.body().string());
                append("\n");
                append("one ----> response : " + response);
                append("one ----> response.cacheResponse : " + response.cacheResponse());
                append("one ----> response.networkResponse : " + response.networkResponse());

                showProgressDialog();
                client.newCall(request).enqueue(new CallbackAdapter(){
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        super.onResponse(call, response);
//                        append("two ++++> " + response.body().string());
                        append("\n");
                        append("two ++++> response : " + response);
                        append("two ++++> response.cacheResponse : " + response.cacheResponse());
                        append("two ++++> response.networkResponse : " + response.networkResponse());
                    }
                });
            }
        });
    }

    /**
     * 使用Gson解析Json
     */
    private void parseJsonWithGson() {
        append("--------------Parse Json--------------");
        showProgressDialog();
        final Gson gson = new Gson();
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);

                Gist gist = gson.fromJson(response.body().string(), Gist.class);
                append(gist.files.size() + "");
                for(Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                    append(entry.getKey() + " : " + entry.getValue().content);
                }
            }
        });
    }

    static class Gist {
        Map<String, GistFile> files;
    }

    static class GistFile {
        String content;
    }

    /**
     * 通过Post提交分块请求
     */
    private void multipartRequst() {
        append("--------------Multipart Request--------------");
        showProgressDialog();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "Square Logo")
                .addFormDataPart("image", "ic_launcher.png", RequestBody.create(MEDIA_TYPE_PNG, new File("ic_launcher.png")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append(response.body().string());
            }
        });
    }

    /**
     * 通过Post提交表单
     */
    private void submitFormByPost() {
        append("--------------Submit Form--------------");
        showProgressDialog();
        RequestBody formBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(formBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append(response.body().string());
            }
        });
    }

    /**
     * 通过Post提交文件
     */
    private void submitFileByPost() {
        append("--------------Submit File--------------");
        File file = new File("/storage/emulated/0/android.java");

        showProgressDialog();
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append(response.body().string());
            }
        });
    }

    /**
     * 通过POST提交流
     */
    private void submitStreamByPost() {
        append("--------------Submit Stream--------------");
        showProgressDialog();
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers \n");
                sink.writeUtf8("------- \n");
                for (int i = 2; i <= 50; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append(response.body().string());
            }
        });
    }


    /**
     * Post提交String
     */
    private void submitStringByPost() {
        append("--------------Submit String--------------");
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";
        showProgressDialog();
        final Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        mOkHttpClient.newCall(request).enqueue(new CallbackAdapter() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
                append(response.body().string());
            }
        });
    }

    /**
     * 获取相应头
     */
    private void getAccessingHeaders() {
        append("--------------Accessing Headers--------------");
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/square/okhttp/issues")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        showProgressDialog();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideProgressDialog();
                append("Request Failed : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hideProgressDialog();
                append("Server : " + response.header("Server"));
                append("Date : " + response.header("Date"));
                append("Content-Length : " + response.header("Content-Length"));
                append("Vary : " + response.header("Vary"));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_base_okhttp_request:
                request();
                break;
        }
    }

    /**
     * 异步Get
     */
    private void asynchronousGet() {
        mResponseInfo.append("\n\n" + "-----------Asynchronous Get------------");
        showProgressDialog();
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideProgressDialog();
//                mResponseInfo.setText("Request Failed : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hideProgressDialog();

                Headers headers = response.headers();
                for(int i = 0; i < headers.size(); i++) {
                    append(headers.name(i) + " : " + headers.value(i));
                }
                append(response.body().string());
            }
        });
    }

    /**
     * 同步get
     */
    private void synchronousGet() {
        mResponseInfo.append("\n\n" + "-----------Synchronous Get------------");
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("http://publicobject.com/helloworld.txt")
                        .build();
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                        }
                    });

                    if(!response.isSuccessful()) {
                        append("Request Failed");
                        return ;
                    }

                    Headers headers = response.headers();
                    for(int i = 0; i < headers.size(); i++) {
                        append(headers.name(i) + " : " + headers.value(i));
                    }
                    append(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Post
     */
    private void postRequest() {
        mResponseInfo.append("\n\n" + "-----------Post Request------------");

        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormBody.Builder().build();

                final Request request = new Request.Builder()
                        .url("http://www.wooyun.org")
                        .post(formBody)
                        .build();
                try {
                    Response response = mOkHttpClient.newCall(request).execute();
                    if(response.isSuccessful()) {
                        append("Request Success : " + response.body().string());
                    } else {
                        append("Request Failed");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void append(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResponseInfo.append("\n" + text);
            }
        });
    }

    class CallbackAdapter implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            hideProgressDialog();
            append("Request Failed : " + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            hideProgressDialog();
        }
    }
}
