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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DecimalFormat;

public class ViewGrade extends AppCompatActivity {
    TextView tvGrade,tvRemark,tvCode,tvName,tvShed,tvsubIns,
            tvSkulYear,tvOrigGrade,tvPrevGrade,tvConnection;

    RelativeLayout netLayout;
    ScrollView viewLayout;

    String intentCode=null;
    String sharedId=null;
    boolean isConnected;

    private  static  final  String key_json="studData";
    private  static  final  String key_subCode="subCode";
    private  static  final  String key_subName="subName";
    private  static  final  String key_studGrade="studGrade";
    private  static  final  String key_studRemark="studRemark";
    private  static  final  String key_subShed="subShed";
    private  static  final  String key_sy="sy";
    private  static  final  String key_prevGrade="prevGrade";
    private  static  final  String key_subIns="subIns";
    public static final String shared_key="id_key";
    public static final String shared_shed="shed_key";
    public static final String shared_code="code_key";
    public static final String shared_subName="subName_key";


    public static  final String shared_name="Student_ID";
    public static  final String shared_rank="Student_Rank";
    public static  final  String auto_loginKey="Auto_LogIn";

    JSONArray info_stuGrade=null;
    SharedPreferences sharepref,sharedRank,autoLogin;
    ViewGradeAsyncTask viewGradeAsyncTask;
    ProgressDialog dialog;
    static  View.OnClickListener mySheetClickListener;
    BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grade);


        final ViewGradeBottomSheet viewGradeBottomSheet= new ViewGradeBottomSheet();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewGradeBottomSheet.show(getSupportFragmentManager(), viewGradeBottomSheet.getTag());
            }
        });
         bottomSheetBehavior=BottomSheetBehavior.from(findViewById(R.id.sheetBottomMainLayout));

        tvCode=(TextView)findViewById(R.id.txtSubcode);
        tvName=(TextView)findViewById(R.id.txtsubName);
        tvShed=(TextView)findViewById(R.id.txtsubShed);
        tvsubIns=(TextView)findViewById(R.id.txtsubIns);
        tvRemark=(TextView)findViewById(R.id.txtRemark);
        tvGrade=(TextView)findViewById(R.id.txtGrade);
        tvPrevGrade=(TextView)findViewById(R.id.txtPreviousGrade);
        tvOrigGrade=(TextView)findViewById(R.id.txtOrigGrade);
        tvSkulYear=(TextView)findViewById(R.id.txtSkulYear);
        tvConnection=(TextView)findViewById(R.id.txtViewGradeConnection);

        viewLayout=(ScrollView) findViewById(R.id.scrollViewGrade);
        netLayout=(RelativeLayout) findViewById(R.id.netViewGradeLayout);
        autoLogin=getSharedPreferences(auto_loginKey, Context.MODE_PRIVATE);
        sharepref=getSharedPreferences(shared_name, Context.MODE_PRIVATE);
            sharedId=sharepref.getString(shared_key,"");
        sharedRank=getSharedPreferences(shared_rank, Context.MODE_PRIVATE);

        dialog= new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        Intent intent=getIntent();
        Bundle bundle_code=intent.getExtras();
        if(bundle_code!=null){
            intentCode=bundle_code.get("courseCde").toString();
                conectionStatus();
                if(isConnected){
                    runSync(sharedId, intentCode);
                }else{
                    dialog.dismiss();
                    viewLayout.setVisibility(View.GONE);
                    netLayout.setVisibility(View.VISIBLE);
                    tvConnection.setText("No Internet Connection");
                }

        }else{

        }

    mySheetClickListener= new MySheetOnClickListener(this);

    }

    public void clickViewRank(View view){
        conectionStatus();
        if(isConnected){

            SharedPreferences.Editor editor=sharedRank.edit();
            editor.clear();
            editor.putString(shared_code,tvCode.getText().toString());
            editor.putString(shared_shed,tvShed.getText().toString());
            editor.putString(shared_subName,tvName.getText().toString());
            editor.putString(shared_key,sharepref.getString(shared_key,""));
            editor.commit();
            startActivity(new Intent(ViewGrade.this,RankActivity.class));
        }else{
            Snackbar.make(view,"No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("Action",null )
                    .show();
        }
    }

    public void clickRefViewGrade(View view){
        dialog.show();
        conectionStatus();
        if(isConnected){
            netLayout.setVisibility(View.GONE);
            runSync(sharedId, intentCode);
        }else{
            dialog.dismiss();
        }
    }


    //module for show the data
    protected void viewData(String ucuJSON){
        try{
            JSONObject jsonObje= new JSONObject(ucuJSON);
            info_stuGrade= jsonObje.getJSONArray(key_json);

            for(int i=0;i<info_stuGrade.length();i++) {
                JSONObject retObj = info_stuGrade.getJSONObject(i);
                String subCode = retObj.getString(key_subCode);
                String subName = retObj.getString(key_subName);
                String subShed = retObj.getString(key_subShed);
                String subSkulYear = retObj.getString(key_sy);
                String studGrade = retObj.getString(key_studGrade);
                String studPrevGrade = retObj.getString(key_prevGrade);
                String subIns= retObj.getString(key_subIns);
                String studRemark= retObj.getString(key_studRemark);

                double parGrade=Double.parseDouble(studGrade.toString());
                DecimalFormat decimalFormat= new DecimalFormat("#");
                String grade =decimalFormat.format(parGrade);
                String prevGrade=null;
                if(studPrevGrade.equals("--")){
                prevGrade="--";
                }else{
                    double parPrevGrade=Double.parseDouble(studPrevGrade.toString());
                    DecimalFormat deciPrevGrade= new DecimalFormat("#");
                    prevGrade =deciPrevGrade.format(parPrevGrade);
                }


                tvCode.setText(subCode);
                tvName.setText(subName);
                tvShed.setText(subShed);
                tvSkulYear.setText(subSkulYear);
                tvGrade.setText(grade);
                tvOrigGrade.setText(studGrade);
                tvPrevGrade.setText(prevGrade);
                tvRemark.setText(studRemark);
                tvsubIns.setText(subIns);

            }
           // String colorRemark=tvRemark.toString().toUpperCase().trim();
           // if(colorRemark.equals("PASSED")){

           // }






        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Module for JSON getData
    public void getStudData(){
        class GetStudGrade extends AsyncTask<String, Void, String> {
            ProgressDialog dialog;
            String shared_id=null;

            @Override
            protected void onPreExecute() {

               shared_id=sharepref.getString(shared_key,"");

                dialog = new ProgressDialog(ViewGrade.this);

                dialog.setMessage("Please Wait");
                dialog.show();

            }



            @Override
            protected String doInBackground(String... params) {
                String data_result=null;
                String line=null;

                String host=getResources().getString(R.string.serverhost);
                String urlQuery=host+"ucumobile/select_subCode.php?"+"studId="+shared_id
                        +"&"+"subCode="+intentCode;
                try{
                    URL url=new URL(urlQuery);
                    HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setConnectTimeout(2000);
                    httpURLConnection.setReadTimeout(2000);

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
                    data_result="Timeout";;
                } catch (IOException e) {
                    data_result="Timeout";
                }

                return data_result;
            }

            @Override
            protected void onPostExecute(String data_result) {
                dialog.dismiss();
                if(data_result.equals("Timeout")){
                    tvConnection.setText("Connection Timeout");
                    netLayout.setVisibility(View.VISIBLE);
                    viewLayout.setVisibility(View.GONE);
                }else{

                    viewData(data_result);
                    viewLayout.setVisibility(View.VISIBLE);
                }

            }
        }
        GetStudGrade grade= new GetStudGrade();
        grade.execute();
    }

    public void runSync(String id, String code){
        String server=getResources().getString(R.string.serverhost);
        viewGradeAsyncTask= (ViewGradeAsyncTask) new ViewGradeAsyncTask(new ViewGradeAsyncTask.AsyncResponse(){

            @Override
            public void processFinish(String response) {
                dialog.dismiss();
                if(response.equals("Timeout")){
                    viewLayout.setVisibility(View.GONE);
                    netLayout.setVisibility(View.VISIBLE);
                    tvConnection.setText("Connection Timeout");
                }else{
                    viewLayout.setVisibility(View.VISIBLE);
                    viewData(response);
                }


            }
        }).execute(id,code,server);

    }

    public void conectionStatus(){
        ConnectivityManager conStatus=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=conStatus.getActiveNetworkInfo();
        isConnected =netInfo!=null && netInfo.isConnectedOrConnecting();

    }

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
            Intent intent = new Intent(ViewGrade.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(code.equals("Post")){
            Intent intent = new Intent(ViewGrade.this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            SharedPreferences.Editor editor=autoLogin.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(ViewGrade.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }
}
