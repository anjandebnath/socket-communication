package com.w3.socketclient;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends Activity {

	public TextView response;
	EditText editTextAddress, editTextPort;
	Button buttonConnect, buttonClear;


	private String mResult;
	private int count = 0;

	SocketClient socketClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editTextAddress = (EditText) findViewById(R.id.addressEditText);
		editTextPort = (EditText) findViewById(R.id.portEditText);
		buttonConnect = (Button) findViewById(R.id.connectButton);
		buttonClear = (Button) findViewById(R.id.clearButton);
		response = (TextView) findViewById(R.id.responseTextView);


		buttonConnect.setOnClickListener(arg0 -> {

			count++;
			socketClient = new SocketClient(editTextAddress.getText()
					.toString(), Integer.parseInt(editTextPort
					.getText().toString()), count, this);

		});

		buttonClear.setOnClickListener(v -> response.setText(""));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
