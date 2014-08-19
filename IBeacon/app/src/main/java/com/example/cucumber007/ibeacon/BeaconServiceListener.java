package com.example.cucumber007.ibeacon;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconData;
import com.radiusnetworks.ibeacon.IBeaconDataNotifier;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;
import com.radiusnetworks.ibeacon.client.DataProviderException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeaconServiceListener extends IntentService implements IBeaconConsumer, RangeNotifier, MonitorNotifier {

	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    private boolean mainActivityOpen = false;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)  {

            String state = intent.getStringExtra("state");
            if(state.equals("started")) {
                mainActivityOpen = true;
            }
            else {
                mainActivityOpen = true;
            }
        }
    };

    private BeaconDatabase beacons = new BeaconDatabase();
	
	public BeaconServiceListener() {
		super("QBeaconService");
	}

    @Override
    public void onCreate() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("activity-state-message"));
        Log.d("spq", "Service started.");
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		iBeaconManager.bind(this);
		return START_STICKY;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {	}
	
	@Override
	public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

		iBeaconManager.unBind(this);
		super.onDestroy();
	}
	
	@Override
	public void onIBeaconServiceConnect() {
		/*iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(Region arg0) {		}
			
			@Override
			public void didExitRegion(Region arg0) { 		}

			@Override
			public void didDetermineStateForRegion(int arg0, Region arg1) {			}
		});*/

        iBeaconManager.setMonitorNotifier(this);
		iBeaconManager.setRangeNotifier(this);
		
		try {
			Region region = new Region("myUniqueId", null, null, null);

            List<Region> regions = new ArrayList<Region>();
            regions = beacons.getRegions();

            for (Region item : regions) {
                Log.d("spq_beacon", "Start monitor in region "+item.getMinor());
                iBeaconManager.startMonitoringBeaconsInRegion(item);
            }

			//iBeaconManager.startMonitoringBeaconsInRegion(region);
			//iBeaconManager.startRangingBeaconsInRegion(region);
		} catch (RemoteException e) {}
	}

	private void pushNotification(Integer id) {
        if (id != -1) {

            Intent intent;// = new Intent(this, CoatActivity.class);
            String sOffer = "";

            String[] res = beacons.getTextArrayById(id);

            String ticker = res[0];
            String title = res[1];
            String message = res[2];
            intent = new Intent(this, beacons.getClasses(id));

            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Notification noti = new NotificationCompat.Builder(this)
                    .setTicker(ticker)
                    .setContentTitle(title)
                    .setContentText(message + sOffer)
                    .setSmallIcon(R.drawable.argo)
                    .setContentIntent(pIntent).build();
            noti.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nManager.notify(id, noti);
        }
        else Log.d("spq", "Notification error: ID");
	}

    private void closeNotification(Integer id) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.cancel(id);
    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void didRangeBeaconsInRegion(Collection<IBeacon> arg0, Region arg1) {
		if(!arg0.isEmpty()) {
            for (IBeacon item : arg0) {
                item.requestData(new IBeaconDataNotifier() {
                    @Override
                    public void iBeaconDataUpdate(IBeacon iBeacon, IBeaconData iBeaconData, DataProviderException e) {
                        //Log.d
                    }
                });
            }
        }
	}

    @Override
    public  void didDetermineStateForRegion(int state, Region region){};

    @Override
    public  void didEnterRegion(Region region){
        Intent intent = new Intent("beacon-message");
        intent.putExtra("direction", "enter");
        intent.putExtra("minor", region.getMinor());
        //TODO убрать broadcast MainActivity -> Service
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        beacons.setBeaconState(beacons.convertToID(region.getMinor()), true);
        Log.d("spq_beacon", "Set state "+true+" to "+beacons.convertToID(region.getMinor()));


        Log.d("spq_beacon", "Enter region " + region.getMinor());
        pushNotification(beacons.convertToID(region.getMinor()));
    };

    @Override
    public  void didExitRegion(Region region){
        Intent intent = new Intent("beacon-message");
        intent.putExtra("direction", "exit");
        intent.putExtra("minor", region.getMinor());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        beacons.setBeaconState(beacons.convertToID(region.getMinor()), false);

        //TODO этот метод работает с нехилой задержкой
        Log.d("spq_beacon", "Exit region "+region.getMinor());
        closeNotification(beacons.convertToID(region.getMinor()));
    };



}
