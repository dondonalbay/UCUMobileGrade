package edu.ucuccs.ucumobilegrade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    //Button
    public void clickImageSubjects(View view){
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
    }
    public void clickImagePost(View view){
        startActivity(new Intent(MenuActivity.this,NewsActivity.class));

    }

}
