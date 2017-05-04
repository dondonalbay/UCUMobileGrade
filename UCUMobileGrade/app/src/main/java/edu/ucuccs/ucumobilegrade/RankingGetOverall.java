package edu.ucuccs.ucumobilegrade;

/**
 * Created by Admin on 6/20/2016.
 */
public class RankingGetOverall {
    String name;
    String grade;
    String rank;

    public RankingGetOverall(String name, String grade, String rank ){
        this.name=name;
        this.grade=grade;
        this.rank=rank;
    }

    public String getName(){
        return name;
            }
    public String getGrade(){
        return grade;
    }
    public String getRank(){
        return rank;
    }
}
