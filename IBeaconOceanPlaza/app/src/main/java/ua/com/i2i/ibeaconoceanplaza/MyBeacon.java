package ua.com.i2i.ibeaconoceanplaza;

import com.radiusnetworks.ibeacon.Region;

/**
 * Created by cucumber007 on 10.07.2014.
 */
public class MyBeacon {

    private Integer id;
    private String address;
    private String ticker;
    private String title;
    private String message;
    private Class activity;
    private Region region;
    private Integer minor;
    private int gui_id;
    boolean state = false;

    public MyBeacon(Integer idP, String codeP, Class activityP, int minorP) {
        id = idP;
        activity = activityP;
        address = codeP;
        minor = minorP;


        region = new Region(id.toString(), null, null, minor);
    }

    public MyBeacon(Integer idP, String codeP, Class activityP, int minorP, int gui_idP, String tickerP, String titleP, String messageP) {
        id = idP;
        activity = activityP;
        address = codeP;
        minor = minorP;
        gui_id = gui_idP;

        ticker = tickerP;
        title = titleP;
        message = messageP;

        region = new Region(id.toString(), null, null, minor);
    }

    public void setText(String tickerP, String titleP, String messageP) {
        ticker = tickerP;
        title = titleP;
        message = messageP;
    }

    public void setGui_id(int gui_id) {
        this.gui_id = gui_id;
    }

    public void setState(boolean stateP) {
        state = stateP;
    }

    public String getAddress() {
        return address;
    }

    public Class getActivity() {
        return activity;
    }

    public Integer getId() {
        return id;
    }

    public String[] getText() {
        return new String[] {ticker, title, message};
    }

    public int getGui_id() {
        return gui_id;
    }

    public Region getRegion() {
        return region;
    }

    public Integer getMinor() {
        return minor;
    }

    public boolean getState() {
        return state;
    }


}
