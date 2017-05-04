package edu.ucuccs.ucumobilegrade;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;

/**
 * Created by Admin on 6/21/2016.
 */
public class PostBottomSheet extends BottomSheetDialogFragment {



    private static RecyclerView.Adapter adapterSheet;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManagerSheet;
    private static RecyclerView recyclerView;
    private static RecyclerView recyclerViewSheet;
    private static ArrayList<SubjectGet> data;
    private static ArrayList<GetSheetMenu> dataSheet;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.sheet_main,container,false);
        Context context = null;
        recyclerViewSheet = (RecyclerView) v.findViewById(R.id.recycleViewSheetMenu);
        recyclerViewSheet.setHasFixedSize(true);


        layoutManagerSheet= new LinearLayoutManager(context);

        recyclerViewSheet.setLayoutManager(layoutManagerSheet);

        recyclerViewSheet.setItemAnimator(new DefaultItemAnimator());

        dataSheet=new ArrayList<>();
        for(int s =0; s < SheetMenuData.sheetImageArray.length ; s++){
            dataSheet.add(new GetSheetMenu(
                    SheetMenuData.sheetNameArray[s],
                    SheetMenuData.sheetImageArray[s]

            ));


        }
        adapterSheet= new SheetAdapterPost(dataSheet);
        recyclerViewSheet.setAdapter(adapterSheet);
        return v;

    }


}
