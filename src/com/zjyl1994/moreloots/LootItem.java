/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zjyl1994.moreloots;

/**
 *
 * @author zjyl1
 */
public class LootItem {
    private final String itemID;
    private final Integer itemNum;
    
    LootItem(String ID,Integer Num){
        this.itemID = ID;
        this.itemNum = Num;
    }
    
    public String getItemID() {
        return itemID;
    }

    public Integer getItemNum() {
        return itemNum;
    }
}
