package model;

import javafx.scene.control.Button;

import java.util.List;

public class categorie {
    public  List<Button> catButtonList;
    public  String categorie;
    public  List<Button> getCatButtonList() {
        return catButtonList;
    }

    public categorie() {

    }
    public categorie(List<Button>  catButtonList,String categorie) {
        this.catButtonList = catButtonList;

        this.categorie = categorie;
    }

    public  String getCategorie() {
        return categorie;
    }
}
