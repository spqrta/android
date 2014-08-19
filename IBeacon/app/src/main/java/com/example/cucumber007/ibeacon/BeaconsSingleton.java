package com.example.cucumber007.ibeacon;

import com.radiusnetworks.ibeacon.IBeacon;

import java.util.Collection;
import java.util.LinkedList;

public class BeaconsSingleton {

	private static BeaconsSingleton instance;
	public Collection<IBeacon> beacons;
	
	private BeaconsSingleton() {
		beacons = new LinkedList<IBeacon>();
	}
	
	public static BeaconsSingleton getInstance() {
		if(instance == null)
			instance = new BeaconsSingleton();
		return instance;
	}
}
