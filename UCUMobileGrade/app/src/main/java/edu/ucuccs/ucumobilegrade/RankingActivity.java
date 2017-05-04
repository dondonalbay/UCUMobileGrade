package edu.ucuccs.ucumobilegrade;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankingActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout linearLayoutLayout;
    RelativeLayout relaRanking,relaRankingConnection;
    TextView tvOwnRank,tvGrade,tvTitleRanking,tvConnection;
    //Variables
    boolean isConnected;
    private  static  final  String key_json="studData";
    private  static  final  String key_studRank="studRank";
    private  static  final  String key_studName="studName";
    private  static  final  String key_studGrade="studGrade";
    private  static  final  String key_ownRank="ownRank";

    public static final String shared_Id="id_key";
    public static final String shared_shed="shed_key";
    public static final String shared_code="code_key";
    public static final String shared_subName="subName_key";
    public static  final String shared_rank="Student_Rank";

    String shId=null;
    String shShed=null;
    String shCode=null;
    String shSubName=null;
    //object
    JSONArray info_stuGrade=null;
    ArrayList<HashMap<String,String>> list_ranking=new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedPref;
    RankingAsyncTask rankingAsyncTask;

    static View.OnClickListener myOnClickListener;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<RankingGet> data;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(RankingActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        sharedPref=getSharedPreferences(shared_rank, Context.MODE_PRIVATE);

        tvOwnRank=(TextView)findViewById(R.id.txtStudNameRanking);
        tvGrade=(TextView)findViewById(R.id.txtGradeRanking);
        tvConnection=(TextView)findViewById(R.id.txtConnectionRanking);
        tvTitleRanking=(TextView)findViewById(R.id.txtListRank);
        relaRanking=(RelativeLayout)findViewById(R.id.relaRanking);
        relaRankingConnection=(RelativeLayout)findViewById(R.id.relaRankingConnection);
        shId=sharedPref.getString(shared_Id,"");
        shCode=sharedPref.getString(shared_code,"");
        shShed=sharedPref.getString(shared_shed,"");
        shSubName=sharedPref.getString(shared_subName,"");


        linearLayoutLayout=(LinearLayout) findViewById(R.id.rankLayout);

        recyclerView=(RecyclerView)findViewById(R.id.recycleViewRanking);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpagerRanking);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabsRanking);
        tabLayout.setupWithViewPager(viewPager);


        connectionStatus();
        if(isConnected){
            try {
                String urk="http://10.0.3.2/ucumobile/rankingBlock.php?"+"subCode="+ URLEncoder.encode(shCode.toString(),"UTF-8")
                        +"&"+"subShed="+ URLEncoder.encode(shShed.toString(),"UTF-8");


                    runRanking(urk);



                relaRanking.setVisibility(View.VISIBLE);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            relaRanking.setVisibility(View.GONE);
            relaRankingConnection.setVisibility(View.VISIBLE);
            tvConnection.setText("No Internet Connection");
        }


    }



    //click Button
    public void clickRetryRanking(View view){
        clickBlock(view);
    }

    public void clickOverAll(View view)  {
        connectionStatus();
        if(isConnected){
            String urlOverAll= null;
            try {
                urlOverAll = "http://10.0.3.2/ucumobile/rankingAll.php?"+"subName="+ URLEncoder.encode(shSubName,"UTF-8");
                tvTitleRanking.setText("OverAll Ranking");
                list_ranking.clear();
                runRanking(urlOverAll);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else{
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("Action",null)
                    .show();
        }


    }
    public void clickBlock(View view){
            connectionStatus();
            if(isConnected){
                String urlBlock= null;
                try {
                    urlBlock = "http://10.0.3.2/ucumobile/rankingAll.php?"+"subCode="+ URLEncoder.encode(shCode,"UTF-8");
                    tvTitleRanking.setText("OverAll Ranking");
                    list_ranking.clear();
                    runRanking(urlBlock);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }else{
                Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                        .setAction("Action",null)
                        .show();
            }

    }
    //Start of Method
    //module for show the data
    public void viewData(String ucuJSON){
        try{
            JSONObject jsonObje= new JSONObject(ucuJSON);
            info_stuGrade= jsonObje.getJSONArray(key_json);
            data= new ArrayList<>();
            String studName;
            String studGrade;
            String studRank;
            //Looping for Read Data
            for(int i=0;i<info_stuGrade.length();i++){
                JSONObject retObj=info_stuGrade.getJSONObject(i);
                data.add(new RankingGet(
                        studName= retObj.getString(key_studName),
                        studGrade=retObj.getString(key_studGrade),
                        studRank= retObj.getString(key_studRank)

                ));

                String ownRank= retObj.getString(key_ownRank);

                if(ownRank.equals( shId.toString())){
                    tvOwnRank.setText(studRank);
                    tvGrade.setText(studGrade);


                }




                adapter= new AdapterRanking(data);
                recyclerView.setAdapter(adapter);



            }





        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Module for JSON getData
    public void getStudRanking(final String getUrl){

        class GetStudGrade extends AsyncTask<String, Void, String > {
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = new ProgressDialog(RankingActivity.this);
                dialog.setMessage("Please Wait");
                dialog.show();
            }
            @Override
            protected String doInBackground(String... string) {
                String data_result=null;
                String line=null;
                String urlQuery=string.toString().trim();
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
                     data_result="Timeout";
                 } catch (IOException e) {
                     data_result="Timeout";
                 }

                return data_result;
            }

            @Override
            protected void onPostExecute(String data_result) {
                dialog.dismiss();
                if(data_result.equals("Timeout")){
                    relaRanking.setVisibility(View.GONE);
                    relaRankingConnection.setVisibility(View.VISIBLE);
                    tvConnection.setText("Connection Timeout");

                }else{

                    viewData(data_result);
                    relaRanking.setVisibility(View.VISIBLE);
                    relaRankingConnection.setVisibility(View.GONE);

                }




            }
        }
        GetStudGrade grade= new GetStudGrade();
        grade.execute();
    }

    public void connectionStatus(){
        ConnectivityManager conStatus=(ConnectivityManager)getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=conStatus.getActiveNetworkInfo();
        isConnected=netInfo!=null && netInfo.isConnectedOrConnecting();


    }
    //End Of Method

    public void runRanking(final String urlRanking){
        rankingAsyncTask = (RankingAsyncTask) new RankingAsyncTask(new RankingAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                BlockRankingFragment blockRankingFragment= new BlockRankingFragment();
                blockRankingFragment.viewData(urlRanking);
            }
        }).execute(urlRanking);
    }


    public void setupViewPager(ViewPager upViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BlockRankingFragment(), "Block Ranking");
        adapter.addFragment(new OverallRankingFragment(), "OverAll Ranking");


        viewPager.setAdapter(adapter);
        this.viewPager = upViewPager;
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }
}
