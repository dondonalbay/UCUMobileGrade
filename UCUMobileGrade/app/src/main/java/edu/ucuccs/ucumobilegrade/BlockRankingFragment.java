package edu.ucuccs.ucumobilegrade;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 6/20/2016.
 */
public class BlockRankingFragment extends Fragment {


    //Variables

    private static final String key_json = "studData";
    private static final String key_studRank = "studRank";
    private static final String key_studName = "studName";
    private static final String key_studGrade = "studGrade";


    //object
    JSONArray info_stuGrade = null;
    RankingAsyncTask rankingAsyncTask;
    ImageView imageRank;

    Context context;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
    private static RecyclerView recyclerView;
    private static ArrayList<RankingGet> data;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewCon = inflater.inflate(R.layout.block_ranking_layout, container, false);

        imageRank=(ImageView)viewCon.findViewById(R.id.imgRank);
        recyclerView = (RecyclerView) viewCon.findViewById(R.id.recycleViewRankingBlock);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return viewCon;


    }

    public void viewData(String ucuJSON) {
        try {
            JSONObject jsonObje = new JSONObject(ucuJSON);
            info_stuGrade = jsonObje.getJSONArray(key_json);
            data = new ArrayList<>();
            String grade;
            //Looping for Read Data
            for (int i = 0; i < info_stuGrade.length(); i++) {
                JSONObject retObj = info_stuGrade.getJSONObject(i);
                data.add(new RankingGet(
                        retObj.getString(key_studName),
                        grade=retObj.getString(key_studGrade),
                        retObj.getString(key_studRank)

                ));


                adapter = new AdapterRanking(data);
                recyclerView.setAdapter(adapter);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
