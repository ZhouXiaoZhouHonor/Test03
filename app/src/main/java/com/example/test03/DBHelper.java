package com.example.test03;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.test03.Dao.user;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME="users.db";
    public static final String TAG="SQLite";
    //public static final int VERSION=1;

    public SQLiteDatabase sqd;

    public DBHelper(Context context,int version){
        super(context,TABLE_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"创建数据库.....");
        String sql="create table user(id integer primary key autoincrement,name varchar(50),phone varchar(50))";
        db.execSQL(sql);
       // db.execSQL("insert into phone(name,phone) values('周泽','15256038842')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"更新数据库");
        //删除数据库然后重新创建
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
        //db.execSQL("alter table user add phone varchar(13) null");
    }

    /*添加*/
    public void insert(String name,String phone){
        sqd=this.getWritableDatabase();//需要进行数据库的填写
        String sql="insert into user(name,phone) values('"+name+"','"+phone+"')";
        sqd.execSQL(sql);
        sqd.close();
        Log.d("TAG_insert","添加数据");
    }

    /*查询所有数据*/
    public List<user> slectAll(){
        sqd=this.getReadableDatabase();//读出数据库
        String sql="select name,phone from user";
        Cursor cursor=sqd.rawQuery(sql,null);
        List<user> list=new ArrayList<>();
        while(cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex("name"));
            String phone=cursor.getString(cursor.getColumnIndex("phone"));
            user u=new user();
            u.setName(name);
            u.setPhone(phone);
            list.add(u);//将对象添加入集合中
        }
        sqd.close();//关闭连接
        return list;
    }
    /*删除数据*/
    public void delete(String name){
        String sql="delete from user where name='"+name+"'";
        sqd=this.getWritableDatabase();
        sqd.execSQL(sql);
        sqd.close();
        Log.d("TAG_DELETE","删除数据");
    }
    /*更新数据库*/
    public void update(String name,String phone){
        String sql="update user set phone='"+phone+"' where name='"+name+"'";
        sqd=this.getWritableDatabase();
        sqd.execSQL(sql);
        sqd.close();
        Log.d("TAG_update","更新成功");
    }

}
