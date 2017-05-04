package edu.ucuccs.ucumobilegrade;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class ChangePassword extends AppCompatActivity {
    ChangePasswordTask  changePasswordTask;
    EditText passNew, passRetry;
    SharedPreferences studpref,autoSharedLogin;
    ProgressDialog dialog;
    AlertDialog.Builder alertBuild, alertBuilds;

    //Variables
    public static  final  String auto_login="Auto_LogIn";
    public static final String key_pref="Student_ID";
    public static  final String logIn_Id="id_key";
    public static final String logIn_Pass="pass_key";
    String sharedId=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ucu_logo);


        autoSharedLogin=getSharedPreferences(auto_login, Context.MODE_PRIVATE);
        studpref=getSharedPreferences(key_pref, Context.MODE_PRIVATE);
        sharedId=studpref.getString(logIn_Id, "");
        passNew=(EditText)findViewById(R.id.changePasswordNew);
        passRetry=(EditText)findViewById(R.id.changePasswordRetry);
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setMessage("Please Wait");

    }
    //Button
    public void clickChangePassword(View view){
        if(passNew.getText().toString().contains(" ") || passNew.getText().toString().equals("")){
            passNew.setError("Can't accept space and empty");
        }else if(passRetry.getText().toString().contains(" ") || passRetry.getText().toString().equals("")){
            passRetry.setError("Can't accept space and empty");
        }else{
            changefile();
        }






    }
    public void runChange(final String id, final String pass, String url){
         alertBuilds=new AlertDialog.Builder(this);
        changePasswordTask= (ChangePasswordTask) new ChangePasswordTask(new ChangePasswordTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                dialog.dismiss();
                if(output.equals("Timeout")){

                   alertBuilds.setCancelable(false);
                    alertBuilds.setMessage("Connection Timeout");
                    alertBuilds.setTitle("Network Error");
                   alertBuilds.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           changefile();
                       }
                   });
                    AlertDialog alert=alertBuilds.create();
                    alert.show();
                }else{
                    SharedPreferences.Editor editor=studpref.edit();
                    SharedPreferences.Editor editorAuto=autoSharedLogin.edit();
                    editorAuto.clear();
                    editor.clear();
                    editor.putString(logIn_Id,id.toString());
                    editor.putString(logIn_Pass,pass.toString());
                    editorAuto.putString(logIn_Id,id.toString());
                    editorAuto.putString(logIn_Pass,pass.toString());
                    editor.commit();
                    editorAuto.commit();
                    Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }


            }
        }).execute(id,pass,url);

    }
    public void changefile(){
        String url=getResources().getString(R.string.serverhost);
        String id=sharedId.toString();
        String passnew=passNew.getText().toString();
        final String pasRetry=passRetry.getText().toString();
        if(pasRetry.toString().equals(passnew.toString())){
            dialog.show();
            runChange(id,pasRetry,url);
        }else{
            alertBuild= new AlertDialog.Builder(this);
            alertBuild.setCancelable(false);

            alertBuild.setMessage("Password not match");
            alertBuild.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    passRetry.setText("");
                    passRetry.setFocusable(true);
                }
            });
            AlertDialog alertDialog=alertBuild.create();
            alertDialog.show();
        }

    }



}
