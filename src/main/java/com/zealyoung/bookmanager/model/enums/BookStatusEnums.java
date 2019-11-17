package com.zealyoung.bookmanager.model.enums;

/**
 * @Author:Zealyoung
 */
public enum  BookStatusEnums {

    NORMAL(0),//正常
    DELETE(1);//删除

    private int value;

    BookStatusEnums(int value){
        this.value = value;
    }

    public int getValue(){
       return value;
    }
}
