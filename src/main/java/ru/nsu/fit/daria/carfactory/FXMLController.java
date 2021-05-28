package ru.nsu.fit.daria.carfactory;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import ru.nsu.fit.daria.carfactory.util.Observer;

public class FXMLController implements Initializable, Observer {

    @FXML
    public Text carCount;
    public Text wheelCount;
    public Text carBodyCount;
    public Text engineCount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        carCount = new Text();
        wheelCount = new Text();
        engineCount = new Text();
    }

    @FXML

    public void initFactory(){
        System.out.println("you suck at coding");
        carCount.setText("eee");
        wheelCount.setText("2232");
        engineCount.setText(String.valueOf(6));
    }

    @Override
    public void onUpdate() {

    }
}
