package edu.ucuccs.ucumobilegrade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewsActivity extends AppCompatActivity {
    //Variables
    private static final String key_json="postData";
    private static final String key_postContent="postContent";
    private static final  String key_postTime="postTime";
    private static final String key_postDate="postDate";
    private static final String key_userName="userName";
    boolean isConnected;
    public static  final  String auto_loginKey="Auto_LogIn";
    //Controls
    RelativeLayout relaNewsLayout, relaNewsConnection;
    TextView tvNewsConnection;


    //Object
    SwipeRefreshLayout swipeRefreshLayout;
    static View.OnClickListener myOnClickListener;
    static  View.OnClickListener mySheetClickListener;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<PostData> data;

    JSONArray ucuPostArray;
    SharedPreferences autoLogin;
    BottomSheetBehavior bottomSheetBehavior;
    PostBottomSheet postBottomSheet;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ucu_logo);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipePost);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        relaNewsLayout=(RelativeLayout)findViewById(R.id.relaNewsLayout);
        relaNewsConnection=(RelativeLayout)findViewById(R.id.relaNewsConnection);
        tvNewsConnection=(TextView)findViewById(R.id.txtConnectionNews);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostRefresh();
            }
        });
        autoLogin=getSharedPreferences(auto_loginKey, Context.MODE_PRIVATE);
        mySheetClickListener= new MySheetOnClickListener(this);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getResources().getString(R.string.app_name));
        connectionStatus();
        if(isConnected){
            getPost();
        }else{
            relaNewsLayout.setVisibility(View.GONE);
            relaNewsConnection.setVisibility(View.VISIBLE);
            tvNewsConnection.setText("No Internet Connection");
        }



         postBottomSheet= new PostBottomSheet();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postBottomSheet.show(getSupportFragmentManager(), postBottomSheet.getTag());
            }
        });
        bottomSheetBehavior=BottomSheetBehavior.from(findViewById(R.id.sheetBottomMainLayout));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //Button
    public void clickRetryPost(View view){
        connectionStatus();
        if(isConnected){
            getPost();
        }else{
            tvNewsConnection.setText("No Internet Connection");
        }


    }
    //Recycle SheetMenu
    public class MySheetOnClickListener implements View.OnClickListener{

        Context contextSheet;

        private MySheetOnClickListener(Context context) {

            this.contextSheet=context;
        }

        @Override
        public void onClick(View v) {
            selectedItemSheet(v);
        }
    }
    private void selectedItemSheet(View v){
        String code =((TextView)v.findViewById(R.id.sheetTextView)).getText().toString();

        if(code.equals("Subject")){
            Intent intent = new Intent(NewsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(code.equals("Post")){
            postBottomSheet.dismiss();
        }
        else if(code.equals("Password")){
            Intent intent = new Intent(NewsActivity.this, ChangePassword.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(NewsActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            SharedPreferences.Editor editor=autoLogin.edit();
            editor.clear();
            editor.commit();

        }


    }
    //Recycle SheetMenu



    //start Classes
    public void getPost(){
        class GetUcuPost extends AsyncTask<String, Void, String>{
            ProgressDialog dialog;
            @Override
            protected void onPreExecute(){
                dialog=new ProgressDialog(NewsActivity.this);
                dialog.setMessage("Please Wait");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String data_result=null;
                String line=null;
                String host=getResources().getString(R.string.serverhost);
                String urlPost=host+"ucumobile/post.php";
                try {
                    URL url = new URL(urlPost);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);

                    InputStream inputStream = httpURLConnection.getInputStream();
                    int conResponse = httpURLConnection.getResponseCode();
                    if (conResponse == httpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        StringBuilder stringBuilder= new StringBuilder();

                        //loop json
                        while((line=bufferedReader.readLine())!=null){
                            stringBuilder.append(line + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        data_result=stringBuilder.toString();
                    } else {

                    }
                }catch (Exception e){
                    data_result="Timeout";
                }

                return data_result;
            }

            @Override
            protected void onPostExecute(String data_result){
                dialog.dismiss();
                if(data_result.equals("Timeout")){
                   relaNewsLayout.setVisibility(View.GONE);
                    relaNewsConnection.setVisibility(View.VISIBLE);
                    tvNewsConnection.setText("Connection Timeout");
                }else{
                    viewPost(data_result);
                }

            }
        }

        GetUcuPost ucuPost=new GetUcuPost();
        ucuPost.execute();

    }
    public void getPostRefresh(){
        class GetUcuPost extends AsyncTask<String, Void, String>{

            @Override
            protected void onPreExecute(){

            }

            @Override
            protected String doInBackground(String... params) {
                String data_result=null;
                String line=null;
                String server=getResources().getString(R.string.serverhost);
                String urlPost=server+"ucumobile/post.php";
                try {
                    URL url = new URL(urlPost);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);

                    InputStream inputStream = httpURLConnection.getInputStream();
                    int conResponse = httpURLConnection.getResponseCode();
                    if (conResponse == httpURLConnection.HTTP_OK) {
                        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        StringBuilder stringBuilder= new StringBuilder();

                        //loop json
                        while((line=bufferedReader.readLine())!=null){
                            stringBuilder.append(line + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        data_result=stringBuilder.toString();
                    } else {

                    }
                }catch (Exception e){
                    data_result="Timeout";
                }

                return data_result;
            }

            @Override
            protected void onPostExecute(String data_result){
                swipeRefreshLayout.setRefreshing(false);
                if(data_result.equals("Timeout")){
                    Toast.makeText(getApplicationContext(),"Connection Timeout", Toast.LENGTH_LONG).show();
                }else{
                    viewPost(data_result);
                }

            }
        }

        GetUcuPost ucuPost=new GetUcuPost();
        ucuPost.execute();

    }


    public void viewPost(String postJson){
        try{
            JSONObject jsonObject=new JSONObject(postJson);
            ucuPostArray=jsonObject.getJSONArray(key_json);
            data= new ArrayList<>();
            for(int j= 0; j<ucuPostArray.length(); j++){
                JSONObject reOject=ucuPostArray.getJSONObject(j);
                String post;
                String postTime;
                String postDate;
                String username;
                data.add(new PostData(
                        username=reOject.getString(key_userName),
                        post=reOject.getString(key_postContent),
                        postTime=reOject.getString(key_postTime),
                        postDate=reOject.getString(key_postDate)
                ));
            }
            adapter = new CustomAdapter(data);
            recyclerView.setAdapter(adapter);

        }catch (Exception e){

        }

    }
    //end Classes

    public void connectionStatus(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        isConnected=networkInfo!=null && networkInfo.isConnectedOrConnecting();


    }


}
