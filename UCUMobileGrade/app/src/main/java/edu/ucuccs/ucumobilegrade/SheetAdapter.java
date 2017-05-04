package edu.ucuccs.ucumobilegrade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Admin on 6/21/2016.
 */
public class SheetAdapter  extends RecyclerView.Adapter<SheetAdapter.MyViewHolder>{
    private ArrayList<GetSheetMenu> dataSet;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sheetTextView;
        ImageView sheetImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.sheetImage=(ImageView) itemView.findViewById(R.id.sheetImageView);
            this.sheetTextView=(TextView)itemView.findViewById(R.id.sheetTextView);
        }
    }
    public SheetAdapter(ArrayList<GetSheetMenu> data){

        this.dataSet = data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sheet_list,parent, false);
        view.setOnClickListener(MainActivity.mySheetClickListener);
        MyViewHolder myViewHolder= new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView sheetTextView=holder.sheetTextView;
        ImageView sheetImageView=holder.sheetImage;

        sheetTextView.setText(dataSet.get(position).getSheetMenuName());
        sheetImageView.setImageResource(dataSet.get(position).getSheetMenuImage());



    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
