package app.utils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
//public class JavaFXInitializer {
//    private static boolean initialized = false;
//
//    public static void init() {
//        if (!initialized) {
//            Platform.startup(() -> {}); // Initialize JavaFX toolkit
//            initialized = true;
//        }
//    }
//}

public class JavaFXInitializer extends Application {
    private static boolean initialized = false;

    @Override
    public void start(Stage primaryStage) {
        // No GUI required; this method ensures JavaFX toolkit is initialized
    }

    public synchronized void init() {
        if (initialized) {
            return; // Prevent multiple initializations
        }
        initialized = true;
        Thread fxThread = new Thread(() -> {
            try {
                Application.launch(JavaFXInitializer.class);
            } catch (IllegalStateException e) {
                // JavaFX is already initialized, ignore the exception
            }
        });
        fxThread.setDaemon(true); // Ensure it doesn't block JVM shutdown
        fxThread.start();
        // Give JavaFX runtime some time to initialize
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}