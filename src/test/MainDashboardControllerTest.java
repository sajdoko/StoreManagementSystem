package test;


import app.utils.JavaFXInitializer;
import controller.UserSessionController;
import controller.admin.MainDashboardController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

//test for btnLogOutOnClick method
public class MainDashboardControllerTest {
    @Mock
    private Node node;

    @Mock
    private Stage stage;

    @Mock
    private Scene scene;

    @InjectMocks
    private MainDashboardController mainDashboardController;

    @BeforeAll
    public static void setUpAll(){
        new JavaFXInitializer().init();
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void btnLogOutOnClick_test() throws IOException{
        Platform.runLater(() -> {
            when(node.getScene()).thenReturn(scene);
            when(scene.getWindow()).thenReturn(stage);
            when(stage.getScene()).thenReturn(scene);

            Alert alert = mock(Alert.class);
            when(alert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

            try {
                mainDashboardController.btnLogOutOnClick(new ActionEvent(node, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            verify(stage, times(1)).close();
            verify(stage, times(1)).setScene(any(Scene.class));
            verify(stage, times(1)).show();
            verify(UserSessionController.class, times(1));
            UserSessionController.cleanUserSession();
        });
    }

    @Test
    public void btnLogOutOnClick_branchCoverage_test() throws IOException{
        Platform.runLater(() -> {
            when(node.getScene()).thenReturn(scene);
            when(scene.getWindow()).thenReturn(stage);
            when(stage.getScene()).thenReturn(scene);

            Alert alert = mock(Alert.class);
            when(alert.showAndWait()).thenReturn(Optional.of(ButtonType.CANCEL));

            try {
                mainDashboardController.btnLogOutOnClick(new ActionEvent(node, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            verify(stage, never()).close();
            verify(stage, never()).setScene(any(Scene.class));
            verify(stage, never()).show();
            verify(UserSessionController.class, never());
            UserSessionController.cleanUserSession();
        });
    }
    @Test
    public void btnLogOutOnClick_conditionCoverage_test() throws IOException{
        Platform.runLater(() -> {
            when(node.getScene()).thenReturn(scene);
            when(scene.getWindow()).thenReturn(stage);
            when(stage.getScene()).thenReturn(scene);

            Alert alert = mock(Alert.class);
            when(alert.showAndWait()).thenReturn(Optional.of(ButtonType.OK));

            try{
                mainDashboardController.btnLogOutOnClick(new ActionEvent(node, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            verify(stage, times(1)).close();
            verify(stage, times(1)).setScene(any(Scene.class));
            verify(stage, times(1)).show();
            verify(UserSessionController.class, times(1));
            UserSessionController.cleanUserSession();

            when(alert.showAndWait()).thenReturn(Optional.of(ButtonType.CANCEL));

            try{
                mainDashboardController.btnLogOutOnClick(new ActionEvent(node, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            verify(stage, times(1)).close();
            verify(stage, times(1)).setScene(any(Scene.class));
            verify(stage, times(1)).show();
            verify(UserSessionController.class, times(1));
            UserSessionController.cleanUserSession();
        });
    }
}
