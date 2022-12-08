/*
 * author:eccentric
 * time:2022/11/8
 */
package com.eccentric.Controller;

import com.eccentric.pojo.Magazine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ResourceBundle;

public class itemController {
    @FXML
    private ImageView image;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private VBox card;

    /**
     * 设置卡片的数据
     * @param magazine
     * @param mc
     */
    public void setDate(final Magazine magazine, final MagController mc){
        nameLabel.setText(magazine.getBookName());
        priceLabel.setText("￥"+magazine.getPrice()+"/月");

        ResourceBundle bundle = ResourceBundle.getBundle("FileHelp");
        String homePath = bundle.getString("HomePath");
        //放入图片
        Image img = null;
        try {
            img = new Image(new File(homePath+magazine.getImgSrc()).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        image.setImage(img);

        //给卡片设置点击事件
        card.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                card.setCursor(Cursor.HAND);
            }
        });
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                mc.setChosenMagazineCard(magazine);
            }
        });

    }
}
