package com.example.test03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.test03.Dao.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.view.ContextMenu.*;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private ListView listView;
    ArrayAdapter<String> arrayAdapter;
    public DBHelper db;
    public List<user> list;
    public List<String> list_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*将需要显示的内容存储在ArrayList集合中*/
        listView=findViewById(R.id.list01);//注册列表组件
        /*将list对象放在arrayAdapter对象中用于在手机上显示*/
        //声明数组适配器用于绑定格式单一的数据，数据源可以是集合或者数组

        list=select();
        list_name=new ArrayList<>();
        Iterator iterator=list.iterator();
        while(iterator.hasNext()){
            user u=(user)iterator.next();
            list_name.add(u.getName());
        }
        arrayAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,list_name);
        listView.setAdapter(arrayAdapter);

        /*对删除、更新之类的功能需要增加上下文菜单。使用Dialog*/
        /*1、注册上下文监听器，显示菜单*/
        registerForContextMenu(listView);

        /*显示联系人详细信息*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user u1=list.get(position);
                String name= u1.getName();//用户名
                String phone= u1.getPhone();//电话号码
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);//用于数据传递
                Bundle bundle=new Bundle();
                //将联系人，手机号码放入bundle对象中
                bundle.putString("uer_name",name);
                bundle.putString("uer_phone",phone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /*更新、删除的提示框*/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=new MenuInflater(this);// MenuInflater是用来实例化Menu目录下的Menu布局文件的。
        menuInflater.inflate(R.menu.menu,menu);
    }
    /*提示框中处理的内容
    * 更新、删除功能*/
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position=menuInfo.position;
        switch(item.getItemId()){
            case R.id.delete1:
                final AlertDialog.Builder normalDialog=new AlertDialog.Builder(this);
                normalDialog.setIcon(R.mipmap.ic_launcher);
                normalDialog.setTitle("删除页面");
                normalDialog.setMessage("确定删除吗？");
                normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /*什么都不做*/
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(list_name.get(position));
                        list_name.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
               normalDialog.show();
               break;
            case R.id.update1:
                final AlertDialog.Builder normalDialog1=new AlertDialog.Builder(this);
                normalDialog1.setIcon(R.mipmap.ic_launcher);
                normalDialog1.setTitle("更新页面").setView(editText);
                View view=getLayoutInflater().inflate(R.layout.update_demo,null);
                final EditText editText1=(EditText) view.findViewById(R.id.update_name);//找到编辑框，将未更改的数据放入
                user us=(user) list.get(position);
                editText1.setText(us.getName());
                editText1.setEnabled(false);//将编辑框设置为不可编辑
                final EditText editText2=(EditText) view.findViewById(R.id.update_phone);
                editText2.setText(us.getPhone());
                normalDialog1.setView(view);
                normalDialog1.setCancelable(false);//禁止默认点击对话框外部或者点击后退键使对话框消失，需要点击取消才能关闭
                normalDialog1.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username=editText1.getText().toString();
                        String userphone=editText2.getText().toString();
                        update(username,userphone);
                        list=select();
                        arrayAdapter.notifyDataSetChanged();//刷新数据
                    }
                });
                normalDialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
                normalDialog1.show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    /*创建actionMenu，在menu中添加添加属性*/
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_user,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final AlertDialog.Builder normalDialog=new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("添加页面");
        normalDialog.setMessage("添加联系人");
        View view=getLayoutInflater().inflate(R.layout.adduser,null);
        final EditText editText_1=(EditText) view.findViewById(R.id.username);
        final EditText editText_2=(EditText) view.findViewById(R.id.userphone);
        normalDialog.setView(view);
        normalDialog.setCancelable(false);
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*什么都不做*/
                arrayAdapter.notifyDataSetChanged();
            }
        });
        normalDialog.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //获取编辑框中的数据
                String user_name=editText_1.getText().toString();
                String user_phone=editText_2.getText().toString();
                insert(user_name,user_phone);//数据添加
                //添加完成之后也需要再次查询所有的用户信息才能将插入的信息显示出来
                list=select();
                list_name.add(user_name);
                arrayAdapter.notifyDataSetChanged();//数据刷新
            }
        });
        normalDialog.show();
        return true;
    }

    /*查询数据*/
    public List<user> select(){
        db=new DBHelper(this,1);
        list=db.slectAll();//用户的信息，包括姓名和电话号码
        db.close();
        return list;
    }

    /*插入数据*/
    public void insert(String name,String phone){
        db=new DBHelper(this,1);
        db.insert(name,phone);
        db.close();
    }

    /*删除数据*/
    public void delete(String name){
        db=new DBHelper(this,1);
        db.delete(name);
        db.close();
    }

    /*更新数据*/
    public void update(String name,String phone){
        db=new DBHelper(this,1);
        db.update(name,phone);
        db.close();
    }

}
