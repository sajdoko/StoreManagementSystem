package test;

import app.utils.JavaFXInitializer;
import controller.user.pages.UserProductsController;
import javafx.application.Platform;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

//test for addActionButtonsToTable method
public class UserProductsControllerTest {
    @InjectMocks
    private UserProductsController userProductsController;

    @BeforeAll
    public static void setUpAll() {
        new JavaFXInitializer().init();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userProductsController.tableProductsPage = new TableView<>();
    }

    @Test
    public void addActionButtonsToTable_test() throws InterruptedException {
        Platform.runLater(() -> {
            userProductsController.addActionButtonsToTable();

            TableColumn<Product, Void> actionColumn = (TableColumn<Product, Void>) userProductsController.tableProductsPage.getColumns().get(0);
            assertEquals("Actions", actionColumn.getText());

            // Create a TableCell instance to test the cell factory
            TableCell<Product, Void> cell = actionColumn.getCellFactory().call(actionColumn);

            try {
                Method updateItemMethod = TableCell.class.getDeclaredMethod("updateItem", Object.class, boolean.class);
                updateItemMethod.setAccessible(true);
                updateItemMethod.invoke(cell, null, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            HBox buttonsPane = (HBox) cell.getGraphic();
            assertEquals(1, buttonsPane.getChildren().size());
        });
    }

    @Test
    public void addActionButtonsToTable_Coverage_test() throws InterruptedException {
        Platform.runLater(() -> {
            userProductsController.addActionButtonsToTable();

            TableColumn<Product, Void> actionColumn = (TableColumn<Product, Void>) userProductsController.tableProductsPage.getColumns().get(0);
            assertEquals("Actions", actionColumn.getText());

            // Create a TableCell instance to test the cell factory
            TableCell<Product, Void> cell = actionColumn.getCellFactory().call(actionColumn);

            try {
                Method updateItemMethod = TableCell.class.getDeclaredMethod("updateItem", Object.class, boolean.class);
                updateItemMethod.setAccessible(true);
                updateItemMethod.invoke(cell, null, false);
                updateItemMethod.invoke(cell, null, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            HBox buttonsPane = (HBox) cell.getGraphic();
            assertEquals(1, buttonsPane.getChildren().size());
        });
    }
}