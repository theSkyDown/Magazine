/*
 * author:eccentric
 * time:2022/11/13
 */
package com.eccentric.Controller.ManageSys;

import com.eccentric.DAO.MagDAO;
import com.eccentric.DAO.OrderDAO;
import com.eccentric.DAO.UserDAO;
import com.eccentric.Starter.Main;
import com.eccentric.Utils.AlertUtil;
import com.eccentric.pojo.Magazine;
import com.eccentric.pojo.Order;
import com.eccentric.pojo.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {
    MagDAO magDAO = new MagDAO();
    UserDAO userDAO = new UserDAO();
    OrderDAO orderDAO = new OrderDAO();

    //用于判断是哪个管理页面
    public static final Integer ManageMag = 1;//管理杂志页面
    public static final Integer ManageUser = 2;//管理用户页面
    public static final Integer ManageOrder = 3;//管理订单页面
    //当前页面
    public static Integer currentPage = null;

    /**
     * 初始化
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //设置默认的页面
        ManageMagBtn();
    }

    /**
     * 切换管理杂志页面
     */
    @FXML
    public void ManageMagBtn(){
        currentPage = ManageMag;
        updatePage();
    }

    /**
     * 切换管理用户页面
     */
    @FXML
    public void ManageUserBtn(){
        currentPage = ManageUser;
        updatePage();
    }

    /**
     * 切换管理订单页面
     */
    @FXML
    public void ManageOrderBtn(){
        currentPage = ManageOrder;
        updatePage();
    }

    @FXML
    public Button addBtn;//添加按钮
    /**
     * 退出按钮
     */
    @FXML
    public void logout(){
        Main.current_user = null;
        Stage stage = (Stage)grid.getScene().getWindow();
        stage.close();
        try {
            new Main().start(new Stage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public GridPane grid;//存放主要内容的布局


    /**
     * 更新当前页面
     */
    public void updatePage(){
        //管理杂志
        if (currentPage.equals(ManageMag)){
            //给添加按钮设置点击事件
            addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    VBox vBox = new VBox(20);
                    vBox.setAlignment(Pos.CENTER);
                    //标题
                    Label title = new Label("添加杂志");
                    title.setFont(new Font(24));
                    title.setTextFill(Color.WHITE);

                    HBox hBox1 = new HBox(10);
                    Label MagNameLabel = new Label("杂志名称");
                    MagNameLabel.setTextFill(Color.WHITE);
                    TextField updateMagName = new TextField();
                    hBox1.setAlignment(Pos.CENTER);
                    hBox1.getChildren().setAll(MagNameLabel,updateMagName);

                    HBox hBox2 = new HBox(10);
                    Label MagClassifyLabel = new Label("杂志类型");
                    MagClassifyLabel.setTextFill(Color.WHITE);
                    ComboBox<String> comboBox = new ComboBox<>();
                    comboBox.setItems(FXCollections.observableList(magDAO.getAllClassifies()));
                    comboBox.getSelectionModel().select(0);
                    hBox2.setAlignment(Pos.CENTER);
                    hBox2.getChildren().setAll(MagClassifyLabel,comboBox);

                    HBox hBox3 = new HBox(10);
                    Label MagPriceLabel = new Label("杂志价格");
                    MagPriceLabel.setTextFill(Color.WHITE);
                    TextField updatePrice = new TextField();
                    hBox3.setAlignment(Pos.CENTER);
                    hBox3.getChildren().setAll(MagPriceLabel,updatePrice);

                    HBox hBox4 = new HBox(10);
                    Label imgLabel = new Label("杂志图片");
                    imgLabel.setTextFill(Color.WHITE);
                    Button btn = new Button("load");
                    final File[] openFile = {null};
                    btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            FileChooser imgChooser = new FileChooser();
                            imgChooser.setTitle("选择图片");
                            imgChooser.getExtensionFilters().setAll(
                                    new FileChooser.ExtensionFilter("PNG","*.png")
                            );
                            openFile[0] = imgChooser.showOpenDialog(addBtn.getScene().getWindow());
                        }
                    });
                    hBox4.setAlignment(Pos.CENTER);
                    hBox4.getChildren().setAll(imgLabel,btn);


                    vBox.getChildren().setAll(title,hBox1,hBox3,hBox2,hBox4);

                    //按钮点击事件
                    Callback<ButtonType,ButtonType> alertCallback = new Callback<ButtonType, ButtonType>() {
                        @Override
                        public ButtonType call(ButtonType param) {
                            if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                                String magName = updateMagName.getText();
                                String priceText = updatePrice.getText();
                                String classify = comboBox.getValue();
                                //拦截空表单
                                if (magName.equals("") || priceText.equals("") || openFile[0] == null){
                                    updatePage();
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
                                    return param;
                                }
                                //拦截输入的不是正浮点数
                                if (!Pattern.matches("\\d*(\\.?)\\d*",priceText)){
                                    updatePage();
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"输入的不是数字");
                                    return param;
                                }
                                //将文件复制到指定目录下
                                ResourceBundle bundle = ResourceBundle.getBundle("FileHelp");
                                String homePath = bundle.getString("HomePath");
                                String imgSrc = "new/" + magName + new Date().getTime() + ".png";
                                File saveFile = new File(homePath + imgSrc);
                                Thread t1 = new Thread(() -> {
                                    try {
                                        Files.copy(openFile[0].toPath(), saveFile.toPath());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }, "fileCopy");
                                t1.start();
                                try {
                                    t1.join();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Magazine magazine = new Magazine(null,magName,imgSrc,classify,Double.valueOf(priceText));
                                if (magDAO.addMag(magazine)) {
                                    updatePage();
                                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"添加成功");
                                }else{
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"添加失败");
                                }

                            }else if(param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
                                System.out.println("用户取消");
                            }
                            return param;
                        }
                    };

                    AlertUtil.alertForm(Alert.AlertType.INFORMATION,vBox,alertCallback);

                }
            });
            //将数据放入布局中
            setMagToGird(magDAO.getAllMagazines());
        //管理用户
        }else if (currentPage.equals(ManageUser)){
            //给添加按钮设置点击事件
            addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("用户添加");

                    VBox vBox = new VBox(20);
                    vBox.setAlignment(Pos.CENTER);
                    //标题
                    Label title = new Label("添加用户");
                    title.setFont(new Font(24));
                    title.setTextFill(Color.WHITE);

                    //输入框
                    HBox hBox1 = new HBox(10);
                    Label usernameLabel = new Label("用户名");
                    usernameLabel.setTextFill(Color.WHITE);
                    TextField usernameInput = new TextField();
                    hBox1.setAlignment(Pos.CENTER);
                    hBox1.getChildren().setAll(usernameLabel,usernameInput);

                    HBox hBox2 = new HBox(10);
                    Label phoneLabel = new Label("电话号");
                    phoneLabel.setTextFill(Color.WHITE);
                    TextField phoneInput = new TextField();
                    hBox2.setAlignment(Pos.CENTER);
                    hBox2.getChildren().setAll(phoneLabel,phoneInput);

                    HBox hBox3 = new HBox(10);
                    Label isManagerLabel = new Label("是否为管理员");
                    isManagerLabel.setTextFill(Color.WHITE);
                    ComboBox<String> comboBox = new ComboBox<>();
                    comboBox.setItems(FXCollections.observableArrayList("否","是"));
                    comboBox.getSelectionModel().select(0);
                    hBox3.setAlignment(Pos.CENTER);
                    hBox3.getChildren().setAll(isManagerLabel,comboBox);

                    HBox hBox4 = new HBox(10);
                    Label addressLabel = new Label("地址");
                    addressLabel.setTextFill(Color.WHITE);
                    TextField addressInput = new TextField();
                    hBox4.setAlignment(Pos.CENTER);
                    hBox4.getChildren().setAll(addressLabel,addressInput);

                    vBox.getChildren().setAll(title,hBox1,hBox2,hBox4,hBox3);

                    //按钮点击事件
                    Callback<ButtonType,ButtonType> alertCallback = new Callback<ButtonType, ButtonType>() {
                        @Override
                        public ButtonType call(ButtonType param) {
                            if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                                String username = usernameInput.getText();
                                String phone = phoneInput.getText();
                                String address = addressInput.getText();
                                Integer isManager = comboBox.getValue().equals("是")?1:0;
                                //拦截空表单
                                if (username.equals("") || phone.equals("")){
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"表单为空");
                                    return param;
                                }
                                //拦截手机号格式错误
                                if (!Pattern.matches("^1[3456789]\\d{9}",phone)){
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"手机格式错误");
                                    return param;
                                }
                                //拦截手机号已经存在
                                if (userDAO.getUserByPhone(phone) != null){
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"手机号已经存在");
                                    return param;
                                }

                                User user = new User(null,username,phone,"123",address,isManager);
                                if (userDAO.addUser(user)) {
                                    ManageUserBtn();
                                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"添加成功");
                                }else{
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"添加失败");
                                }


                            } else if (param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE) {
                                System.out.println("用户取消");
                            }
                            return param;
                        }
                    };

                    AlertUtil.alertForm(Alert.AlertType.INFORMATION,vBox,alertCallback);
                }
            });
            setUserToGrid(userDAO.getAllUser());
        //管理订单
        }else if (currentPage.equals(ManageOrder)){
            //给添加按钮设置点击事件
            addBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    VBox vBox = new VBox(20);
                    vBox.setAlignment(Pos.CENTER);

                    //设置标题
                    Label title = new Label("添加订单");
                    title.setFont(new Font(24));
                    title.setTextFill(Color.WHITE);

                    //设置用户
                    HBox hBox1 = new HBox(10);
                    Label userLabel = new Label("用户");
                    userLabel.setTextFill(Color.WHITE);
                    ComboBox<User> userComboBox = new ComboBox<>();
                    userComboBox.setItems(FXCollections.observableList(userDAO.getAllUser()));
                    userComboBox.getSelectionModel().select(0);
                    userComboBox.setPrefWidth(200);
                    userComboBox.setVisibleRowCount(5);
                    hBox1.setAlignment(Pos.CENTER);
                    hBox1.getChildren().setAll(userLabel,userComboBox);

                    //设置杂志
                    HBox hBox2 = new HBox(10);
                    Label magLabel = new Label("杂志");
                    magLabel.setTextFill(Color.WHITE);
                    ComboBox<Magazine> magComboBox = new ComboBox<>();
                    magComboBox.setItems(FXCollections.observableList(magDAO.getAllMagazines()));
                    magComboBox.getSelectionModel().select(0);
                    magComboBox.setPrefWidth(200);
                    magComboBox.setVisibleRowCount(5);
                    hBox2.setAlignment(Pos.CENTER);
                    hBox2.getChildren().setAll(magLabel,magComboBox);

                    //设置开始时间
                    HBox hBox3 = new HBox(10);
                    Label startTimeLabel = new Label("开始时间");
                    startTimeLabel.setTextFill(Color.WHITE);
                    DatePicker startTime = new DatePicker();
                    hBox3.setAlignment(Pos.CENTER);
                    hBox3.getChildren().setAll(startTimeLabel,startTime);

                    //设置结束时间
                    HBox hBox4 = new HBox(10);
                    Label endTimeLabel = new Label("结束时间");
                    endTimeLabel.setTextFill(Color.WHITE);
                    DatePicker endTime = new DatePicker();
                    hBox4.setAlignment(Pos.CENTER);
                    hBox4.getChildren().setAll(endTimeLabel,endTime);

                    vBox.getChildren().setAll(title,hBox1,hBox2,hBox3,hBox4);

                    //按钮点击事件
                    Callback<ButtonType,ButtonType> callback = new Callback<ButtonType, ButtonType>() {
                        @Override
                        public ButtonType call(ButtonType param) {
                            if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                                Integer userId = userComboBox.getValue().getId();
                                Integer magId = magComboBox.getValue().getId();

                                //拦截时间未填写
                                if (startTime.getValue() == null || endTime.getValue() == null) {
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"请选择时间");
                                    return param;
                                }

                                LocalDateTime start_time = LocalDateTime.of(startTime.getValue(), LocalTime.now());
                                LocalDateTime end_time = LocalDateTime.of(endTime.getValue(),LocalTime.now());
                                //拦截用户已经订阅了
                                if (orderDAO.getOrderByBookIdAndUserId(magId, userId) != null){
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"该用户已经订阅了该书籍");
                                    return param;
                                }
                                Order order = new Order(null,magId,userId,start_time,end_time);
                                if (orderDAO.addOrder(order)) {
                                    updatePage();
                                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"添加成功");
                                }else{
                                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"添加失败");
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
            setOrderToGrid(orderDAO.getAllOrder());
        }else{
            System.exit(0);
        }
    }



    /**
     * 将杂志放入滚动布局中
     * @param magazines
     */
    public void setMagToGird(List<Magazine> magazines){
        grid.getChildren().clear();
        int row = 1;
        try {
            for (int i=0;i< magazines.size();i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/ManageSys/MagItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                MagItemController controller = fxmlLoader.getController();
                controller.setDate(magazines.get(i),this);

                grid.add(anchorPane,0,row++);

                grid.setPrefHeight(200);
                grid.setPrefWidth(1000);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    /**
     * 将用户放入滚动布局
     * @param users
     */
    public void setUserToGrid(List<User> users){
        grid.getChildren().clear();
        int row = 1;
        try {
            for (int i=1;i<users.size();i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/ManageSys/UserItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                UserItemController controller = fxmlLoader.getController();
                controller.setDate(users.get(i),this);

                grid.add(anchorPane,0,row++);

                grid.setPrefHeight(200);
                grid.setPrefWidth(1000);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 将订单放入滚动布局
     * @param orders
     */
    public void setOrderToGrid(List<Order> orders){
        grid.getChildren().clear();
        int row = 1;
        try {
            for (int i=0;i< orders.size();i++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxml/ManageSys/OrderItem.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                OrderItemController controller = fxmlLoader.getController();
                controller.setDate(orders.get(i),this);

                grid.add(anchorPane,0,row++);

                grid.setPrefHeight(200);
                grid.setPrefWidth(1000);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
