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
public class AdapterSubjects extends RecyclerView.Adapter<AdapterSubjects.MyViewHolder>{
    private ArrayList<SubjectGet> dataSet;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        TextView subjectCode;
        TextView subjectShed;


        public MyViewHolder(View itemView){
            super(itemView);
            this.subjectName=(TextView) itemView.findViewById(R.id.txtSubName);
            this.subjectCode=(TextView) itemView.findViewById(R.id.txtCode);
            this.subjectShed=(TextView)itemView.findViewById(R.id.txtShed);



        }

    }

    public AdapterSubjects(ArrayList<SubjectGet> data) {

        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_subject,parent,false);
        view.setOnClickListener(MainActivity.myOnClickListener) ;
        MyViewHolder myViewHolder= new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        TextView subjectName=holder.subjectName;
        TextView subjectCode=holder.subjectCode;
        TextView subjectShed=holder.subjectShed;


        subjectName.setText(dataSet.get(listPosition).getSubjectName());
        subjectCode.setText(dataSet.get(listPosition).getSubjectCode());
        subjectShed.setText(dataSet.get(listPosition).getSubjectShed());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
