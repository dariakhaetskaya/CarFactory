package ru.nsu.fit.daria.carfactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class MainApp extends Application{
    private final Pane group = new Pane();
    private final Scene scene = new Scene(group, 1200, 800);
    private Text carCount;
    private Text carsInStorage;
    private Text wheelsInStorage;
    private Text enginesInStorage;
    private Text carBodiesInStorage;
    private CarFactory factory;

    @Override
    public void start(Stage stage) {

        factory = new CarFactory();
        carCount = new Text("Total cars produced: " + factory.getProducedCarCount());
        carsInStorage = new Text("Cars in storage: " + factory.getCarStorageSize());
        carBodiesInStorage = new Text("Cars bodies in storage: " + factory.getCarBodyStorageSize());
        wheelsInStorage = new Text("Wheels in storage: " + factory.getWheelStorageSize());
        enginesInStorage = new Text("Engines in storage: " + factory.getEngineStorageSize());

        int topPadding = 70;
        int leftPadding = 40;
        int interval = 30;

        carCount.setX(leftPadding);
        carCount.setY(topPadding);

        carsInStorage.setX(leftPadding);
        carsInStorage.setY(topPadding + interval);

        wheelsInStorage.setX(leftPadding);
        wheelsInStorage.setY(topPadding + 2 * interval);

        enginesInStorage.setX(leftPadding);
        enginesInStorage.setY(topPadding + 3 * interval);

        carBodiesInStorage.setX(leftPadding);
        carBodiesInStorage.setY(topPadding + 4 * interval);

        /************************ slider ***************************/
        Label label = new Label("Supplier delay in mills:");

        Label infoLabel = new Label("-");
        infoLabel.setTextFill(Color.BLUE);

        Slider slider = new Slider();

        slider.setMin(0);
        slider.setMax(60000);
        slider.setValue(1);

//        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        slider.setBlockIncrement(10);

        // Adding Listener to value property.
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            infoLabel.setText("New value: " + newValue.intValue());
            factory.setSupplierDelay(newValue.intValue());
        }
        );

        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        root.getChildren().addAll(label, slider, infoLabel);
        root.setLayoutY(400);
        /***********************************************************/

        Group counts = new Group(root, carCount, carsInStorage, wheelsInStorage, enginesInStorage, carBodiesInStorage);
        counts.setStyle("-fx-font: 17 arials;");

        Scene scene = new Scene(counts, 1000, 800);
        stage.setTitle("Car Factory");
        stage.setScene(scene);
        stage.show();

        Timer upd = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    carsInStorage.setText("Cars in storage: " + factory.getCarStorageSize());
                    carBodiesInStorage.setText("Cars bodies in storage: " + factory.getCarBodyStorageSize());
                    wheelsInStorage.setText("Wheels in storage: " + factory.getWheelStorageSize());
                    enginesInStorage.setText("Engines in storage: " + factory.getEngineStorageSize());
                    carCount.setText("Total cars produced: " + factory.getProducedCarCount());
                });
            }
        };

        upd.schedule(task, 0, 300);

//        Scanner in = new Scanner(System.in);
//        String str = in.nextLine();
//        factory.stopFactory();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
