package ua.com.i2i.ibeaconoceanplaza;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Notifier extends Activity {
	
	protected void onCreate(Activity act) {
		
	}
	
	public static void notify(Context cont, String text) {
	    Toast.makeText(cont, text, Toast.LENGTH_SHORT).show();
    }
	
}