package ru.nsu.fit.daria.carfactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import static ru.nsu.fit.daria.carfactory.util.Utils.*;


public class MainApp extends Application {
    private final Pane group = new Pane();
    private final Scene scene = new Scene(group, 1200, 800);
    private Text carCount;
    private Text carsInStorage;
    private Text wheelsInStorage;
    private Text enginesInStorage;
    private Text carBodiesInStorage;
    private CarFactory factory;

    @Override
    public void start(Stage stage) throws Exception {

       // CarFactory factory = new CarFactory();
        FXMLLoader loader = new FXMLLoader();

        URL xmlUrl = getClass().getResource("scene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        FXMLController controller = loader.getController();

        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        carCount = new Text(String.valueOf(100));
        carBodiesInStorage = new Text(String.valueOf(200));
        wheelsInStorage = new Text(String.valueOf(300));
        enginesInStorage = new Text(String.valueOf(400));

        carCount.setX(100);
        carCount.setY(100);
        wheelsInStorage.setX(100);
        wheelsInStorage.setY(200);
        enginesInStorage.setX(100);
        enginesInStorage.setY(300);
        carBodiesInStorage.setX(100);
        carBodiesInStorage.setY(400);
        carCount.setStyle("-fx-font: 20 arials;");

        controller.initFactory();
        stage.setTitle("Car Factory");
        stage.setScene(scene);
        System.out.println(ANSI_CYAN + "Showing stage" + ANSI_RESET );
        stage.show();
        System.out.println(ANSI_YELLOW + "Showed stage" + ANSI_RESET );
//        Scanner in = new Scanner(System.in);
//        String str = in.nextLine();
//        factory.stopFactory();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
