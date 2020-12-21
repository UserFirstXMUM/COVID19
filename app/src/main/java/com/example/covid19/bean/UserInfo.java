package com.example.covid19.bean;

import com.example.covid19.R;

import java.util.ArrayList;

public class UserInfo {
    public long rowid;
    public int xuhao;//id
    public String username;
    public int age;
    public String email;
    public boolean manager;
    public Boolean gender;
    public String birthday;
    public String register_time;
    public String phone;
    public String password;
    public String head_portrait; // 小图的保存路径
    public int portrait; // 小图的资源编号

    public UserInfo() {
        rowid = 0L;
        xuhao = 0;
        username = "";
        age = 0;
        email = "";
        manager = false;
        birthday="";
        gender=false;
        register_time = "";
        phone = "";
        password = "";
        head_portrait = "";
        portrait = 0;
    }

    // 声明一个手机商品的名称数组
    private static String[] mUserNameArray = {
            "Jony",
            "Misy",
            "Bob",
            "Tom",
            "Henry"
    };
    private static int[] mAgeArray = {20,30,40,16,45};

    private static String[] mEmailArray = {
            "SWE1709001@xmu.edu.my", "SWE1709024@xmu.edu.my", "SWE1709133@xmu.edu.my", "SWE1709128@xmu.edu.my", "SWE1709118@xmu.edu.my"
    };
    private static boolean[] mManageArray = {true,false,false,true,false};
    private static String[] mBirthdayArray= {
            "1999/07/05", "1999/07/05", "2000/04/25", "1997/08/05", "1991/10/05"
    };
    private static boolean[] mGenderArray = {true,false,false,true,false};
    private static String[] mRTimeArray = {
            "2019-06-13 06:53:29",
            "2019-06-13 16:13:36",
            "2019-03-10 08:49:56",
            "2018-06-13 06:45:00",
            "2019-06-12 17:43:34"};
    private static String[] mPhoneArray = {
            "0172949072", "0172929872", "0172942472", "0172255872", "0172912872"
    };
    private static String[] mPasswordArray = {
            "123456", "rt0172929872", "eg0172942472", "sf0172255872", "ch0172912872"
    };
    // 声明一个手机商品的小图数组
    private static int[] mThumbArray = {
            R.drawable.girl_s,R.drawable.duck_s,R.drawable.labi_s,R.drawable.cat_s,R.drawable.pig_s
    };

    // 获取默认的手机信息列表
    public static ArrayList<UserInfo> getDefaultList() {
        ArrayList<UserInfo> usersList = new ArrayList<UserInfo>();
        for (int i = 0; i < mUserNameArray.length; i++) {
            UserInfo info = new UserInfo();
            info.username = mUserNameArray[i];
            info.age = mAgeArray[i];
            info.email = mEmailArray[i];
            info.manager =mManageArray[i];
            info.birthday = mBirthdayArray[i];
            info.gender = mGenderArray[i];
            info.register_time = mRTimeArray[i];
            info.password=mPasswordArray[i];
            info.phone = mPhoneArray[i];
            info.portrait = mThumbArray[i];
            usersList.add(info);
        }
        return usersList;
    }
}
