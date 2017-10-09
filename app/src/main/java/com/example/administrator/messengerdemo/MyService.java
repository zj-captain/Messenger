package com.example.administrator.messengerdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.lang.ref.WeakReference;

public class MyService extends Service {

    private Messenger reciveMessenger = new Messenger(new ServiceHandle(this)); //接收来自客户端的消息

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return reciveMessenger.getBinder();
    }

    class ServiceHandle extends Handler {
        WeakReference<MyService> myServiceWeakReference;

        public ServiceHandle(MyService myService) {
            myServiceWeakReference = new WeakReference<MyService>(myService);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.CLIENT_MSG_01:
                    Messenger messenger = msg.replyTo;
                    Message message = Message.obtain();
                    message.what = Constant.SERVICE_REPLY_01;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "这是服务端回应的第一条消息");
                    message.setData(bundle);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.CLIENT_MSG_02:
                    Messenger messenger1 = msg.replyTo;
                    Message message1 = Message.obtain();
                    message1.what = Constant.SERVICE_REPLY_02;
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("info", "这是服务端回应的第二条消息");
                    message1.setData(bundle1);
                    try {
                        messenger1.send(message1);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}
