package edu.ucuccs.ucumobilegrade;

/**
 * Created by Admin on 6/21/2016.
 */
public class GetSheetMenu {
    String sheetMenuName;
    int sheetMenuImage;


    public GetSheetMenu(String sheetMenuName, int sheetMenuImage){
        this.sheetMenuName=sheetMenuName;
        this.sheetMenuImage=sheetMenuImage;

    }

    public String getSheetMenuName(){
        return sheetMenuName;
    }
    public int getSheetMenuImage(){
        return sheetMenuImage;
    }

}
