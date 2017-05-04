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
public class SubjectAsyncTask extends AsyncTask<String, Void , String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse response = null;//Call back interface

    public SubjectAsyncTask(AsyncResponse asyncResponse) {
        response = asyncResponse;//Assigning call back interfacethrough constructor
    }



    @Override
    protected String doInBackground(String...link) {
        String data_result=null;
        String line=null;

        String host=link[1]+"ucumobile/getdata.php?"+"studId="+link[0];
        try{
            URL url=new URL(host);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(20000);
            httpURLConnection.setReadTimeout(20000);


            InputStream inputStream= httpURLConnection.getInputStream();

            int con=httpURLConnection.getResponseCode();
            if(con==httpURLConnection.HTTP_OK){

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

                data_result="Timeout";
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
