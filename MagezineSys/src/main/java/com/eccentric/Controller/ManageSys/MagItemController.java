/*
 * author:eccentric
 * time:2022/11/13
 */
package com.eccentric.Controller.ManageSys;

import com.eccentric.DAO.MagDAO;
import com.eccentric.DAO.OrderDAO;
import com.eccentric.Utils.AlertUtil;
import com.eccentric.Utils.JDBCUtil;
import com.eccentric.pojo.Magazine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class MagItemController {
    OrderDAO orderDAO = new OrderDAO();
    MagDAO magDAO = new MagDAO();

    @FXML
    public ImageView imgView;//杂志图片

    @FXML
    public Label magName;//杂志名称

    @FXML
    public Label magClassify;//杂志类型

    @FXML
    public Label magPrice;//杂志价格

    @FXML
    public Label subNum;//订阅人数

    @FXML
    public Button updateInf;//修改按钮

    @FXML
    public Button deleteMag;//删除按钮

    /**
     * 放入数据
     * @param magazine
     */
    @SuppressWarnings("all")
    public void setDate(Magazine magazine,ManageController mc){
        //杂志图片
        ResourceBundle bundle = ResourceBundle.getBundle("FileHelp");
        String homePath = bundle.getString("HomePath");
        Image img = null;
        try {
            img = new Image(new File(homePath+magazine.getImgSrc()).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        imgView.setImage(img);
        //杂志名称
        magName.setText(magazine.getBookName());
        //杂志类型
        magClassify.setText(magazine.getClassify());
        //杂志价格
        magPrice.setText("￥" + magazine.getPrice() + "/月");
        //订阅人数
        Integer magSubNum = orderDAO.getMagSubNum(magazine.getId());
        subNum.setText(magSubNum + "人");

        //修改按钮
        updateInf.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(20);
                vBox.setAlignment(Pos.CENTER);
                Label title = new Label("修改杂志信息");
                title.setFont(new Font(24));
                title.setTextFill(Color.WHITE);
                //输入框
                HBox hBox1 = new HBox(10);
                Label MagNameLabel = new Label("杂志名称");
                MagNameLabel.setTextFill(Color.WHITE);
                TextField updateMagName = new TextField();
                updateMagName.setText(magazine.getBookName());
                hBox1.setAlignment(Pos.CENTER);
                hBox1.getChildren().setAll(MagNameLabel,updateMagName);

                HBox hBox2 = new HBox(10);
                Label MagClassifyLabel = new Label("杂志类型");
                MagClassifyLabel.setTextFill(Color.WHITE);
                //选择框
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.setItems(FXCollections.observableList(magDAO.getAllClassifies()));
                comboBox.getSelectionModel().select(magazine.getClassify());
                hBox2.setAlignment(Pos.CENTER);
                hBox2.getChildren().setAll(MagClassifyLabel,comboBox);

                HBox hBox3 = new HBox(10);
                Label MagPriceLabel = new Label("杂志价格");
                MagPriceLabel.setTextFill(Color.WHITE);
                TextField updatePrice = new TextField();
                updatePrice.setText(magazine.getPrice().toString());
                hBox3.setAlignment(Pos.CENTER);
                hBox3.getChildren().setAll(MagPriceLabel,updatePrice);


                vBox.getChildren().setAll(title,hBox1,hBox3,hBox2);


                Callback<ButtonType,ButtonType> alertCellback = new Callback<ButtonType, ButtonType>() {
                    @Override
                    public ButtonType call(ButtonType param) {
                        if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                            magazine.setBookName(updateMagName.getText());
                            magazine.setClassify(comboBox.getValue());
                            String priceText = updatePrice.getText();
                            //拦截空表单
                            if (magazine.getBookName().equals("") || priceText.equals("")){
                                mc.updatePage();
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
                                return param;
                            }
                            //拦截输入的价格不是正浮点数
                            if (!Pattern.matches("\\d*(\\.?)\\d*",priceText)){
                                mc.updatePage();
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"请输入数字");
                                return param;
                            }
                            magazine.setPrice(Double.valueOf(updatePrice.getText()));
                            //判断是否修改成功
                            if (magDAO.updateMag(magazine)) {
                                setDate(magazine,mc);
                                AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"修改成功");
                            }else{
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"修改失败");
                            }
                        }else if(param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                            System.out.println("用户取消");
                        }
                        return param;
                    }
                };
                AlertUtil.alertForm(Alert.AlertType.INFORMATION, vBox,alertCellback);
            }
        });

        //删除按钮
        deleteMag.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (magDAO.deleteMagById(magazine.getId())) {
                    mc.updatePage();
                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"删除成功");
                }else{
                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"删除失败");
                }
            }
        });

    }



}
