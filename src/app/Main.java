package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Datasource;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("../view/main-dashboard.fxml"));
        primaryStage.setTitle("Store Manager");
        primaryStage.getIcons().add(new Image("/view/resources/img/brand/fav.png"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if(!Datasource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
