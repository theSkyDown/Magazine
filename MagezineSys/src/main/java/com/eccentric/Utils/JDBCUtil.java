/*
 * author:eccentric
 * time:2022/11/9
 */
package com.eccentric.Utils;

import com.eccentric.pojo.Magazine;
import com.eccentric.pojo.Order;
import com.eccentric.pojo.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class JDBCUtil {
    /**
     * 用于获取PreparedStatement
     * @param sql
     * @return
     */
    public static PreparedStatement getStmt(String sql){
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(bundle.getString("driver"));
            conn = DriverManager.getConnection(bundle.getString("url"),bundle.getString("username"),bundle.getString("password"));
            stmt = conn.prepareStatement(sql);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stmt;
    }

    /**
     * 将resultSet类型转换成Magazine类型
     * @param resultSet
     * @return
     */
    public static Magazine resultSetToMag(ResultSet resultSet){
        Magazine m = new Magazine();
        try {
            m.setId(resultSet.getInt("id"));
            m.setBookName(resultSet.getString("book_name"));
            m.setImgSrc(resultSet.getString("img_src"));
            m.setClassify(resultSet.getString("classify"));
            m.setPrice(resultSet.getDouble("price"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    /**
     * 将resultSet类型转换成User类型
     * @param resultSet
     * @return
     */
    public static User resultToUser(ResultSet resultSet){
        User user = new User();
        try {
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPhone(resultSet.getString("phone"));
            user.setPassword(resultSet.getString("password"));
            user.setAddress(resultSet.getString("address"));
            user.setIsManager(resultSet.getInt("isManager"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /**
     * 将resultSet类型转换成Order类型
     * @param resultSet
     * @return
     */
    public static Order resultToOrder(ResultSet resultSet){
        Order order = new Order();
        try {
            order.setId(resultSet.getInt("id"));
            order.setBookId(resultSet.getInt("book_id"));
            order.setUserId(resultSet.getInt("user_id"));
            Date start_time_date = resultSet.getDate("start_time");
            Time start_time_time = resultSet.getTime("start_time");
            LocalDateTime start_time = LocalDateTime.of(start_time_date.toLocalDate(),start_time_time.toLocalTime());
            order.setStartTime(start_time);
            Date end_time_date = resultSet.getDate("end_time");
            Time end_time_time = resultSet.getTime("end_time");
            LocalDateTime end_time = LocalDateTime.of(end_time_date.toLocalDate(),end_time_time.toLocalTime());
            order.setEndTime(end_time);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

}
