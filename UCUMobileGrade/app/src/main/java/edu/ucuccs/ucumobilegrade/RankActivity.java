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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RankActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPagers ;

    public static final String shared_Id="id_key";
    public static final String shared_shed="shed_key";
    public static final String shared_code="code_key";
    public static final String shared_subName="subName_key";
    public static  final String shared_rank="Student_Rank";
    public static  final  String auto_loginKey="Auto_LogIn";
    String shId=null;
    String shShed=null;
    String shCode=null;
    String shSubname=null;
    SharedPreferences sharedPref;
    boolean isConnected;

    RankingAsyncTask rankingAsyncTask;
    BlockRankingFragment blockRankingFragment;
    OverallRankingFragment overallRankingFragment;
    ProgressDialog dialog;
    String urlBlock=null;
    BottomSheetBehavior bottomSheetBehavior;
    static  View.OnClickListener mySheetClickListener;
    SharedPreferences autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ucu_logo);
        final RankBottomSheet rankBottomSheet= new RankBottomSheet();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankBottomSheet.show(getSupportFragmentManager(), rankBottomSheet.getTag());

            }
        });
        bottomSheetBehavior= BottomSheetBehavior.from(findViewById(R.id.sheetBottomMainLayout));
        dialog= new ProgressDialog(this);
        dialog.setMessage("Please wait");
        autoLogin=getSharedPreferences(auto_loginKey, Context.MODE_PRIVATE);
                sharedPref=getSharedPreferences(shared_rank,Context.MODE_PRIVATE);

        shId=sharedPref.getString(shared_Id,"");
        shCode=sharedPref.getString(shared_code,"");
        shShed=sharedPref.getString(shared_shed,"");
        shSubname=sharedPref.getString(shared_subName,"");


        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabsRankingBlock);

        tabLayout.addTab(tabLayout.newTab().setText("Block Ranking"));
        tabLayout.addTab(tabLayout.newTab().setText("Overall Ranking"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setIcon(R.drawable.block);
        tabLayout.getTabAt(1).setIcon(R.drawable.overall);

        viewPagers=(ViewPager)findViewById(R.id.viewpagerRankingBlock);



        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPagers.setAdapter(adapter);
        viewPagers.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);

        String server=getResources().getString(R.string.serverhost);

            //Classes
            blockRankingFragment=new BlockRankingFragment();
            overallRankingFragment= new OverallRankingFragment();
        try {

            urlBlock = server+"ucumobile/rankingBlock.php?"+"subCode="+ URLEncoder.encode(shCode.toString(),"UTF-8")
                    +"&"+"subShed="+ URLEncoder.encode(shShed.toString(),"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
            connetionStatus();
            if(isConnected){
                runRankingBlock(urlBlock);
            }else{
                dialog.dismiss();

            }









    mySheetClickListener= new MySheetOnClickListener(this);

    }
    //Classes

    public void connetionStatus(){
        ConnectivityManager conStatus=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=conStatus.getActiveNetworkInfo();
        isConnected=netInfo !=null && netInfo.isConnectedOrConnecting();
    }

    public void runRankingBlock(final String urlRanking) {
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        rankingAsyncTask = (RankingAsyncTask) new RankingAsyncTask(new RankingAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String outputBlock) {
                if(outputBlock.equals("Timeout")){
                    alert(urlRanking);
                    dialog.dismiss();

                }else{
                    String server=getResources().getString(R.string.serverhost);
                    blockRankingFragment.viewData(outputBlock);
                    String urlOverAll = null;
                    try {
                        urlOverAll = server+"ucumobile/rankingAll.php?"+"subName="+ URLEncoder.encode(shSubname.toString(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    runRankingOver(urlOverAll);
                }

            }
        }).execute(urlRanking);
    }
    public void runRankingOver(String urlRankingOver){
        rankingAsyncTask=(RankingAsyncTask) new RankingAsyncTask(new RankingAsyncTask.AsyncResponse(){

            @Override
            public void processFinish(String outputOverAll) {
                dialog.dismiss();
                overallRankingFragment.viewData(outputOverAll);
            }
        }).execute(urlRankingOver);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPagers.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        viewPagers.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public void alert(final String urlRetry){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Network Error");
        builder.setMessage("The network request has timed out");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                runRankingBlock(urlRetry);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert= builder.create();

        alert.show();
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
            Intent intent = new Intent(RankActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(code.equals("Post")){
            Intent intent = new Intent(RankActivity.this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(code.equals("Password")){
            Intent intent = new Intent(RankActivity.this, ChangePassword.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


        else{
            SharedPreferences.Editor editor=autoLogin.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(RankActivity.this, LogInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }
}
