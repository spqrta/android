package com.example.cucumber007.beacon;

import android.util.Log;

import com.radiusnetworks.ibeacon.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cucumber007 on 09.07.2014.
 */
public class BeaconDatabase {

    static private List<MyBeacon> beacons = new ArrayList<MyBeacon>();

    public BeaconDatabase() {
        beacons.add(new MyBeacon(
                beacons.size(),
                "F0:D9:96:04:06:18",
                CoatActivity.class,
                27311,
                R.id.btnCoat,
                "Акция",
                "Специальное предложение для Вас",
                "Пиджак женский VICTOR&ROLF, -33%"
        ));

        beacons.add(new MyBeacon(
                beacons.size(),
                "DE:95:B2:7D:6B:FA",
                ShoesActivity.class,
                56665,
                R.id.btnShoes,
                "Акция",
                "Специальное предложение для Вас",
                "Туфли мужские ECCO, баллы х2"
        ));

        beacons.add(new MyBeacon(
                beacons.size(),
                "C4:3B:FA:CA:8A:27",
                EnterActivity.class,
                40603,
                R.id.imageButton2,
                "Добро пожаловать!",
                "Добро пожаловать в Argo",
                "Желаем Вам удачного шоппинга :)"
        ));

    }

    public static int convertToID(String address){
        for(MyBeacon item : beacons) {
            if(item.getAddress().equals(address)) return item.getId();
        }
        Log.d("spq_beacon", "No beacon with such address: "+address);
        return -1;
    }

    public static int convertToID(Integer minor){
        for(MyBeacon item : beacons) {
            //Log.d("spq_convert", item.getMinor()+" "+minor);
            if(item.getMinor().equals(minor)) return item.getId();
        }
        Log.d("spq_beacon", "No beacon with such minor: "+minor);
        return -1;
    }

    public String[] getTextArrayById(Integer id){
        return beacons.get(id).getText();
    }

    public String[] getTextArray(Integer minor){
        int id = convertToID(minor);
        if(id != -1) return beacons.get(convertToID(minor)).getText();
        else return new String[] {"no data", "no data", "no data"};
    }

    public Class getClasses(int id){
        return beacons.get(id).getActivity();
    }

    public List<Region> getRegions(){
        List<Region> list = new ArrayList<Region>();

        for (MyBeacon item : beacons) {
            list.add(item.getRegion());
        }

        return list;
    }

    public static int getGuiByMinor(int minor){
        return beacons.get(convertToID(minor)).getGui_id();
    }

    public static int getGuiById(int id){
        return beacons.get(id).getGui_id();
    }

    public static boolean[] getBeaconState() {
        boolean[] res = new boolean[beacons.size()];
        for (int i = 0; i < beacons.size(); i++) {
            res[i] = beacons.get(i).getState();
            Log.d("spq_beacon", "State get "+i+" "+beacons.get(i).getState());

        }
        return res;
    }

    public void setBeaconState(int id, boolean state){
        beacons.get(id).setState(state);
        Log.d("spq_beacon", "State "+true+" to "+id);
    }
}


