package ua.com.i2i.mobylife;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;

public class HttpGetterTask extends AsyncTask<String, Void, String> {
    public HttpGetReceiver delegate=null;
    Context context;
    String output = "";
    int type;
    HashMap<String, String> headers;

    public HttpGetterTask(Context context) {
        super();
        delegate = (HttpGetReceiver)context;
        headers = new HashMap<String, String>();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... urls) {
        return makeHttpRequest(urls[0], headers);
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
        delegate.receiveHttpData(data, type);
    }



    public String makeHttpRequest(String url, HashMap<String, String> headers) {

        // создаём HTTP запрос
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            for(Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpGet.setHeader(key, value);
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            output = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;

    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void getCategories() {
        this.execute("http://104.131.143.21:8000/api/categories"); //TODO strings
        type = 0;
    }

    public void getApps(int categoryId) {
        this.execute("http://104.131.143.21:8000/api/apps?"+"category="+categoryId+"&page_size=5&page=1"); //TODO strings
        type = 1;
    }

    public void getApps() {
        this.execute("http://104.131.143.21:8000/api/apps&page_size=5&page=1"); //TODO strings
        type = 1;
    }
}


