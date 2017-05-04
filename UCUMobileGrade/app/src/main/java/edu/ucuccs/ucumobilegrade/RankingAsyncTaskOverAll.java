package edu.ucuccs.ucumobilegrade;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Created by Admin on 6/19/2016.
 */
public class RankingAsyncTaskOverAll extends AsyncTask<String, Void, String> {



    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;//Call back interface

    public RankingAsyncTaskOverAll(AsyncResponse asyncResponse) {
        delegate = asyncResponse;//Assigning call back interfacethrough constructor
    }

    @Override
    protected String doInBackground(String... params) {
        String data_result=null;
        String line=null;
        String urlQuery= null;

            urlQuery =params[0].toString().trim();

        try{
            URL url=new URL(urlQuery);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            InputStream inputStream= httpURLConnection.getInputStream();



            int conResponse=httpURLConnection.getResponseCode();
            if(conResponse==httpURLConnection.HTTP_OK){
                //encode by utf-8
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
                StringBuilder stringBuilder =new StringBuilder();

                //loop json
                while((line=reader.readLine())!=null)
                {
                    stringBuilder.append(line + "\n");
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                data_result=stringBuilder.toString();
            }else{

            }


        }catch (SocketTimeoutException e){
            data_result="Timeout";
        } catch (UnsupportedEncodingException e) {
            data_result="Timeout";
        } catch (MalformedURLException e) {
            data_result="Timeout";
        } catch (IOException e) {
            data_result="Timeout";
        }

        return data_result;
    }

    @Override
    protected void onPostExecute(String data_result){
        delegate.processFinish(data_result);

    }
}
