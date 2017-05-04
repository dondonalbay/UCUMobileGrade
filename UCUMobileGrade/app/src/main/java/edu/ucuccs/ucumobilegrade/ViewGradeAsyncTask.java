package edu.ucuccs.ucumobilegrade;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Admin on 6/20/2016.
 */
public class ViewGradeAsyncTask extends AsyncTask<String, Void, String> {
    public interface AsyncResponse{
        void processFinish(String response);
    }
    public AsyncResponse response=null;

    public ViewGradeAsyncTask(AsyncResponse asyncResponse){
        response=asyncResponse;
    }

    @Override
    protected String doInBackground(String... params) {

        String data_result=null;
        String host=params[2]+"ucumobile/select_subCode.php?"+"studId="+params[0]
                +"&"+"subCode="+params[1];
        String line=null;
        try{
            URL url= new URL(host);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setReadTimeout(20000);
            InputStream inputStream= httpURLConnection.getInputStream();
            int conResponse=httpURLConnection.getResponseCode();
            if(conResponse==httpURLConnection.HTTP_OK){
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
                StringBuilder  stringBuilder=new StringBuilder();
                while ((line=reader.readLine())!=null){
                    stringBuilder.append(line + "\n");
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                data_result=stringBuilder.toString();
            }

        }catch (Exception e){
            data_result="Timeout";
        }

        return data_result;
    }

    @Override
    protected void onPostExecute(String data_result){
        response.processFinish(data_result);

    }
}
