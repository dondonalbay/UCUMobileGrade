package edu.ucuccs.ucumobilegrade;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogInActivity extends AppCompatActivity {
    //Control
    EditText studId,studPass;
    TextInputLayout inputId, inputPass;
    //Variables
    boolean auto_log,isConneted;
    public   static  final String studId_key="Student_ID";
    public static  final  String auto_login="Auto_LogIn";
    public static  final String logIn_Id="id_key";
    public static final String logIn_Pass="pass_key";
    String id=null;
    String pass=null;
    String dataScanner=null;
    //Objects
    SharedPreferences sharedLogin,autoSharedLogin;
    AlertDialog.Builder alert;
    ConnectivityManager conStaus;
    NetworkInfo activeNetwork;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        studId=(EditText)findViewById(R.id.logstudId);
        studPass=(EditText)findViewById(R.id.logstudPass);
        inputId=(TextInputLayout)findViewById(R.id.inputId);
        inputPass=(TextInputLayout)findViewById(R.id.inputPass);
        sharedLogin=getSharedPreferences(studId_key, Context.MODE_PRIVATE);
        autoSharedLogin=getSharedPreferences(auto_login, Context.MODE_PRIVATE);
        alert= new AlertDialog.Builder(LogInActivity.this);


        connetionStatus();
        if(isConneted){


        }else{
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            AlertDialog alertExe = alert.create();
            alertExe.show();

        }

        String getAuto_LogId = autoSharedLogin.getString(logIn_Id, "");
        String getAuto_LogPass = autoSharedLogin.getString(logIn_Pass, "");
        if(getAuto_LogId.equals("")){
            clearTextBox();

        }else{
            studId.setText(getAuto_LogId);
            studPass.setText(getAuto_LogPass);

            startActivity(new Intent(LogInActivity.this, MainActivity.class));

        }



    }


    //Scanner
 /*  public void scanQR() {
        IntentIntegrator integrator = new IntentIntegrator(LogInActivity.this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (scanResult != null) {

                dataScanner=scanResult.getContents();
                LogIn();


            }
        } else if (resultCode == RESULT_CANCELED) {

        }
    }
*/
    //Scanner



    //Start Click
    //Button Login
   public void clickLogin(View view){
     LogIn();

   }
    //Forgot Password
    public void clickForgotPass(View view){

    }
    //End Click
//Start of Classes
    public void LogIn() {
        connetionStatus();
    if(isConneted){
        signIn();

    }else{
        alert.setMessage("No Internet Connection");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog alertExe = alert.create();
        alertExe.show();

    }

    }

        public void signIn(){
            class GetLogIn extends AsyncTask<String, Void, String>{
                ProgressDialog dialog;
                SharedPreferences.Editor editor=sharedLogin.edit();

                @Override
                protected void onPreExecute() {
                    id=studId.getText().toString().trim();
                    pass=studPass.getText().toString().trim();
                    dialog = new ProgressDialog(LogInActivity.this);

                    dialog.setMessage("Connecting");
                    dialog.show();
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                }


                @Override
                protected String doInBackground(String... strings) {
                    String data_result=null;
                    try{


                    String studId=id.toString().trim();
                    String studPassword=pass.toString().trim();
                        String host=getResources().getString(R.string.serverhost);
                    String urlQuery=host+"ucumobile/login.php?"+"studId="+studId
                                +"&"+"studPassword="+studPassword;





                        URL url=new URL(urlQuery);
                        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                        httpURLConnection.setReadTimeout(20000);
                        httpURLConnection.setConnectTimeout(20000);
                        InputStream inputStream=httpURLConnection.getInputStream();
                        int conResponse=httpURLConnection.getResponseCode();
                        if(conResponse==httpURLConnection.HTTP_OK){
                            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
                            StringBuilder stringBuilder=new StringBuilder();
                            String data_line=null;
                            while ((data_line=bufferedReader.readLine())!=null){
                                stringBuilder.append(data_line +"\n");


                            }
                            bufferedReader.close();
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
                    String vali=null;
                    vali=data_result.toString().trim();


                        if(vali.toString().equals("ok")){
                            alert.setMessage("Remember Log In?");
                            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auto_log=true;
                                    sharedPref(auto_log);

                                }
                            });
                            alert.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auto_log=false;
                                    sharedPref(auto_log);
                                }
                            });
                            AlertDialog alertExe = alert.create();
                            alertExe.show();




                        }else if(vali.toString().equals("Timeout")){

                            alert.setMessage("Connetion Timeout");
                            alert.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LogIn();
                                }
                            });
                            AlertDialog alertExe = alert.create();
                            alertExe.show();

                        }else{
                            alert.setMessage("Problem to Log In");
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    clearTextBox();
                                }
                            });
                            AlertDialog alertExe = alert.create();
                            alertExe.show();
                    }

                        dialog.dismiss();



                        return;

                }



            }
            GetLogIn log= new GetLogIn();
            log.execute();
        }

    //SharedPref
    public void sharedPref(Boolean auto){
        SharedPreferences.Editor editorId= sharedLogin.edit();
        SharedPreferences.Editor editorLog= autoSharedLogin.edit();
        if(auto){

            editorLog.clear();
            editorLog.putString(logIn_Id,id.toString());
            editorLog.putString(logIn_Pass, pass.toString());
            editorLog.commit();

            editorId.clear();

            editorId.putString(logIn_Id,id.toString());
            editorId.putString(logIn_Pass,pass.toString());
            editorId.commit();
            clearTextBox();
            startActivity(new Intent(LogInActivity.this,MainActivity.class));

        }else {

            editorId.clear();
            editorLog.clear();
            editorId.putString(logIn_Id,id.toString());
            editorId.putString(logIn_Pass,pass.toString());

            editorId.commit();


            String getPref = sharedLogin.getString(logIn_Id, "");
            Toast.makeText(getApplicationContext(),getPref,Toast.LENGTH_LONG).show();

            clearTextBox();
            startActivity(new Intent(LogInActivity.this,MainActivity.class));

        }


    }

    public void clearTextBox(){
        studId.setText("");
        studPass.setText("");
    }

    public void connetionStatus(){
        conStaus=(ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork=conStaus.getActiveNetworkInfo();
        isConneted=activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
    }


    //End of Classes

}
