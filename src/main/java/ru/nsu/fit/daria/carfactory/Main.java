package ru.nsu.fit.daria.carfactory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        launch(args);
        CarFactory factory = new CarFactory();
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        factory.stopFactory();
    }
}
