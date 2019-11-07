package com.cgi.extraction.fx;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.cgi.extraction.*;
import com.cgi.extraction.exception.AucunActiveLinkException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.control.ChoiceBox;

import javax.swing.*;

public class MainApp extends Application {


    private Stage primaryStage;
    private AbstractButton actionStatus;
    private AbstractExtract extract = null;

    @Override
    public void start(final Stage primaryStage) {

        primaryStage.setTitle("BMC Remedy EXTRACT");



        final Group root = new Group();
        Scene scene = new Scene(root, 800, 600, Color.GREEN);
        primaryStage.setScene(scene);

        final Label label = new Label("Sélectionner un Active Link ");
        label.setLayoutX(20);
        label.setLayoutY(40);
        label.setFont(new Font("Arial",25));
        root.getChildren().add(label);

        final Label labelExtract = new Label("Extraction en cours.. ");
        labelExtract.setLayoutX(40);
        labelExtract.setLayoutY(250);
        labelExtract.setFont(new Font("Arial",25));
        root.getChildren().add(labelExtract);

        final Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Information");
        alertError.setHeaderText("Erreur fichier");
        alertError.setContentText("Veuillez sélectionner le bon active link");

        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Extraction terminée");

        labelExtract.setVisible(false);



        final ChoiceBox choice = new ChoiceBox();
        choice.getItems().addAll("Forms",
                "Active Link",
                "Active Link Guildes",
                "Filtres",
                "Filtres Guildes",
                "Application",
                "Forms",
                "Escalation",
                "Flashboard Variables",
                "Flashboard",
                "Images",
                "Menu",
                "Web Service");
        choice.setLayoutX(70);
        choice.setLayoutY(100);
        root.getChildren().add(choice);

        final Button buttonLoad = new Button("Sélectionner fichier");

        buttonLoad.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent arg0) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.def)", "*.def");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(primaryStage);

                switch ((String) choice.getValue()){
                    case "Forms":
                        extract = new FormsExtract();
                        break;

                    case "Active Link":
                        extract = new ActiveLinkExtract();
                        break;
                    case "Active Link Guildes":
                        extract = new ActiveLinkGuidesExtract();
                        break;

                    case "Filtres":
                        extract = new FiltresExtract();
                        break;
                    case "Filtres Guildes":
                        extract = new FilterGuidesExtract();
                        break;

                    case "Application":
                        extract = new ApplicationExtract();
                        break;
                    case "Escalation":
                        extract = new EscalationExtract();
                        break;

                    case "Flashboard Variables":
                        extract = new FlashboardsVariablesExtract();
                        break;
                    case "Flashboard":
                        extract = new FlashboardsExtract();
                        break;
                    case "Images":
                        extract = new ImagesExtract();
                        break;
                    case "Menu":
                        extract = new MenusExtract();
                        break;
                    case "Web Service":
                        extract = new WebServicesExtract();
                        break;

                }
                extract.setInput(file.getAbsolutePath());
                try {

                    labelExtract.setVisible(true);
                    extract.extract();
                    labelExtract.setVisible(false);
                    alert.showAndWait();

                } catch (AucunActiveLinkException e) {
                    labelExtract.setVisible(false);
                    alertError.showAndWait();

                }
            }
        });
        VBox vBox = VBoxBuilder.create()
                .children(buttonLoad)
                .build();
        root.getChildren().add(vBox);
        vBox.setLayoutX(90);
        vBox.setLayoutY(180);



        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);

    }
}
