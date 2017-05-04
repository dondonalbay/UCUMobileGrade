package edu.ucuccs.ucumobilegrade;

/**
 * Created by Admin on 6/20/2016.
 */
public class SubjectGet {
    String subjectName;
    String subjectCode;
    String subjectShed;

    public SubjectGet(String subjectName, String subjectCode, String subjectShed){
        this.subjectName=subjectName;
        this.subjectCode= subjectCode;
        this.subjectShed=subjectShed;
    }
    public String getSubjectName(){
        return subjectName;
    }
    public String getSubjectCode(){
        return subjectCode;
    }

    public String getSubjectShed(){
        return subjectShed;
    }
}
