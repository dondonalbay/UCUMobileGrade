package edu.ucuccs.ucumobilegrade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Admin on 6/20/2016.
 */
public class AdapterRanking extends RecyclerView.Adapter<AdapterRanking.MyViewHolder> {
    private ArrayList<RankingGet> dataSet;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
            TextView name;
            TextView grade;
            TextView rank;
            TextView txtThug;
            ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);

            this.name=(TextView)itemView.findViewById(R.id.textStudName);
            this.grade=(TextView)itemView.findViewById(R.id.txtGrade);
            this.rank=(TextView)itemView.findViewById(R.id.txtRank);
            this.txtThug=(TextView)itemView.findViewById(R.id.txtThug);
            this.imageView=(ImageView)itemView.findViewById(R.id.imgRank);
        }
    }
    public AdapterRanking(ArrayList<RankingGet> data){
        this.dataSet=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_ranking,parent,false);


        MyViewHolder myViewHolder =new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView name=holder.name;
        TextView grade=holder.grade;
        TextView rank=holder.rank;
        TextView txtThug=holder.txtThug;
        ImageView imageView=holder.imageView;
        String finalGrade;

        name.setText(dataSet.get(position).getName());
        grade.setText("");
        finalGrade=(dataSet.get(position).getGrade());
        double parGrade=Double.parseDouble(finalGrade);
        DecimalFormat decimalFormat= new DecimalFormat("#");
        Integer gradeFake = Integer.valueOf(decimalFormat.format(parGrade));

        if(gradeFake<=100 && gradeFake>=96){
            imageView.setImageResource(R.drawable.like);
            txtThug.setText(" ");
        }else if(gradeFake<96 && gradeFake>=90){
            imageView.setImageResource(R.drawable.like);
            txtThug.setText(" ");
        }else if(gradeFake<90 && gradeFake>=86){
            imageView.setImageResource(R.drawable.like);
            txtThug.setText(" ");
        }else if(gradeFake<86 && gradeFake>=80){
            imageView.setImageResource(R.drawable.like);
            txtThug.setText(" ");
        }else if(gradeFake<80 && gradeFake>=76){
            imageView.setImageResource(R.drawable.like);
            txtThug.setText(" ");
        }else if(gradeFake==75){
            imageView.setImageResource(R.drawable.like);
            txtThug.setText(" ");
        }else if(gradeFake<75){
            imageView.setImageResource(R.drawable.dislike);
            txtThug.setText(" ");

        }

        rank.setText("Rank: "+dataSet.get(position).getRank());




    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
