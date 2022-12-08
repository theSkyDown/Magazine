/*
 * author:eccentric
 * time:2022/11/15
 */
package com.eccentric.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;


public class imageTest extends Application {
    @Test
    public void test(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hBox = new HBox();
        Image img = new Image(new File("/home/eccentric/Documents/Magazine/img/Magazine/art/艺术与设计.png").toURI().toURL().toString());
        ImageView imv = new ImageView(img);
        hBox.getChildren().setAll(imv);

        primaryStage.setScene(new Scene(hBox,200,200));
        primaryStage.show();
    }
}
