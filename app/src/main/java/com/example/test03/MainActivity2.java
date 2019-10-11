package com.example.test03;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.view.View.*;

public class MainActivity2 extends AppCompatActivity {

    private TextView u_name1,u_phone1;
    private ImageView imageView_phone;
    private  ImageView imageView_message;

    private  Intent intent;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message);
        //Intent intent=getIntent();
        Bundle bundle=getIntent().getExtras();
        String u_name=bundle.getString("uer_name");
        //System.out.println(u_name);
        final String u_phone=bundle.getString("uer_phone");
        //Toast.makeText(this,"acacia"+u_name,Toast.LENGTH_SHORT).show();
        u_name1=findViewById(R.id.textView21);
        u_phone1=findViewById(R.id.textView31);
        u_name1.setText(u_name);
        u_phone1.setText(u_phone);

        /*对短信，电话设置监听*/
        imageView_phone=findViewById(R.id.imageView4);
        //设置图片被点击后可以进行事件监听
        imageView_phone.setFocusable(true);
        imageView_phone.setClickable(true);
        //开始监听
        imageView_phone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//Toast.makeText(this,"被点击",Toast.LENGTH_SHORT).show();
                //System.out.println("点击成功");
                intent=new Intent();
                intent.putExtra("user_phone",u_phone);
                intent.setAction("com.example.test03.UserPhone");
                startActivity(intent);
                //System.out.println("即将发送");
            }
        });

        imageView_message=findViewById(R.id.imageView3);
        imageView_message.setFocusable(true);
        imageView_message.setClickable(true);
        imageView_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent();
                intent.putExtra("user_messages",u_phone);
                intent.setAction("com.example.test03.UserMessage");
                startActivity(intent);
            }
        });
    }

}
