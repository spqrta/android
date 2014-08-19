package com.example.cucumber007.beacon;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class EnterActivity extends Activity implements OnClickListener {

    private BroadcastReceiver actMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)  {
            Log.d("spq_enter", intent.getIntExtra("minor", -1) + " Got it.");
            GuiBeaconReact(intent.getStringExtra("direction"), intent.getIntExtra("minor", -1));
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter);
		
		((ImageButton) findViewById(R.id.btnCoat)).setOnClickListener(this);;
		((ImageButton) findViewById(R.id.btnShoes)).setOnClickListener(this);

        Intent intent = new Intent("activity-state-message");
        intent.putExtra("state", "started");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        LocalBroadcastManager.getInstance(this).registerReceiver(actMessageReceiver,
                new IntentFilter("beacon-message"));

        updateGuiBeaconState();
	}

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent("activity-state-message");
        intent.putExtra("state", "started");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent("activity-state-message");
        intent.putExtra("state", "started");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        updateGuiBeaconState();
    }

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, 
				v.getId() == R.id.btnCoat ? CoatActivity.class : ShoesActivity.class);
		startActivity(intent);
	}

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(actMessageReceiver);

        Intent intent = new Intent("activity-state-message");
        intent.putExtra("state", "stopped");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Intent intent = new Intent("activity-state-message");
        intent.putExtra("state", "stopped");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        super.onStop();
    }

    @Override
    protected void onPause() {
        Intent intent = new Intent("activity-state-message");
        intent.putExtra("state", "stopped");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        super.onPause();
    }

    private void GuiBeaconReact(String direction, int minor){
        ImageButton imb = (ImageButton)findViewById(BeaconDatabase.getGuiByMinor(minor));
        if(direction.equals("exit")){
            imb.setVisibility(View.GONE);
        }
        else {
            imb.setVisibility(View.VISIBLE);
        }
    }

    private void updateGuiBeaconState(){
        boolean[] state = BeaconDatabase.getBeaconState();
        for (int i = 0; i < state.length; i++) {
            findViewById(BeaconDatabase.getGuiById(i)).setVisibility(state[i] ? View.VISIBLE : View.GONE);
            Log.d("spq_enter", "Loading beacon state "+i+"="+state[i]);
        }
    }
}
