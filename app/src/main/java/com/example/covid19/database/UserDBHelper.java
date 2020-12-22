package com.example.covid19.database;

import java.util.ArrayList;

import com.example.covid19.Travel;
import com.example.covid19.bean.UserInfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class UserDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "UserDBHelper";
    private static final String DB_NAME = "user.db"; // 数据库的名称
    private static final int DB_VERSION = 1; // 数据库的版本号
    private static UserDBHelper mHelper = null; // 数据库帮助器的实例
    private SQLiteDatabase mDB = null; // 数据库的实例
    public static final String TABLE_NAME = "user_info"; // 表的名称
    public static final String TABLE_TRAVEL="Travel";
    public static final String TABLE_T_N="name";
    public static final String TABLE_T_A="address";
    public static final String TABLE_T_D="date";

    public UserDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private UserDBHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static UserDBHelper getInstance(Context context, int version) {
        if (version > 0 && mHelper == null) {
            mHelper = new UserDBHelper(context, version);
        } else if (mHelper == null) {
            mHelper = new UserDBHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }
    }

    // 创建数据库，执行建表语句
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        String drop_sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        Log.d(TAG, "drop_sql:" + drop_sql);
        db.execSQL(drop_sql);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                + "username VARCHAR NOT NULL," + "age INTEGER NOT NULL,"
                + "email VARCHAR NOT NULL," + "manager INTEGER NOT NULL," + "gender INTEGER NOT NULL," + "birthday VARCHAR," + "register_time VARCHAR NOT NULL"
                //演示数据库升级时要先把下面这行注释
                + ",phone VARCHAR NOT NULL" + ",password VARCHAR NOT NULL" + ",head_portrait VARCHAR" + ",portrait INTEGER"
                + ");";
        Log.d(TAG, "create_sql:" + create_sql);
        db.execSQL(create_sql);
        String create_sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,"
                + "username VARCHAR NOT NULL," + "age INTEGER NOT NULL,"
                + "email VARCHAR NOT NULL," + "manager INTEGER NOT NULL," + "gender INTEGER NOT NULL," + "birthday VARCHAR," + "register_time VARCHAR NOT NULL"
                //演示数据库升级时要先把下面这行注释
                + ",phone VARCHAR NOT NULL" + ",password VARCHAR NOT NULL" + ",head_portrait VARCHAR" + ",portrait INTEGER"
                + ");";
        Log.d(TAG, "create_sql:" + create_sql);
        db.execSQL(create_sql);
        db.execSQL("create table " + TABLE_TRAVEL + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR,ADDRESS TEXT,DATE TEXT)");
    }


    // 修改数据库，执行表结构变更语句
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        if (newVersion > 1) {
            //Android的ALTER命令不支持一次添加多列，只能分多次添加
            String alter_sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + "phone VARCHAR;";
            Log.d(TAG, "alter_sql:" + alter_sql);
            db.execSQL(alter_sql);
            alter_sql = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + "password VARCHAR;";
            Log.d(TAG, "alter_sql:" + alter_sql);
            db.execSQL(alter_sql);
        }
    }

    public Cursor getTravelRecord()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * From " + TABLE_TRAVEL, null);
        return res;
    }
    public void addTravel(String name,String address,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * From " + TABLE_TRAVEL, null);
        ContentValues cv= new ContentValues();
        cv.put(TABLE_T_A,address);
        cv.put(TABLE_T_N,name);
        cv.put(TABLE_T_D,date);
        db.insert(TABLE_TRAVEL,null,cv);
    }
    // 根据指定条件删除表记录
    public int deleteUser(String condition) {
        // 执行删除记录动作，该语句返回删除记录的数目
        return mDB.delete(TABLE_NAME, condition, null);
    }

    // 删除该表的所有记录
    public int deleteAllUser() {
        // 执行删除记录动作，该语句返回删除记录的数目
        return mDB.delete(TABLE_NAME, "1=1", null);
    }


    // 往该表添加一条记录
    public long insert_user(UserInfo info) {
        ArrayList<UserInfo> infoArray = new ArrayList<UserInfo>();
        infoArray.add(info);
        return insert_user(infoArray);
    }


    // 往该表添加多条记录
    public long insert_user(ArrayList<UserInfo> infoArray) {
        long result = -1;
        for (int i = 0; i < infoArray.size(); i++) {
            UserInfo info = infoArray.get(i);
            ArrayList<UserInfo> tempArray = new ArrayList<UserInfo>();
            // 如果存在同名记录，则更新记录
            // 注意条件语句的等号后面要用单引号括起来
            if (info.username != null && info.username.length() > 0) {
                String condition = String.format("username='%s'", info.username);
                tempArray = Userquery(condition);
                if (tempArray.size() > 0) {
                    Userupdate(info, condition);
                    result = tempArray.get(0).rowid;
                    continue;
                }
            }
            // 如果存在同样的手机号码，则更新记录
            if (info.phone != null && info.phone.length() > 0) {
                String condition = String.format("phone='%s'", info.phone);
                tempArray = Userquery(condition);
                if (tempArray.size() > 0) {
                    Userupdate(info, condition);
                    result = tempArray.get(0).rowid;
                    continue;
                }
            }
            // 不存在唯一性重复的记录，则插入新记录
            ContentValues cv = new ContentValues();
            cv.put("username", info.username);
            cv.put("age", info.age);
            cv.put("email", info.email);
            cv.put("manager", info.manager);
            cv.put("gender", info.gender);
            cv.put("birthday", info.birthday);
            cv.put("register_time", info.register_time);
            cv.put("phone", info.phone);
            cv.put("password", info.password);
            cv.put("head_portrait", info.head_portrait);
            cv.put("portrait", info.portrait);
            // 执行插入记录动作，该语句返回插入记录的行号
            result = mDB.insert(TABLE_NAME, "", cv);
            // 添加成功后返回行号，失败后返回-1
            if (result == -1) {
                return result;
            }
        }
        return result;
    }

    // 根据条件更新指定的表记录
    public int Userupdate(UserInfo info, String condition) {
        ContentValues cv = new ContentValues();
        cv.put("username", info.username);
        cv.put("age", info.age);
        cv.put("email", info.email);
        cv.put("manager", info.manager);
        cv.put("gender", info.gender);
        cv.put("birthday", info.birthday);
        cv.put("register_time", info.register_time);
        cv.put("phone", info.phone);
        cv.put("password", info.password);
        cv.put("head_portrait", info.head_portrait);
        cv.put("portrait", info.portrait);
        // 执行更新记录动作，该语句返回记录更新的数目
        return mDB.update(TABLE_NAME, cv, condition, null);
    }

    public int Userupdate(UserInfo info) {
        // 执行更新记录动作，该语句返回记录更新的数目
        return Userupdate(info, "rowid=" + info.rowid);
    }

    // 根据指定条件查询记录，并返回结果数据队列
    public ArrayList<UserInfo> Userquery(String condition) {
        String sql = String.format("select rowid,_id,username,age,email,manager,gender,birthday,register_time," +
                "phone,password,head_portrait,portrait from %s where %s;", TABLE_NAME, condition);
        Log.d(TAG, "query sql: " + sql);
        ArrayList<UserInfo> infoArray = new ArrayList<UserInfo>();
        // 执行记录查询动作，该语句返回结果集的游标
        mDB = mHelper.getWritableDatabase();
        Cursor cursor = mDB.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            UserInfo info = new UserInfo();
            info.rowid = cursor.getLong(0); // 取出长整型数
            info.xuhao = cursor.getInt(1); // 取出整型数
            info.username = cursor.getString(2); // 取出字符串
            info.age = cursor.getInt(3);
            info.email = cursor.getString(4);
            //SQLite没有布尔型，用0表示false，用1表示true
            info.manager = (cursor.getInt(5) == 0) ? false : true;
            info.gender = (cursor.getInt(6) == 0) ? false : true;
            info.birthday = cursor.getString(7);
            info.register_time = cursor.getString(8);
            info.phone = cursor.getString(9);
            info.password = cursor.getString(10);
            info.head_portrait = cursor.getString(11);
            info.portrait = cursor.getInt(12);
            infoArray.add(info);
        }
        cursor.close(); // 查询完毕，关闭游标
        return infoArray;
    }

    public ArrayList<UserInfo> SearchUserquery(String condition) {
        String sql = String.format("select rowid,_id,username,age,email,manager,gender,birthday,register_time," +
                "phone,password,head_portrait,portrait from user_info where username like '%%%s%%'", condition);
        Log.d(TAG, "query sql: " + sql);
        ArrayList<UserInfo> infoArray = new ArrayList<UserInfo>();
        // 执行记录查询动作，该语句返回结果集的游标
        mDB = mHelper.getWritableDatabase();
        Cursor cursor = mDB.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            UserInfo info = new UserInfo();
            info.rowid = cursor.getLong(0); // 取出长整型数
            info.xuhao = cursor.getInt(1); // 取出整型数
            info.username = cursor.getString(2); // 取出字符串
            info.age = cursor.getInt(3);
            info.email = cursor.getString(4);
            //SQLite没有布尔型，用0表示false，用1表示true
            info.manager = (cursor.getInt(5) == 0) ? false : true;
            info.gender = (cursor.getInt(6) == 0) ? false : true;
            info.birthday = cursor.getString(7);
            info.register_time = cursor.getString(8);
            info.phone = cursor.getString(9);
            info.password = cursor.getString(10);
            info.head_portrait = cursor.getString(11);
            info.portrait = cursor.getInt(12);
            infoArray.add(info);
        }
        cursor.close(); // 查询完毕，关闭游标
        return infoArray;
    }

    // 根据手机号码查询指定记录
    public UserInfo UserqueryByPhone(String phone) {
        UserInfo info = null;
        ArrayList<UserInfo> infoArray = Userquery(String.format("phone='%s'", phone));
        if (infoArray.size() > 0) {
            info = infoArray.get(0);
        }
        return info;
    }
    public UserInfo UserqueryByUsername(String username) {
        UserInfo info = null;
        ArrayList<UserInfo> infoArray = Userquery(String.format("username='%s'", username));
        if (infoArray.size() > 0) {
            info = infoArray.get(0);
        }
        return info;
    }
    public UserInfo UserqueryByUserid(String user_id) {
        UserInfo info = null;
        ArrayList<UserInfo> infoArray = Userquery(String.format("_id='%s'", user_id));
        if (infoArray.size() > 0) {
            info = infoArray.get(0);
        }
        return info;
    }

}
