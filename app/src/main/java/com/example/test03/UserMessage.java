package com.example.test03;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
/*能够跳转到短信界面*/
public class UserMessage extends AppCompatActivity {
    private String phone;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.uer_messages);
        intent=getIntent();
        phone=intent.getStringExtra("user_messages");
        intent=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phone));
        intent.putExtra("sms_body","使用我们的软件");
        startActivity(intent);
    }
}
