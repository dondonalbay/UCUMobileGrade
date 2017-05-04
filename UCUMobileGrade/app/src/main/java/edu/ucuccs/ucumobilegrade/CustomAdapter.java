package edu.ucuccs.ucumobilegrade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;



/**
 * Created by Admin on 6/15/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private ArrayList<PostData> dataSet;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news;
        TextView name;
        TextView postDate;
        TextView postTime;

        public MyViewHolder(View itemView){
            super(itemView);
            this.news=(TextView) itemView.findViewById(R.id.txtName);
            this.name=(TextView) itemView.findViewById(R.id.txtPost);
            this.postDate=(TextView)itemView.findViewById(R.id.txtPostDate);
            this.postTime=(TextView)itemView.findViewById(R.id.txtPostTime);


        }

    }

    public CustomAdapter(ArrayList<PostData> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_news,parent,false);
        view.setOnClickListener(NewsActivity.myOnClickListener) ;

        MyViewHolder myViewHolder= new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        TextView news=holder.news;
        TextView name=holder.name;
        TextView postDate=holder.postDate;
        TextView postTime=holder.postTime;

        news.setText(dataSet.get(listPosition).getPost());
        name.setText(dataSet.get(listPosition).getName());
        postDate.setText(dataSet.get(listPosition).getPostDate());
        postTime.setText(dataSet.get(listPosition).getPostTime());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
