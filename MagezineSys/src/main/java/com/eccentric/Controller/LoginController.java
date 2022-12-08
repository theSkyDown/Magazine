/*
 * author:eccentric
 * time:2022/11/9
 */
package com.eccentric.Controller;

import com.eccentric.DAO.UserDAO;
import com.eccentric.Starter.Main;
import com.eccentric.Utils.AlertUtil;
import com.eccentric.Utils.MD5Util;
import com.eccentric.pojo.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    @FXML
    public TextField phoneInput;

    @FXML
    PasswordField pwdInput;

    UserDAO dao = new UserDAO();

    /**
     * 登陆按钮
     */
    @FXML
    public void LoginBtn() throws Exception {
        String phone = phoneInput.getText().trim();
        String password = pwdInput.getText().trim();

        //拦截空表单
        if (phone.equals("") || password.equals("")){
            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
            return;
        }

        User user = dao.getUserByPhone(phone);
        if(user != null && MD5Util.getMD5(password).equals(user.getPassword())){
            String FXMLFile = "fxml/Magazine.fxml";
            if (user.getIsManager() == 1){
                System.out.println("超级管理员");
                FXMLFile = "fxml/ManageSys/Manage.fxml";
            }

            Main.current_user = user;
            System.out.println(Main.current_user);
            //关闭登陆页面
            Stage mainStage = (Stage)phoneInput.getScene().getWindow();
            mainStage.close();

            //登陆成功并打开主页面
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(FXMLFile));
            stage.setTitle("杂志订阅系统");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        }else{
            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"账号或密码错误");
        }

    }

    /**
     * 退出按钮
     */
    @FXML
    public void exit(){
        Stage stage = (Stage)phoneInput.getScene().getWindow();
        stage.close();
    }

    /**
     * 注册按钮
     */
    @FXML
    public void Register(){
        Stage stage = (Stage)phoneInput.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/register.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
