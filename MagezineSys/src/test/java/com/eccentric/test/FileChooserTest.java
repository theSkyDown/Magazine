/*
 * author:eccentric
 * time:2022/11/14
 */
package com.eccentric.test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileChooserTest extends Application {
    @Test
    public void test(){
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox hBox = new HBox();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("测试");
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("PNG","*.png")
        );


        Button btn = new Button();
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String HomePath = "/home/eccentric/Documents/ideacoding/MagezineSys/src/main/resources/img/";
                File file = fileChooser.showOpenDialog(stage);
                File other = new File(HomePath+"/temp.png");
                try {
                    Files.copy(file.toPath(),other.toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(file);
            }
        });

        hBox.getChildren().setAll(btn);


        stage.setScene(new Scene(hBox));

        stage.show();
    }
}
