/*
 * author:eccentric
 * time:2022/11/11
 */
package com.eccentric.DAO;

import com.eccentric.Utils.JDBCUtil;
import com.eccentric.pojo.Magazine;
import com.eccentric.pojo.Order;
import com.eccentric.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO {

    /**
     * 添加订单
     * @param order
     * @return
     */
    public Boolean addOrder(Order order){
        String sql = "insert into orders values(null,?,?,?,?)";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setInt(1,order.getUserId());
            stmt.setInt(2,order.getBookId());
            stmt.setString(3,order.getStartTime().toString());
            stmt.setString(4,order.getEndTime().toString());
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;

    }


    /**
     * 通过用户id和杂志id查询订单
     * @param bookId
     * @param userId
     * @return
     */
    public Order getOrderByBookIdAndUserId(Integer bookId,Integer userId){
        String sql = "select * from orders where book_id = ? and user_id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Order order = null;
        try {
            stmt.setInt(1,bookId);
            stmt.setInt(2,userId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                order = JDBCUtil.resultToOrder(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    /**
     * 通过id删除订单
     * @param id
     * @return
     */
    public Boolean removeOrderById(Integer id){
        String sql = "delete from orders where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setInt(1,id);
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 通过杂志id计算订阅人数
     * @param MagId
     * @return
     */
    public Integer getMagSubNum(Integer MagId){
        String sql = "select count(*) as sum from orders where book_id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Integer res = null;
        try {
            stmt.setInt(1,MagId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                res = resultSet.getInt("sum");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 查询所有订单
     * @return
     */
    public List<Order> getAllOrder(){
        String sql = "select * from orders";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        List<Order> list = new ArrayList<>();
        try {
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Order order = JDBCUtil.resultToOrder(resultSet);
                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 通过订单id查找订单
     * @param orderId
     * @return
     */
    public Order getOrderById(Integer orderId){
        String sql = "select * from orders where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Order order = null;
        try {
            stmt.setInt(1,orderId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                order = JDBCUtil.resultToOrder(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }


    /**
     * 通过id修改订单的信息
     * @param order
     * @return
     */
    public Boolean updateOrder(Order order){
        String sql = "update orders set user_id = ?,book_id = ?,start_time = ?,end_time = ? where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setInt(1,order.getUserId());
            stmt.setInt(2,order.getBookId());
            stmt.setString(3,order.getStartTime().toString());
            stmt.setString(4,order.getEndTime().toString());
            stmt.setInt(5,order.getId());
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

}
