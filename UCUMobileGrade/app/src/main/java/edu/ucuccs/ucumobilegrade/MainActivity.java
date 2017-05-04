package edu.ucuccs.ucumobilegrade;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //Control
    TextView tvStudId,tvStudName,tvStudDegree,tvNoItem,tvConnection;
      RelativeLayout mainLayout,netLayout;
    ImageButton sheetSub,sheetPost;


    //Variables
    boolean isConnected;
    private  static  final  String key_json="studData";
    private  static  final  String key_subCode="subCode";
    private  static  final  String key_subName="subName";
    private  static  final  String key_subShed="subShed";
    private  static  final  String key_studName="studName";
    private  static  final  String key_studId="studId";
    private  static  final  String key_studDegree="studDegree";
    public static final String shared_key="id_key";
    public static final String logIn_Pass="pass_key";
    public static  final String shared_name="Student_ID";
    public static  final  String auto_loginKey="Auto_LogIn";
    String queryId,queryPass;



    //object
    JSONArray info_stuGrade=null;
    ArrayList<HashMap<String,String>> list_subjects=new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedPref,autoLogin;
    AlertDialog.Builder alertBuilder;
    SubjectAsyncTask subjectAsyncTask;
    ProgressDialog dialoga;

    static View.OnClickListener myOnClickListener;
    static  View.OnClickListener mySheetClickListener;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView.Adapter adapterSheet;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManagerSheet;
    private static RecyclerView recyclerView;
    private static RecyclerView recyclerViewSheet;
    private static ArrayList<SubjectGet> data;
    private static ArrayList<GetSheetMenu> dataSheet;

    private BottomSheetBehavior bottomSheetBehavior;

    MainBottomSheet mainBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ucu_logo);
         mainBottomSheet= new MainBottomSheet();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBottomSheet.show(getSupportFragmentManager(), mainBottomSheet.getTag());
            }
        });

        bottomSheetBehavior=BottomSheetBehavior.from(findViewById(R.id.sheetBottomMainLayout));
        //initialized Control


        tvStudId=(TextView) findViewById(R.id.txtStudId);
        tvStudName=(TextView) findViewById(R.id.txtStudName);
        tvStudDegree=(TextView)findViewById(R.id.txtStudDegree);
        tvConnection=(TextView)findViewById(R.id.txtConnection);
        tvNoItem=(TextView)findViewById(R.id.ViewEmpty);
        alertBuilder=new AlertDialog.Builder(MainActivity.this);
        sharedPref=getSharedPreferences(shared_name, Context.MODE_PRIVATE);
        autoLogin=getSharedPreferences(auto_loginKey, Context.MODE_PRIVATE);
            queryId=sharedPref.getString(shared_key, "");
            queryPass=sharedPref.getString(logIn_Pass,"");

        mainLayout=(RelativeLayout) findViewById(R.id.reLayoutGone);
         netLayout=(RelativeLayout) findViewById(R.id.netLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewMain);
        recyclerView.setHasFixedSize(true);
        recyclerViewSheet = (RecyclerView) findViewById(R.id.recycleViewSheetMenu);
        recyclerViewSheet.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManagerSheet= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewSheet.setLayoutManager(layoutManagerSheet);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSheet.setItemAnimator(new DefaultItemAnimator());


        dialoga= new ProgressDialog(this);
        dialoga.setMessage("Please Wait");
        dialoga.setCancelable(false);
        dialoga.setCanceledOnTouchOutside(false);


        dialoga.show();
        //Conditions
        connetionStatus();
        if(isConnected){

            runSubject(queryId);

        }else{
            dialoga.dismiss();

            mainLayout.setVisibility(View.GONE);
            tvConnection.setText("No Internet Connection");
            netLayout.setVisibility(View.VISIBLE);


        }










        mySheetClickListener= new MySheetOnClickListener(this);
        myOnClickListener = new MyOnClickListener(this);
    }
    //Recycle Subjects
    public class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {

            this.context = context;
        }

        @Override
        public void onClick(View v) {

            selectItem(v);
        }

        private void selectItem(View v) {
            String code =((TextView)v.findViewById(R.id.txtCode)).getText().toString();
            Intent intent= new Intent(context,ViewGrade.class);
            intent.putExtra("courseCde",code.toString().trim());
            startActivity(intent);

        }
    }
    //Recycle Subjects
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
         mainBottomSheet.dismiss();
        }else if(code.equals("Post")){
            Intent intent = new Intent(MainActivity.this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(code.equals("Password")){
            Intent intent = new Intent(MainActivity.this, ChangePassword.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            SharedPreferences.Editor editor=autoLogin.edit();
            editor.clear();
            editor.commit();

        }


    }
    //Recycle SheetMenu

    //Button
    public void clickRefMainLayout(View view){
        dialoga.show();
        connetionStatus();
        if(isConnected){
            runSubject(queryId);
        }else{
            dialoga.dismiss();
           tvConnection.setText("No Internet Connection");
        }


    }



    //Start Classes
    protected void viewData(String ucujson){
        try{

                JSONObject jsonObje= new JSONObject(ucujson);
                info_stuGrade= jsonObje.getJSONArray(key_json);
            data= new ArrayList<>();
                //Looping for Read Data
                for(int i=0;i<info_stuGrade.length();i++){
                    JSONObject retObj=info_stuGrade.getJSONObject(i);
                   data.add(new SubjectGet(
                             retObj.getString(key_subName),
                   retObj.getString(key_subCode),
                    retObj.getString(key_subShed)

                    ));
                    String studName= retObj.getString(key_studName);
                    String studId= retObj.getString(key_studId);
                    String studDegree=retObj.getString(key_studDegree);

                    tvStudId.setText(studId);

                    tvStudName.setText(studName);
                    tvStudDegree.setText(studDegree);



                }

                adapter=new AdapterSubjects(data);
            recyclerView.setAdapter(adapter);

            if((tvStudId.getText()).equals("No Data")){
                netLayout.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
                tvConnection.setText("No Data Available");
            }

            String splitName[]= (tvStudName.getText().toString()).split(",");
            if((splitName[0].toString().toLowerCase()).equals(queryPass.toString())){
                changePassword();
            }else{

            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

        //Module for JSON getData
        public void runSubject(String studId){
            String server=getResources().getString(R.string.serverhost);
            subjectAsyncTask= (SubjectAsyncTask) new SubjectAsyncTask(new SubjectAsyncTask.AsyncResponse(){

                @Override
                public void processFinish(String result) {
                    dialoga.dismiss();
                    if(result.equals("Timeout")){
                        mainLayout.setVisibility(View.GONE);
                        netLayout.setVisibility(View.VISIBLE);
                        tvConnection.setText("Connection Timeout");
                    }else{
                        mainLayout.setVisibility(View.VISIBLE);
                        netLayout.setVisibility(View.GONE);
                        viewData(result);
                    }


                }
            }).execute(studId,server);

        }

        public void connetionStatus(){
            ConnectivityManager conStatus=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo=conStatus.getActiveNetworkInfo();
            isConnected=netInfo !=null && netInfo.isConnectedOrConnecting();
        }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {  alertBuilder.setMessage("Abort now?");
            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertExe = alertBuilder.create();
            alertExe.show();


        }
        return super.onKeyDown(keyCode, event);
    }

    public void changePassword(){
        //ChangePassword
        AlertDialog.Builder alertBuild= new AlertDialog.Builder(this);
        alertBuild.setMessage("Change Password for Security");
        alertBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, ChangePassword.class));
            }
        });

        AlertDialog alertDialog= alertBuild.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();




        //ChangePassword

    }

}
