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



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 6/20/2016.
 */
public class OverallRankingFragment extends Fragment {


    //Variables

    private static final String key_json = "studData";
    private static final String key_studRank = "studRank";
    private static final String key_studName = "studName";
    private static final String key_studGrade = "studGrade";


    //object
    JSONArray info_stuGrade = null;
    RankingAsyncTaskOverAll rankingAsyncTask;


    Context context;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
    private static RecyclerView recyclerView;
    private static ArrayList<RankingGetOverall> data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewCons = inflater.inflate(R.layout.overall_ranking_layout, container, false);
        recyclerView = (RecyclerView) viewCons.findViewById(R.id.recycleViewRankingOverAll);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return viewCons;


    }

    public void viewData(String ucuJSON) {
        try {
            JSONObject jsonObje = new JSONObject(ucuJSON);
            info_stuGrade = jsonObje.getJSONArray(key_json);
            data = new ArrayList<>();

            //Looping for Read Data
            for (int i = 0; i < info_stuGrade.length(); i++) {
                JSONObject retObj = info_stuGrade.getJSONObject(i);
                data.add(new RankingGetOverall(
                        retObj.getString(key_studName),
                        retObj.getString(key_studGrade),
                        retObj.getString(key_studRank)

                ));


                adapter = new AdapterRankingOverAll(data);
                recyclerView.setAdapter(adapter);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
