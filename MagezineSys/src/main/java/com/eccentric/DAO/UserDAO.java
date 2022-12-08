/*
 * author:eccentric
 * time:2022/11/9
 */
package com.eccentric.DAO;

import com.eccentric.Utils.JDBCUtil;
import com.eccentric.Utils.MD5Util;
import com.eccentric.pojo.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {


    /**
     * 通过手机号查询用户
     * @param phone
     * @return
     */
    public User getUserByPhone(String phone){
        String sql = "select * from users where phone = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        User user = null;
        try {
            stmt.setString(1,phone);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                user = JDBCUtil.resultToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }


    /**
     * 添加用户
     * @param user
     * @return
     */
    public Boolean addUser(User user){
        String sql = "insert into users(id,username,phone,password,address,isManager) values(null,?,?,?,?,?)";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = false;
        try {
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPhone());
            stmt.setString(3,MD5Util.getMD5(user.getPassword()));
            stmt.setString(4,user.getAddress());
            stmt.setInt(5,user.getIsManager());
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }


    /**
     * 通过id修改用户信息
     * @param user
     * @return
     */
    public Boolean updateUser(User user){
        String sql = "update users set username = ?,address = ?,isManager = ? where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getAddress());
            stmt.setInt(3,user.getIsManager());
            stmt.setInt(4,user.getId());
            res = stmt.executeUpdate() == 1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 通过用户id更新密码
     * @param id
     * @param newPwd
     * @return
     */
    public Boolean updateUserPassword(Integer id,String newPwd){
        String sql = "update users set password = ? where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            String md5Pwd = MD5Util.getMD5(newPwd);
            stmt.setString(1,md5Pwd);
            stmt.setInt(2,id);
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }


    /**
     * 查询所有用户
     * @return
     */
    public List<User> getAllUser(){
        String sql = "select * from users";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        List<User> list = new ArrayList<>();
        try {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                User user = JDBCUtil.resultToUser(resultSet);
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 通过用户id查找用户
     * @param userId
     * @return
     */
    public User getUserById(Integer userId){
        String sql = "select * from users where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        User user = null;
        try {
            stmt.setInt(1,userId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                user = JDBCUtil.resultToUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    /**
     * 通过用户id删除用户
     * @param userId
     * @return
     */
    public Boolean deleteUserById(Integer userId){
        String sql = "delete from users where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setInt(1,userId);
            res = stmt.executeUpdate()==1?true:false;

            //如果用户删除成功则清空该用户的所有订单
            if (res){
                String sql2 = "delete from orders where user_id = ?";
                PreparedStatement stmt2 = JDBCUtil.getStmt(sql2);
                stmt2.setInt(1,userId);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

}
