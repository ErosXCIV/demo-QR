package controlador;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class demoQR extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../vista/mainWindow.fxml")));
        Scene scene = new Scene(root, 650, 410);
        scene.getStylesheets().add("./vista/CSS/mainWindowsStylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("QR Generator");
        primaryStage.getIcons().add(new Image("./vista/icons/qr-icon.PNG"));
        primaryStage.resizableProperty().setValue(false);

        // Cerrar app y detener thread
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            primaryStage.close();
            controladorQR.finDelBucle = true;
        });

        primaryStage.show();
    }
}
