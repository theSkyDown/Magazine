/*
 * author:eccentric
 * time:2022/11/10
 */
package com.eccentric.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class AlertUtil {
    /**
     * 统一弹窗的样式
     * @param type 弹出类型
     * @param content 输出的类型
     */
    public static void alertDisplay(Alert.AlertType type, String content){
        Alert alert = new Alert(type,content);
        alert.setHeaderText(null);
        alert.getDialogPane().setStyle("-fx-background-color: #17212B;");
        Label text = (Label) alert.getDialogPane().getChildren().get(1);
        text.setText(content);
        text.setStyle("-fx-text-fill: white;-fx-font-size: 24");
        alert.showAndWait();
    }


    /**
     * 统一表弹窗样式
     * @param type 弹窗的类型
     * @param vBox 弹窗内容
     * @return
     */
    public static Alert alertForm(Alert.AlertType type, VBox vBox,Callback<ButtonType,ButtonType> callback){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        //设置内容
        alert.getDialogPane().setContent(vBox);
        alert.getDialogPane().setStyle("-fx-background-color: #17212B;");
        //宽高
        alert.getDialogPane().setPrefHeight(200);
        alert.getDialogPane().setPrefWidth(400);
        //设置按钮
        //按钮
        List<ButtonType> list = new ArrayList<>();
        list.add(ButtonType.FINISH);
        list.add(ButtonType.CLOSE);
        ObservableList<ButtonType> buttonTypes = FXCollections.observableList(list);
        alert.getButtonTypes().setAll(buttonTypes);
        alert.setResultConverter(callback);
        alert.showAndWait();
        return alert;
    }


}
