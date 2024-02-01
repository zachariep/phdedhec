package com.biserv.victorp.research.phdedhec;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhDEDHECApplication extends Application {
    public static final String NAME_PHD_EDHEC_RESEARCH_C_2021_PLUS = "PhD EDHEC Research. (c) 2021+";
    public static final String VIEW_FILE_PHDEDHECMAIN_FXML = "phdedhecmain-view.fxml";
    public static final String VIEW_FILE_STYLE_CSS = "style_view.css";

    private static final Logger logger = LogManager.getLogger(PhDEDHECApplication.class.getName());

    private FXMLLoader fxmlLoader;
    private Scene scenePrimary;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Start ...");
        setPrimaryStage(primaryStage);
        setFxmlLoader( new FXMLLoader(PhDEDHECApplication.class.getResource(PhDEDHECApplication.VIEW_FILE_PHDEDHECMAIN_FXML)));
        setScenePrimary( new Scene(getFxmlLoader().load(), 960, 640));
        getPrimaryStage().setTitle(PhDEDHECApplication.NAME_PHD_EDHEC_RESEARCH_C_2021_PLUS);
        getPrimaryStage().setScene(getScenePrimary());
        PhDEDHECController controller = getFxmlLoader().getController();
        controller.setApplication(this);
        String cssData = PhDEDHECApplication.class.getResource(PhDEDHECApplication.VIEW_FILE_STYLE_CSS).toExternalForm();
        controller.getConfigPropListView()
                  .getStylesheets()
                  .add(cssData);
        controller.getDataPropListView()
                  .getStylesheets()
                  .add(cssData);
        logger.info("Title: {}", getPrimaryStage().getTitle());
        Parent parent = getScenePrimary().getRoot();
        controller.initProgressHBox();
        getPrimaryStage().show();
        logger.info("End ...");
    }

    public FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public void setFxmlLoader(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Scene getScenePrimary() { return scenePrimary; }
    public void setScenePrimary(Scene scenePrimary) {
        this.scenePrimary = scenePrimary;
    }

    public Stage getPrimaryStage() { return primaryStage; }
    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}