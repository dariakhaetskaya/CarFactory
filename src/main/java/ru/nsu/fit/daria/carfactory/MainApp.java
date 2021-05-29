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
    private Text carCount;
    private Text carsInStorage;
    private Text wheelsInStorage;
    private Text enginesInStorage;
    private Text carBodiesInStorage;
    private Text factoryBudget;
    private CarFactory factory;

    @Override
    public void start(Stage stage) {

        factory = new CarFactory();
        carCount = new Text("Total cars produced: " + factory.getProducedCarCount());
        carsInStorage = new Text("Cars in storage: " + factory.getCarStorageSize());
        carBodiesInStorage = new Text("Cars bodies in storage: " + factory.getCarBodyStorageSize());
        wheelsInStorage = new Text("Wheels in storage: " + factory.getWheelStorageSize());
        enginesInStorage = new Text("Engines in storage: " + factory.getEngineStorageSize());
        factoryBudget = new Text("Factory budget: " + factory.getFactoryBudget());

        int topPadding = 50;
        int leftPadding = 40;
        int interval = 40;

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

        factoryBudget.setX(leftPadding);
        factoryBudget.setY(topPadding + 5 * interval);

        // put sliders in a box
        VBox sliderBox = new VBox();
        sliderBox.setPadding(new Insets(leftPadding));
        sliderBox.setSpacing(20);
        sliderBox.setLayoutY(250);

        // create Wheel Supplier Delay Slider
        Label wheelSupplierDelay = new Label("Wheel supplier delay in mills:");
        Label newWheelSupplierDelay = new Label("-");
        newWheelSupplierDelay.setTextFill(Color.DARKGREEN);
        Slider wheelSupplierDelaySlider = new Slider();
        wheelSupplierDelaySlider.setMin(0);
        wheelSupplierDelaySlider.setMax(60000);
        wheelSupplierDelaySlider.setValue(1000);
        wheelSupplierDelaySlider.setShowTickMarks(true);
        wheelSupplierDelaySlider.setBlockIncrement(10);

        // Adding Listener to value property.
        wheelSupplierDelaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            newWheelSupplierDelay.setText("Current wheel supplier delay: " + newValue.intValue());
            factory.setWheelSupplierDelay(newValue.intValue());
        }
        );

        sliderBox.getChildren().addAll(wheelSupplierDelay, newWheelSupplierDelay, wheelSupplierDelaySlider);

        // create Engine Supplier Delay Slider
        Label engineSupplierDelay = new Label("Engine supplier delay in mills:");
        Label newEngineSupplierDelay = new Label("-");
        newEngineSupplierDelay.setTextFill(Color.DARKGREEN);
        Slider engineSupplierDelaySlider = new Slider();
        engineSupplierDelaySlider.setMin(0);
        engineSupplierDelaySlider.setMax(60000);
        engineSupplierDelaySlider.setValue(1000);
        engineSupplierDelaySlider.setShowTickMarks(true);
        engineSupplierDelaySlider.setBlockIncrement(10);

        // Adding Listener to value property.
        engineSupplierDelaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    newEngineSupplierDelay.setText("Current engine supplier delay: " + newValue.intValue());
                    factory.setEngineSupplierDelay(newValue.intValue());
                }
        );

        sliderBox.getChildren().addAll(engineSupplierDelay, newEngineSupplierDelay, engineSupplierDelaySlider);

        // create Car Body Supplier Delay Slider
        Label carBodySupplierDelay = new Label("Car body supplier delay in mills:");
        Label newCarBodySupplierDelay = new Label("-");
        newCarBodySupplierDelay.setTextFill(Color.DARKGREEN);
        Slider carBodySupplierDelaySlider = new Slider();
        carBodySupplierDelaySlider.setMin(0);
        carBodySupplierDelaySlider.setMax(60000);
        carBodySupplierDelaySlider.setValue(1000);
        carBodySupplierDelaySlider.setShowTickMarks(true);
        carBodySupplierDelaySlider.setBlockIncrement(10);

        // Adding Listener to value property.
        carBodySupplierDelaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    newCarBodySupplierDelay.setText("Current car body supplier delay: " + newValue.intValue());
                    factory.setEngineSupplierDelay(newValue.intValue());
                }
        );

        sliderBox.getChildren().addAll(carBodySupplierDelay, newCarBodySupplierDelay, carBodySupplierDelaySlider);

        // create Dealer Delay Slider
        Label dealerDelay = new Label("Dealer delay in mills:");
        Label newDealerDelay = new Label("-");
        newDealerDelay.setTextFill(Color.DARKGREEN);
        Slider dealerDelaySlider = new Slider();
        dealerDelaySlider.setMin(0);
        dealerDelaySlider.setMax(60000);
        dealerDelaySlider.setValue(1000);
        dealerDelaySlider.setShowTickMarks(true);
        dealerDelaySlider.setBlockIncrement(10);

        // Adding Listener to value property.
        dealerDelaySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    newDealerDelay.setText("Current dealer delay: " + newValue.intValue());
                    factory.setDealerDelay(newValue.intValue());
                }
        );
        sliderBox.getChildren().addAll(dealerDelay, newDealerDelay, dealerDelaySlider);


        // group everything
        Group counts = new Group(sliderBox, carCount, carsInStorage, wheelsInStorage, enginesInStorage, carBodiesInStorage, factoryBudget);
        counts.setStyle("-fx-font: 17 arials;");

        Scene scene = new Scene(counts, 600, 900);
        scene.setFill(Color.PEACHPUFF);
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
                    factoryBudget.setText("Factory budget: " + factory.getFactoryBudget());
                });
            }
        };

        upd.schedule(task, 0, 10);

    }

    @Override
    public void stop() throws Exception {
        factory.stopFactory();
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
