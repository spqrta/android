package com.example.cucumber007.ibeacon;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        startService(new Intent(this, BeaconServiceListener.class));
        configureBtButton();
	}
	
	private void configureBtButton() {
		final Button btButton = (Button) findViewById(R.id.btnBt);
		final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(btAdapter.isEnabled()) {
			Intent intent = new Intent(this, EnterActivity.class);
			startActivity(intent);
			finish();
		}
		else
			btButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					btAdapter.enable();
					btButton.setText("Your's bluetooth\nis already on");
					btButton.setOnClickListener(null);
					Intent intent = new Intent(getApplicationContext(), EnterActivity.class);
					startActivity(intent);
					finish();
				}
			});
	}

    /*public void messClick(View v) {
        Intent intent = new Intent("activity-state-message");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d("spq", "Message sent.");
    }*/

}
