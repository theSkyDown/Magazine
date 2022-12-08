/*
 * author:eccentric
 * time:2022/11/13
 */
package com.eccentric.Controller.ManageSys;

import com.eccentric.DAO.UserDAO;
import com.eccentric.Starter.Main;
import com.eccentric.Utils.AlertUtil;
import com.eccentric.Utils.MD5Util;
import com.eccentric.pojo.User;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.regex.Pattern;


public class UserItemController {
    UserDAO userDAO = new UserDAO();

    @FXML
    public Label username;//用户名

    @FXML
    public Label phone;//电话号码

    @FXML
    public Label isManager;//是否为管理员

    @FXML
    public Label address;//用户地址

    @FXML
    public Button updateBtn;//修改按钮

    @FXML
    public Button deleteBtn;//删除按钮

    @FXML
    public Button resetPwdBtn;//重置密码按钮


    /**
     * 放入数据
     * @param user
     */
    public void setDate(User user,ManageController mc){
        //设置用户名
        username.setText(user.getUsername());
        //设置电话号码
        phone.setText(user.getPhone());
        //设置是否为管理员
        isManager.setText(user.getIsManager()==1?"是":"否");
        //设置用户地址
        address.setText(user.getAddress());
        //修改按钮点击事件
        updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(20);
                vBox.setAlignment(Pos.CENTER);

                //标题
                Label title = new Label("修改用户信息");
                title.setFont(new Font(24));
                title.setTextFill(Color.WHITE);

                //输入框
                HBox hBox1 = new HBox(10);
                Label usernameLabel = new Label("用户名");
                usernameLabel.setTextFill(Color.WHITE);
                TextField usernameInput = new TextField(user.getUsername());
                hBox1.setAlignment(Pos.CENTER);
                hBox1.getChildren().setAll(usernameLabel,usernameInput);

                HBox hBox3 = new HBox(10);
                Label addressLabel = new Label("地址");
                addressLabel.setTextFill(Color.WHITE);
                TextField addressInput = new TextField(user.getAddress());
                hBox3.setAlignment(Pos.CENTER);
                hBox3.getChildren().setAll(addressLabel,addressInput);

                HBox hBox4 = new HBox(10);
                Label isManagerLabel = new Label("是否是管理员");
                isManagerLabel.setTextFill(Color.WHITE);
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.setItems(FXCollections.observableArrayList("否","是"));
                comboBox.getSelectionModel().select(user.getIsManager());
                hBox4.setAlignment(Pos.CENTER);
                hBox4.getChildren().setAll(isManagerLabel,comboBox);

                vBox.getChildren().setAll(title,hBox1,hBox3,hBox4);

                //点击事件
                Callback<ButtonType,ButtonType> callback = new Callback<ButtonType, ButtonType>() {
                    @Override
                    public ButtonType call(ButtonType param) {
                        if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                            user.setUsername(usernameInput.getText());
                            user.setAddress(addressInput.getText());
                            user.setIsManager(comboBox.getValue().equals("是")?1:0);
                            //拦截空表单
                            if (user.getUsername().equals("")||user.getPhone().equals("")){
                                mc.updatePage();
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
                                return param;
                            }
                            //判断是否修改成功
                            if (userDAO.updateUser(user)) {
                                setDate(user,mc);
                                AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"修改成功");
                            }else{
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"修改失败");
                            }

                        } else if (param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                            System.out.println("用户取消");
                        }
                        return param;
                    }
                };
                AlertUtil.alertForm(Alert.AlertType.INFORMATION,vBox,callback);
            }
        });

        //删除按钮点击事件
        deleteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (userDAO.deleteUserById(user.getId())) {
                    mc.updatePage();
                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"删除成功");
                }else{
                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"删除失败");
                }
            }
        });

        //重置密码按钮
        resetPwdBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(20);
                vBox.setAlignment(Pos.CENTER);
                Label title = new Label("修改密码");
                title.setFont(new Font(24));
                title.setTextFill(Color.WHITE);

                //两个输入框
                HBox newPwdHBox = new HBox(10);
                Label newLabel = new Label("新密码");
                newLabel.setTextFill(Color.WHITE);
                PasswordField newPwd = new PasswordField();
                newPwdHBox.setAlignment(Pos.CENTER);
                newPwdHBox.getChildren().setAll(newLabel,newPwd);

                HBox comPwdHBox = new HBox(10);
                Label comLabel = new Label("确认密码");
                comLabel.setTextFill(Color.WHITE);
                PasswordField comPwd = new PasswordField();
                comPwdHBox.setAlignment(Pos.CENTER);
                comPwdHBox.getChildren().setAll(comLabel,comPwd);

                vBox.getChildren().setAll(title,newPwdHBox,comPwdHBox);

                //提交或者取消
                Callback<ButtonType,ButtonType> alertCallback = new Callback<ButtonType, ButtonType>() {
                    @Override
                    public ButtonType call(ButtonType param) {
                        if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                            try {
                                //拦截空表单
                                if (newPwd.getText().equals("") || comPwd.getText().equals("")){
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
                                    return param;
                                }
                                //拦截两次密码不相同的情况
                                if (!comPwd.getText().equals(newPwd.getText())) {
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"两次输入的密码不相同");
                                    return param;
                                }
                                //判断数据库是否修改成功
                                if (userDAO.updateUserPassword(user.getId(),newPwd.getText())) {
                                    setDate(user,mc);
                                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"重置成功");
                                }else{
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"重置失败");
                                }

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else if (param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                            System.out.println("用户已取消");
                        }
                        return param;
                    }
                };
                AlertUtil.alertForm(Alert.AlertType.INFORMATION, vBox,alertCallback);
            }
        });
    }

}
