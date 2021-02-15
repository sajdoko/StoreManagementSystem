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

    /**
     * {@inheritDoc}
     * @param primaryStage      Accepts Stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        primaryStage.setTitle("Store Management System");
        primaryStage.getIcons().add(new Image("/view/resources/img/brand/fav.png"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();

    }

    /**
     * {@inheritDoc}
     * This method initializes the application and opens the connection to the database.
     * @throws Exception      If an input or exception occurred.
     */
    @Override
    public void init() throws Exception {
        super.init();
        if(!Datasource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database");
            Platform.exit();
        }
    }

    /**
     * {@inheritDoc}
     * This method stops the application and closes the connection to the database.
     * @throws Exception      If an input or exception occurred.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
    }

    /**
     * {@inheritDoc}
     * The main method.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
