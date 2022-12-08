/*
 * author:eccentric
 * time:2022/11/9
 */
package com.eccentric.pojo;

public class Magazine {
    private Integer id;
    private String bookName;
    private String imgSrc;
    private String classify;
    private Double price;

    public Magazine() {
    }

    public Magazine(Integer id, String bookName, String imgSrc, String classify, Double price) {
        this.id = id;
        this.bookName = bookName;
        this.imgSrc = imgSrc;
        this.classify = classify;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", classify='" + classify + '\'' +
                ", price=" + price +
                '}';
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
