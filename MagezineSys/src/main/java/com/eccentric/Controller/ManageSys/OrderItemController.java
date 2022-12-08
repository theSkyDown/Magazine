/*
 * author:eccentric
 * time:2022/11/13
 */
package com.eccentric.Controller.ManageSys;

import com.eccentric.DAO.MagDAO;
import com.eccentric.DAO.OrderDAO;
import com.eccentric.DAO.UserDAO;
import com.eccentric.Utils.AlertUtil;
import com.eccentric.pojo.Magazine;
import com.eccentric.pojo.Order;
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

import java.time.LocalDateTime;
import java.time.LocalTime;


public class OrderItemController {
    UserDAO userDAO = new UserDAO();
    MagDAO magDAO = new MagDAO();
    OrderDAO orderDAO = new OrderDAO();

    @FXML
    public Label username;

    @FXML
    public Label magName;

    @FXML
    public Label startTime;

    @FXML
    public Label endTime;

    @FXML
    public Button updateBtn;

    @FXML
    public Button deleteBtn;


    /**
     * 放入数据
     * @param order
     */
    public void setDate(Order order,ManageController mc){
        //设置用户名
        username.setText(userDAO.getUserById(order.getUserId()).getUsername());
        //设置杂志名称
        magName.setText(magDAO.getMagById(order.getBookId()).getBookName());
        //设置起始时间
        startTime.setText(order.getStartTime().toString());
        //设置截止时间
        endTime.setText(order.getEndTime().toString());
        //修改按钮点击事件
        updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox vBox = new VBox(20);
                vBox.setAlignment(Pos.CENTER);

                //设置标题
                Label title = new Label("修改订单");
                title.setFont(new Font(24));
                title.setTextFill(Color.WHITE);

                //设置用户
                HBox hBox1 = new HBox(10);
                Label userLabel = new Label("用户");
                userLabel.setTextFill(Color.WHITE);
                ComboBox<User> userComboBox = new ComboBox<>();
                userComboBox.setItems(FXCollections.observableList(userDAO.getAllUser()));
                userComboBox.getSelectionModel().select(userDAO.getUserById(order.getUserId()));
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
                magComboBox.getSelectionModel().select(magDAO.getMagById(order.getBookId()));
                magComboBox.setPrefWidth(200);
                magComboBox.setVisibleRowCount(5);
                hBox2.setAlignment(Pos.CENTER);
                hBox2.getChildren().setAll(magLabel,magComboBox);

                //设置开始时间
                HBox hBox3 = new HBox(10);
                Label startTimeLabel = new Label("开始时间");
                startTimeLabel.setTextFill(Color.WHITE);
                DatePicker startTime = new DatePicker();
                startTime.setValue(order.getStartTime().toLocalDate());
                hBox3.setAlignment(Pos.CENTER);
                hBox3.getChildren().setAll(startTimeLabel,startTime);

                //设置结束时间
                HBox hBox4 = new HBox(10);
                Label endTimeLabel = new Label("结束时间");
                endTimeLabel.setTextFill(Color.WHITE);
                DatePicker endTime = new DatePicker();
                endTime.setValue(order.getEndTime().toLocalDate());
                hBox4.setAlignment(Pos.CENTER);
                hBox4.getChildren().setAll(endTimeLabel,endTime);

                vBox.getChildren().setAll(title,hBox1,hBox2,hBox3,hBox4);

                Callback<ButtonType,ButtonType> callback = new Callback<ButtonType, ButtonType>() {
                    @Override
                    public ButtonType call(ButtonType param) {
                        if (param.getButtonData() == ButtonBar.ButtonData.FINISH){
                            Integer userId = userComboBox.getValue().getId();
                            Integer magId = magComboBox.getValue().getId();
                            //拦截时间未填写
                            if (startTime.getValue() == null || endTime.getValue() == null) {
                                mc.updatePage();
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"请选择时间");
                                return param;
                            }
                            LocalDateTime start_time = LocalDateTime.of(startTime.getValue(), order.getStartTime().toLocalTime());
                            LocalDateTime end_time = LocalDateTime.of(endTime.getValue(),order.getEndTime().toLocalTime());
                            //拦截用户已订阅
                            Order temp = orderDAO.getOrderByBookIdAndUserId(magId, userId);
                            if (temp!=null && !temp.getId().equals(order.getId())){
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"该用户已经订阅了该书籍");
                                return param;
                            }
                            order.setUserId(userId);
                            order.setBookId(magId);
                            order.setStartTime(start_time);
                            order.setEndTime(end_time);
                            if (orderDAO.updateOrder(order)) {
                                setDate(order,mc);
                                AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"修改成功");
                            }else{
                                AlertUtil.alertDisplay(Alert.AlertType.ERROR,"修改失败");
                            }
                        }else if (param.getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE){
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
                if (orderDAO.removeOrderById(order.getId())) {
                    mc.updatePage();
                    AlertUtil.alertDisplay(Alert.AlertType.INFORMATION,"删除成功");
                }else{
                    AlertUtil.alertDisplay(Alert.AlertType.ERROR,"删除失败");
                }
            }
        });
    }
}
