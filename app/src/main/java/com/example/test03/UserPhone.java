package com.example.test03;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/*能够跳转到拨号界面*/
public class UserPhone extends AppCompatActivity {

    private String phone="@zhouze@";
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.user_phone);
        //System.out.println("跳转成功");
        intent=getIntent();
        phone=intent.getStringExtra("user_phone");
        System.out.println("电话号码："+phone);
        intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
