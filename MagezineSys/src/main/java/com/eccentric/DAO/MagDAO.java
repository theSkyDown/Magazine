/*
 * author:eccentric
 * time:2022/11/9
 */
package com.eccentric.DAO;

import com.eccentric.Utils.JDBCUtil;
import com.eccentric.pojo.Magazine;
import com.eccentric.pojo.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MagDAO {
    /**
     * 查询所有杂志信息
     * @return
     */
    public List<Magazine> getAllMagazines(){
        List<Magazine> magazines = new ArrayList<Magazine>();
        String sql = "select * from books";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Magazine m = JDBCUtil.resultSetToMag(resultSet);
                magazines.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return magazines;
    }


    /**
     * 通过名字的模糊信息查询
     * @param input
     * @return
     */
    public List<Magazine> findMagLikeByName(String input){
        List<Magazine> magazines = new ArrayList<Magazine>();
        String sql = "select * from books where book_name like ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        ResultSet resultSet = null;
        try {
            stmt.setString(1,"%"+input+"%");
            resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Magazine m = JDBCUtil.resultSetToMag(resultSet);
                magazines.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return magazines;
    }


    /**
     * 查询所有杂志的类型
     * @return
     */
    public List<String> getAllClassifies(){
        List<String> classifies = new ArrayList<String>();
        String sql = "select classify from books group by classify";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        try {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                classifies.add(resultSet.getString("classify"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return classifies;
    }


    /**
     * 通过杂志的类型查询杂志
     * @param classify
     * @return
     */
    public List<Magazine> getMagByClassify(String classify){
        List<Magazine> magazines = new ArrayList<Magazine>();
        String sql = "select * from books where classify = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        try {
            stmt.setString(1,classify);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Magazine m = JDBCUtil.resultSetToMag(resultSet);
                magazines.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return magazines;
    }


    /**
     * 通过用户的id去查询订单表，得到书籍的id来查询书籍
     * @param userId
     * @return
     */
    public List<Magazine> getMagazinesByUserId(Integer userId){
        String sql = "select * from books where id in(select book_id from orders where user_id = ?)";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        List<Magazine> list = new ArrayList<>();
        try {
            stmt.setInt(1,userId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                list.add(JDBCUtil.resultSetToMag(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 通过订单id查找订单
     * @param magId
     * @return
     */
    public Magazine getMagById(Integer magId){
        String sql = "select * from books where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Magazine magazine = null;
        try {
            stmt.setInt(1,magId);
            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()){
                magazine = JDBCUtil.resultSetToMag(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return magazine;
    }

    /**
     * 修改书籍信息
     * @param magazine
     * @return
     */
    public Boolean updateMag(Magazine magazine){
        String sql = "update books set book_name = ?,classify = ?,price = ? where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setString(1,magazine.getBookName());
            stmt.setString(2,magazine.getClassify());
            stmt.setDouble(3,magazine.getPrice());
            stmt.setInt(4,magazine.getId());
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 添加杂志
     * @param magazine
     * @return
     */
    public Boolean addMag(Magazine magazine){
        String sql = "insert into books values(null,?,?,?,?)";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setString(1,magazine.getBookName());
            stmt.setString(2,magazine.getImgSrc());
            stmt.setString(3,magazine.getClassify());
            stmt.setDouble(4,magazine.getPrice());
            res = stmt.executeUpdate()==1?true:false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public Boolean deleteMagById(Integer magId){
        String sql = "delete from books where id = ?";
        PreparedStatement stmt = JDBCUtil.getStmt(sql);
        Boolean res = null;
        try {
            stmt.setInt(1,magId);
            res = stmt.executeUpdate()==1?true:false;
            if (res){
                String sql2 = "delete from orders where book_id = ?";
                PreparedStatement stmt2 = JDBCUtil.getStmt(sql2);
                stmt2.setInt(1,magId);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;
    }


}
