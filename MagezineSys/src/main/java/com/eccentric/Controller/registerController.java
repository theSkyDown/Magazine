/*
 * author:eccentric
 * time:2022/11/10
 */
package com.eccentric.Controller;


import com.eccentric.DAO.UserDAO;
import com.eccentric.Utils.AlertUtil;
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
import java.util.Random;
import java.util.regex.Pattern;


public class registerController{

    @FXML
    private TextField phoneInput;

    @FXML
    private PasswordField pwdInput;

    @FXML
    private PasswordField pwdConInput;

    UserDAO dao = new UserDAO();

    /**
     * 返回登陆按钮
     */
    @FXML
    public void backLogin(){
        Stage stage = (Stage)phoneInput.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Login.fxml"));
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 退出按钮
     */
    @FXML
    public void exit(){
        Stage stage = (Stage) phoneInput.getScene().getWindow();
        stage.close();
    }


    /**
     * 注册按钮
     */
    @FXML
    public void RegisterBtn(){
        String phone = phoneInput.getText().trim();
        String password = pwdInput.getText().trim();
        String passwordConfirm = pwdConInput.getText().trim();

        //拦截空表单
        if (phone.equals("") || password.equals("") || passwordConfirm.equals("")){
            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
            return;
        }
        //拦截两次密码不相同
        if (!passwordConfirm.equals(password)){
            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"两次输入的密码不同");
            return;
        }
        //拦截手机号格式
        if(!Pattern.matches("^1[3456789]\\d{9}",phone)){
            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"请输入正确的手机格式");
            return;
        }
        //拦截账号已存在
        if (dao.getUserByPhone(phone) != null) {
            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"账号已经存在");
            return;
        }

        //生成用户名
        Random random = new Random();
        String username = "user_" + random.nextInt(100000);
        User user = new User(null,username,phone,password,null,0);
        //添加用户
        dao.addUser(user);
        //跳转至登陆页面
        backLogin();
        AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"注册成功");
    }

}
