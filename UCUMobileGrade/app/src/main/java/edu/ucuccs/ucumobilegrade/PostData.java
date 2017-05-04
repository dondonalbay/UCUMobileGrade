package edu.ucuccs.ucumobilegrade;

/**
 * Created by Admin on 6/15/2016.
 */
public class PostData {
    String post;
    String name;
    String postTime;
    String postDate;


    public PostData(String post, String name, String postTime, String postDate) {
        this.post = post;
        this.name=name;
        this.postTime=postTime;
        this.postDate=postDate;



    }


    public String getPost(){
        return post;
    }
    public String getName(){
        return name;

    }
    public  String getPostTime(){
        return postTime;
    }
    public String getPostDate(){
        return postDate;
    }

}
