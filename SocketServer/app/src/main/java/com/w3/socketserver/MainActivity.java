package com.w3.socketserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    TextView infoip, msg;
    private Server server;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        server = new Server(this);
        infoip.setText(server.getIpAddress()+":"+server.getPort());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }
}
