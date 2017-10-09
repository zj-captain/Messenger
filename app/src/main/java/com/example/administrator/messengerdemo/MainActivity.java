package com.example.administrator.messengerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private Messenger serviceMessenger; //来自服务端的Messenger
    private Messenger receiveMessenger; //接收并处理服务端的消息

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Button btn_send01 = (Button) findViewById(R.id.btn_send01);
        Button btn_send02 = (Button) findViewById(R.id.btn_send02);

        receiveMessenger = new Messenger(new ReceiveHandle(this));

        btn_send01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向服务端发送第一条消息
                Message message = Message.obtain();
                message.what = Constant.CLIENT_MSG_01;
                message.replyTo = receiveMessenger;
                try {
                    serviceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_send02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向服务端发送第二条消息
                Message message = Message.obtain();
                message.what = Constant.CLIENT_MSG_02;
                message.replyTo = receiveMessenger;
                try {
                    serviceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceMessenger = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, MyService.class), conn, Context.BIND_AUTO_CREATE);
    }

    private class ReceiveHandle extends Handler {
        WeakReference<MainActivity> mainActivityWeakReference;

        public ReceiveHandle(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SERVICE_REPLY_01:
                    Toast.makeText(mContext, msg.getData().getString("msg"), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.SERVICE_REPLY_02:
                    Toast.makeText(mContext, msg.getData().getString("info"), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
