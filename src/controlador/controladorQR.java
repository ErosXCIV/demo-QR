package controlador;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class controladorQR implements Initializable {

    // Parte 1/3
    public Label labelEmail;
    public TextField textFieldEmail;

    // Parte 2/3
    public Label labelTipoDeDato;
    public RadioButton radioButtonEnlaceWeb;
    public RadioButton radioButtonNumTelf;
    public RadioButton radioButtonSMS;
    public RadioButton radioButtomTextoSinFormato;
    public ToggleGroup tipoDeDato;

    // Parte 3/3
    public Label labelIntroducirDatos;
    public TextField textFieldIntroducirDatos;
    public TextField textFieldTextoSMS;

    // Parte Generar Final
    public Button buttonGenerar;
    public Button buttonDescargar;
    public ImageView imagenQR;
    public Label labelQR;


    // Rectángulos de énfasis al seleccionar
    public Rectangle rectanguloEmail;
    public Rectangle rectanguloRadioButtoms;
    public Rectangle rectanguloIntroducirDatos;

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    // Atributo auxiliar que siempre será falso
    public static boolean finDelBucle = false;

    // Image para QR
    public Image imageToQR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Diseño de botón de descargar QR
        Image img = new Image("./vista/icons/download-icon.png");
        ImageView view = new ImageView(img);
        buttonDescargar.setGraphic(view);

        // Hilo que supervisa que los campos estén completos y correctos
        thread.start();
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            do {
                mostrarButtonGenerarQR();
            } while (!finDelBucle);
        }
    });

    public void rectangulosUX(int pos){
        switch (pos){
            case 1:
                rectanguloEmail.setVisible(true);
                rectanguloRadioButtoms.setVisible(false);
                rectanguloIntroducirDatos.setVisible(false);
                break;
            case 2:
                rectanguloEmail.setVisible(false);
                rectanguloRadioButtoms.setVisible(true);
                rectanguloIntroducirDatos.setVisible(false);
                break;
            case 3:
                rectanguloEmail.setVisible(false);
                rectanguloRadioButtoms.setVisible(false);
                rectanguloIntroducirDatos.setVisible(true);
                break;
            default:
                rectanguloEmail.setVisible(false);
                rectanguloRadioButtoms.setVisible(false);
                rectanguloIntroducirDatos.setVisible(false);
                break;
        }
    }

    public void clickEnDatosEmail(MouseEvent event){
            rectangulosUX(1);
    }

    public void clickEnIntroducirDatos(MouseEvent event){
        rectangulosUX(3);
    }

    public void comprobarDatosTextFieldEmail(KeyEvent event) {
        rectangulosUX(1);

        if(textFieldEmail.getText().matches(EMAIL_PATTERN)){
            textFieldEmail.setStyle("-fx-text-box-border: white ;\n" + "-fx-focus-color: green ;");
        } else {
            textFieldEmail.setStyle("-fx-text-box-border: red ;\n" + "-fx-focus-color: red ;");
        }

    }

    public void checkRadioButtom(ActionEvent event) {
        rectangulosUX(2);

        tipoDeDato.selectedToggleProperty().addListener((observable, oldToggle, toggleSeleccionado) -> {
                    if (toggleSeleccionado == radioButtonEnlaceWeb) {
                        clearAndHideTextFieldTextoDatos();
                        textFieldIntroducirDatos.promptTextProperty().setValue("www.shopify.com");



                    } else if (toggleSeleccionado == radioButtonNumTelf) {
                        clearAndHideTextFieldTextoDatos();
                        textFieldIntroducirDatos.promptTextProperty().setValue("617 306 777");



                    } else if (toggleSeleccionado == radioButtonSMS){
                        textFieldIntroducirDatos.clear();
                        textFieldTextoSMS.setVisible(true);
                        rectanguloIntroducirDatos.setHeight(105);
                        textFieldIntroducirDatos.promptTextProperty().setValue("617 306 777");


                    } else if (toggleSeleccionado == radioButtomTextoSinFormato){
                        clearAndHideTextFieldTextoDatos();
                        textFieldIntroducirDatos.promptTextProperty().setValue("Escriba el texto aquí");

                    } else {
                        clearAndHideTextFieldTextoDatos();
                    }
                }
        );
    }

    public void clearAndHideTextFieldTextoDatos(){
        textFieldTextoSMS.setVisible(false);
        textFieldTextoSMS.clear();
        textFieldIntroducirDatos.clear();
        rectanguloIntroducirDatos.setHeight(81);
    }

    public void mostrarButtonGenerarQR() {

        if (radioButtonSMS.isSelected()){

            if ((textFieldEmail.getText().matches(EMAIL_PATTERN) && (textFieldIntroducirDatos.getText().matches("[0-9]+") && textFieldIntroducirDatos.getText().length() == 9) && !(textFieldTextoSMS.getText().trim().isEmpty()))){
                buttonGenerar.setDisable(false);
            } else {
                imagenQR.opacityProperty().setValue(0.25);
                buttonGenerar.setDisable(true);
                buttonDescargar.setDisable(true);
            }
        }

        if (radioButtonNumTelf.isSelected()){

            if((textFieldEmail.getText().matches(controladorQR.EMAIL_PATTERN) && (textFieldIntroducirDatos.getText().matches("[0-9]+") && textFieldIntroducirDatos.getText().length() == 9))){
                buttonGenerar.setDisable(false);
            } else {
                imagenQR.opacityProperty().setValue(0.25);
                buttonGenerar.setDisable(true);
                buttonDescargar.setDisable(true);

            }
        }

        if (!radioButtonSMS.isSelected() && !radioButtonNumTelf.isSelected()) {

            if((textFieldEmail.getText().matches(controladorQR.EMAIL_PATTERN) && !(textFieldIntroducirDatos.getText().trim().isEmpty()))){
                buttonGenerar.setDisable(false);
            } else {
                imagenQR.opacityProperty().setValue(0.25);
                buttonGenerar.setDisable(true);
                buttonDescargar.setDisable(true);

            }
        }

    }

    public void clickButtonGenerarQR(ActionEvent event) {

        rectangulosUX(0);

        ByteArrayOutputStream out = QRCode.from(this.textFieldIntroducirDatos.getText()).to(ImageType.PNG).withSize(225, 225).stream();
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        imageToQR = new Image(in);

//        ImageView view = new ImageView(imageToQR);
//        view.setStyle("-fx-stroke-width: 2; -fx-stroke: blue");

        imagenQR.opacityProperty().setValue(1);
        imagenQR.setImage(imageToQR);

        buttonDescargar.setDisable(false);
    }

    public void guardarQR(ActionEvent event) throws IOException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../vista/mainWindow.fxml")));
        Scene directoryfileChooserScene = new Scene(root,650, 410);
        Stage directoryChooserStage = new Stage();
        directoryChooserStage.setScene(directoryfileChooserScene);
        directoryChooser.setTitle("Guardar imagen");

        File carpetaSeleccionada = directoryChooser.showDialog(directoryChooserStage);
        File carpetaDestino = new File("");

        if (!(carpetaSeleccionada == null)){

            carpetaDestino = new File((carpetaSeleccionada.getAbsolutePath() + "\\QR-autogenerado.png"));

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(imageToQR, null), "png", carpetaDestino);

                Alert avisoDescargaFinalizada = new Alert(Alert.AlertType.INFORMATION);
                avisoDescargaFinalizada.headerTextProperty().setValue(null);
                avisoDescargaFinalizada.setContentText("Descarga de QR completada.");
                avisoDescargaFinalizada.resizableProperty().setValue(false);
                avisoDescargaFinalizada.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
