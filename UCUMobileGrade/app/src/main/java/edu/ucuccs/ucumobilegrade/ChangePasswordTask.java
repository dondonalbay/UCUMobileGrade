package edu.ucuccs.ucumobilegrade;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Admin on 6/26/2016.
 */
public class ChangePasswordTask extends AsyncTask<String, Void, String> {
    public interface AsyncResponse{
        void processFinish(String output);
    }
    public AsyncResponse response=null;
    public ChangePasswordTask(AsyncResponse asyncResponse){
        response=asyncResponse;
    }

    @Override
    protected String doInBackground(String... params) {
        String result=null;
        String server=params[2];

        String urlQuery=server+"ucumobile/updatePassword.php";

        try{

            URL url= new URL(urlQuery);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(5000);


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data= URLEncoder.encode("pass", "UTF-8")+"="+ URLEncoder.encode(params[1], "UTF-8")+"&"+
                        URLEncoder.encode("id", "UTF-8")+"="+ URLEncoder.encode(params[0],"UTF-8");
                writer.write(data);
                writer.flush();
                writer.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                inputStream.close();
                result="Success";


        }catch (Exception e){
            result="Timeout";
        }




        return result;
    }

    @Override
    protected void onPostExecute(String result){
        response.processFinish(result);

    }
}
