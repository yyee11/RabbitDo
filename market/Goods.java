package com.example.todo.market;

public class Goods {
    private int goodsId;
    private int goodsIcon;
    private String goodsTitle;
    private String goodsPrice;

    public Goods() {
    }

    public Goods(int goodsId, int goodsIcon, String goodsTitle, String goodsPrice) {
        this.goodsId = goodsId;
        this.goodsIcon = goodsIcon;
        this.goodsTitle = goodsTitle;
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public int getGoodsIcon() {
        return goodsIcon;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

}
