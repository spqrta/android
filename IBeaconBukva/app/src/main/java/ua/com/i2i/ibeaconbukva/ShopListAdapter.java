package ua.com.i2i.ibeaconbukva;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

public class ShopListAdapter {

	public static HashMap<String, String> departments = new HashMap<String, String>();
	private static HashMap<String, Integer> imgs = new HashMap<String, Integer>();
	
	private static final String COAT = "Coat department";
	private static final String SHOOES = "Shooes deparment";
	
	private final Context context;
	private final List<String> values;
	
	public ShopListAdapter(Context context, List<String> values) {
		
		departments.put("C4:3B:FA:CA:8A:27", COAT);
		departments.put("DE:95:B2:7D:6B:FA", SHOOES);
		
		imgs.put(COAT, R.drawable.coat);
		imgs.put(SHOOES, R.drawable.shooes);
		
		this.context = context;
		this.values = values;
	}
}
