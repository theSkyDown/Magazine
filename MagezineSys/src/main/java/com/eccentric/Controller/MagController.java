/*
 * author:eccentric
 * time:2022/11/8
 */
package com.eccentric.Controller;


import com.eccentric.DAO.MagDAO;
import com.eccentric.DAO.OrderDAO;
import com.eccentric.DAO.UserDAO;
import com.eccentric.Starter.Main;
import com.eccentric.Utils.AlertUtil;
import com.eccentric.Utils.MD5Util;
import com.eccentric.pojo.Magazine;
import com.eccentric.pojo.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import static javafx.scene.paint.Color.LIGHTGRAY;

public class MagController implements Initializable {
    /**
     * 初始化时查询所有数据并展示
     */
    public void initialize(URL location, ResourceBundle resources) {
        //将用户名字放入
        menuButton.setText(Main.current_user.getUsername());
        //将分类按钮放入布局中
        setButton(magDAO.getAllClassifies());
        //将查询的数据放入布局
        setGrid(magDAO.getAllMagazines());

        //监听键盘输入，键盘松开按键，触发搜索方法
        searchInput.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                search();
            }
        });

        //给“我的订阅”添加点击事件
        toOrders.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                toOrder();
            }
        });

    }

    private Integer chosenMagazineId;//选中杂志的id
    @FXML
    private MenuButton menuButton;//账户管理
    @FXML
    private VBox chosenMagazineCard;//详细信息卡片
    @FXML
    private Label magazineNameLabel;//杂志名称

    @FXML
    private Label magazinePriceLabel;//杂志价格

    @FXML
    private ImageView magazineImage;//杂志的图片

    @FXML
    private Label classify;//杂志的类型

    @FXML
    private GridPane grid;//存放杂志卡片的布局

    @FXML
    private TextField searchInput;//搜索输入框

    @FXML
    private HBox classifyButton;//分类按钮的布局

    @FXML
    private Label toOrders;//跳转到我已经订阅

    @FXML
    private ComboBox<Integer> comboBox;//选择订阅时间的选择框


    /**
     * 查询按钮的点击事件
     */
    @FXML
    public void search(){
        List<Magazine> magazines = magDAO.findMagLikeByName(searchInput.getText());
        setGrid(magazines);
    }

    /**
     * 退出按钮
     */
    @FXML
    public void logout(){
        Main.current_user = null;
        Stage stage = (Stage) chosenMagazineCard.getScene().getWindow();
        stage.close();
        try {
            new Main().start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改信息按钮
     */
    @FXML
    public void updateInf(){
        //VBox布局
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        Label title = new Label("修改信息");
        title.setFont(new Font(24));
        title.setTextFill(Color.WHITE);
        //两个输入框
        HBox usernameHBox = new HBox(10);
        Label nameLabel = new Label("修改用户名");
        nameLabel.setTextFill(Color.WHITE);
        TextField usernameUpdate = new TextField(Main.current_user.getUsername());
        usernameHBox.setAlignment(Pos.CENTER);
        usernameHBox.getChildren().setAll(nameLabel,usernameUpdate);

        HBox addressHBox = new HBox(10);
        Label addLabel = new Label("修改地址");
        addLabel.setTextFill(Color.WHITE);
        TextField addressUpdate = new TextField(Main.current_user.getAddress());
        addressHBox.setAlignment(Pos.CENTER);
        addressHBox.getChildren().setAll(addLabel,addressUpdate);

        vBox.getChildren().addAll(title,usernameHBox,addressHBox);


        Callback<ButtonType,ButtonType> alertCallback = new Callback<ButtonType, ButtonType>() {
            @Override
            public ButtonType call(ButtonType param) {
                if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                    //用户名不能为空
                    String username = usernameUpdate.getText();
                    if (username.equals("")){
                        AlertUtil.alertDisplay(Alert.AlertType.ERROR,"用户名不能为空");
                        return param;
                    }
                    Main.current_user.setUsername(username);
                    Main.current_user.setAddress(addressUpdate.getText());
                    userDAO.updateUser(Main.current_user);
                }else if (param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                    System.out.println("用户已取消");
                }
                menuButton.setText(Main.current_user.getUsername());
                return param;
            }
        };
        AlertUtil.alertForm(Alert.AlertType.INFORMATION, vBox,alertCallback);


    }


    /**
     * 修改密码按钮
     */
    @FXML
    public void resetPwd(){
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        Label title = new Label("修改密码");
        title.setFont(new Font(24));
        title.setTextFill(Color.WHITE);

        //三个输入框

        HBox originPwdHBox = new HBox(10);
        Label originLabel = new Label("原密码");
        originLabel.setTextFill(Color.WHITE);
        PasswordField originPwd = new PasswordField();
        originPwdHBox.setAlignment(Pos.CENTER);
        originPwdHBox.getChildren().setAll(originLabel,originPwd);

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

        vBox.getChildren().setAll(title,originPwdHBox,newPwdHBox,comPwdHBox);

        //提交或者取消
        Callback<ButtonType,ButtonType> alertCallback = new Callback<ButtonType, ButtonType>() {
            @Override
            public ButtonType call(ButtonType param) {
                if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                    try {
                        //拦截空表单
                        if (originPwd.getText().equals("") || newPwd.getText().equals("") || comPwd.getText().equals("")){
                            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
                            return param;
                        }
                        //拦截原密码错误
                        if (!MD5Util.getMD5(originPwd.getText()).equals(Main.current_user.getPassword())){
                            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"原密码错误");
                            return param;
                        }
                        //拦截两次密码不相同的情况
                        if (!comPwd.getText().equals(newPwd.getText())) {
                            AlertUtil.alertDisplay(Alert.AlertType.ERROR,"两次输入的密码不相同");
                            return param;
                        }
                        userDAO.updateUserPassword(Main.current_user.getId(),newPwd.getText());
                        AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"身份过期，请重新登陆");
                        logout();
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

    MagDAO magDAO = new MagDAO();
    OrderDAO orderDAO = new OrderDAO();

    UserDAO userDAO = new UserDAO();

    /**
     * 点击“我的订阅”后更新Grid布局
     */
    public void toOrder(){
        setGrid(magDAO.getMagazinesByUserId(Main.current_user.getId()));
    }

    /**
     * 设置详细信息卡片的数据
     * @param magazine
     */
    public void setChosenMagazineCard(Magazine magazine){
        //设置选中卡片的信息
        magazineNameLabel.setText(magazine.getBookName());
        magazinePriceLabel.setText("￥"+magazine.getPrice().toString()+"/月");
        ResourceBundle bundle = ResourceBundle.getBundle("FileHelp");
        String homePath = bundle.getString("HomePath");
        Image image = null;
        try {
            image = new Image(new File(homePath+magazine.getImgSrc()).toURI().toURL().toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        magazineImage.setImage(image);
        classify.setText("类型："+magazine.getClassify());
        //将选择杂志的id记录下来
        chosenMagazineId = magazine.getId();
        //选择时间的控件加数据
        List<Integer> monthList = new ArrayList<>();
        for (int i=1;i<=12;i++){
            monthList.add(i);
        }
        ObservableList<Integer> list = FXCollections.observableList(monthList);
        comboBox.setItems(list);
        comboBox.setVisibleRowCount(3);
        //更新订阅按钮
        updateButtonToChosenMagazine(Main.current_user.getId(),magazine.getId());

    }


    /**
     * 将数据放入到Grid布局中
     * @param magazines
     */
    public void setGrid(List<Magazine> magazines){
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for(int i=0;i<magazines.size();i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                itemController item = fxmlLoader.getController();
                item.setDate(magazines.get(i),this);

                if (column == 3){
                    column = 0;
                    row++;
                }

                grid.add(anchorPane,column++,row);
                //宽度
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                //高度
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
            //将第一本书的信息放到详细信息卡片中
            if (magazines.size()>0){
                setChosenMagazineCard(magazines.get(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将分类按钮放入布局中
     * @param classifies
     */
    public void setButton(final List<String> classifies){
        classifyButton.getChildren().clear();
        for (int i=0;i<classifies.size();i++){
            final String classify = classifies.get(i);
            final Button btn = new Button(classify);
            //给按钮增加样式
            btn.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 100;");
            btn.setPrefHeight(48);
            btn.setPrefWidth(135);
            btn.setFont(new Font(24));
            btn.setTextFill(LIGHTGRAY);
            //给按钮添加点击事件
            btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    setGrid(magDAO.getMagByClassify(classify));
                }
            });

            //间距
            Pane pane = new Pane();
            pane.setPrefHeight(99);
            pane.setPrefWidth(24);

            classifyButton.getChildren().addAll(btn,pane);
        }
        //删除最后一个间距
        classifyButton.getChildren().remove(classifies.size()*2-1);
    }


    /**
     * 更新详细卡片页面中的订阅按钮
     */
    public void updateButtonToChosenMagazine(Integer userId,Integer MagazineId){
        //订阅按钮在详细卡片的位置
        int index = chosenMagazineCard.getChildren().size()-2;
        //判断用户是否已经订阅该杂志
        Order order = orderDAO.getOrderByBookIdAndUserId(MagazineId,userId);
        //如果用户没有订阅此杂志
        Button SubBtn = new Button();
        //设置统一样式
        SubBtn.setPrefHeight(50);
        SubBtn.setPrefWidth(270);
        SubBtn.setFont(new Font(18));
        //没有订阅的样式如下
        if (order == null){
            comboBox.getSelectionModel().select(0);
            SubBtn.setText("订 阅");
            SubBtn.setStyle("-fx-background-radius: 100; -fx-background-color: #4a6c78;-fx-text-fill: white");
            //添加订阅的点击事件
            SubBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    LocalDateTime startTime = LocalDateTime.now();
                    LocalDateTime endTime = startTime.plusMonths(comboBox.getValue());
                    Order order = new Order(null,chosenMagazineId,userId,startTime,endTime);
                    if (orderDAO.addOrder(order)){
                        updateButtonToChosenMagazine(userId,MagazineId);
                        AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"订阅成功");
                    }else{
                        AlertUtil.alertDisplay(Alert.AlertType.ERROR,"订阅失败");

                    }
                }
            });
        }else{
            //更新按钮的样式
            SubBtn.setText("取 消 订 阅");
            SubBtn.setStyle("-fx-background-radius: 100;-fx-background-color: #e86969;-fx-text-fill: black;");
            //取消订阅的点击事件
            SubBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (orderDAO.removeOrderById(order.getId())) {
                        updateButtonToChosenMagazine(userId,MagazineId);
                        AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"取消成功");
                    }else{
                        AlertUtil.alertDisplay(Alert.AlertType.ERROR,"取消失败");
                    }
                }
            });
            //将订阅时间更改为用户订阅剩余的时间
            Period between = Period.between(LocalDate.now(), order.getEndTime().toLocalDate());
            if (between.getYears() >= 1){
                comboBox.getSelectionModel().select(11);
            }else{
                int month = between.getMonths() - 1;
                //如果相差的时间为负数，说明已经过期了
                if (between.isNegative()) {
                    orderDAO.removeOrderById(order.getId());
                    updateButtonToChosenMagazine(userId,MagazineId);
                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"已过期");
                    toOrder();
                    return;
                }else if (between.getMonths()<1){
                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"订阅时间不足一个月");
                    month = 0;
                }
                comboBox.getSelectionModel().select(month);
            }



        }
        //更新按钮
        chosenMagazineCard.getChildren().remove(index);
        chosenMagazineCard.getChildren().add(index,SubBtn);
    }

}
