package com.huongbien.config;

import com.huongbien.dao.TableDAO;

public class Variable {
    //OrderTableController
    //--
    public static int floor = TableDAO.getInstance().getTopFloor().getFloor();
    public static String status = "Trạng thái";
    public static String tableTypeName = "Loại bàn";
    public static String seats = "Số chỗ";
    //---
    public static String tableVipID = "LB002";
    public static double tablePrice = 100000;
    //---
    public static String[] partyTypesArray = {"Gia đình", "Sinh nhật", "Đám cưới", "Đám dỗ", "Đám hỏi", "Kỷ niệm", "Hội nghị", "Hẹn hò", "Khác..."};

    //Password WIFI
    public static String PASSWORD_WIFI = "12345678";
}
