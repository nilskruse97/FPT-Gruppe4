package main;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.View;
import controller.Controller;

public class MainClass extends Application{
    public static void main(String[] args) {
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        

        // hier die Daten verwalten
        Model model = new Model();
        Controller controller = new Controller();
        View view = new View(controller);
        controller.link(model, view);



        // JavaFX new
        Scene scene  = new Scene(view, 700, 500);
        primaryStage.setTitle("MUSICPLAYER");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
