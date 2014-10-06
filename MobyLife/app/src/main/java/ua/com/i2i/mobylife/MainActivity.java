package ua.com.i2i.mobylife;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity implements ActionBar.TabListener, HttpGetReceiver, AbsListView.OnScrollListener {

    Context context = this;
    SectionsPagerAdapter mSectionsPagerAdapter;
    List<String> CategoryList = new ArrayList<String>();
    HashMap<String, Integer> Categories = new HashMap<String, Integer>();
    Menu globalMenu;
    LayoutInflater ltInflater;

    ViewPager mViewPager; //The {@link ViewPager} that will host the section contents.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayShowHomeEnabled(false);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        LinearLayout llMain;

        //llMain = (LinearLayout) findViewById(R.id.tabLayout);
        ltInflater = getLayoutInflater();

        HttpGetterTask getter = new HttpGetterTask(this);
        getter.setHeader("Language", "ru");
        getter.getCategories();

        HttpGetterTask getter1 = new HttpGetterTask(this);
        getter1.setHeader("Language", "ru");
        getter1.setHeader("Platform", "1");
        getter1.setHeader("Version", "7.1");
        getter1.setHeader("Country", "ru");
        getter1.getApps();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        globalMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        fillMenu(menu);
        return true;
    }

    private void fillMenu(Menu menu) {
        for (String categ : CategoryList) {
            MenuItem item =  menu.getItem(0).getSubMenu().add(categ);
        }


        for(Map.Entry<String, Integer> entry : Categories.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        Log.d("culog",id+" "+(String)item.getTitle());
        if(!((String)(item.getTitle())).equals("Category")) {
            //TODO костыль
            int categoryID = Categories.get((String)item.getTitle());
            HttpGetterTask getter1 = new HttpGetterTask(this);
            getter1.setHeader("Language", "ru");
            getter1.setHeader("Platform", "1");
            getter1.setHeader("Version", "7.1");
            getter1.setHeader("Country", "ru");
            getter1.getApps(categoryID);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }
    }

    @Override
    public void receiveHttpData(String data, int type) {
        Log.d("get_req", data);
        switch(type) {
            case 0: //categories
                try {
                    JSONArray json = new JSONArray(data);
                    for (int i = 0; i < json.length(); i++) {
                        CategoryList.add(((JSONObject)json.get(i)).getString("name"));
                        Categories.put(((JSONObject)json.get(i)).getString("name"), ((JSONObject)json.get(i)).getInt("id"));
                    };
                    fillMenu(globalMenu);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1: //apps
                try {
                    displayApps(new JSONObject(data));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default: break;
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("scroll", ""+scrollState);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void displayApps(JSONObject json) {
        try {
            JSONArray res = json.getJSONArray("results");

            ViewGroup tab = (ViewGroup) findViewById(R.id.tabLayout);
            tab.removeAllViews();

            for (int i = 0; i < res.length(); i++) {

                View item = ltInflater.inflate(R.layout.app_layout, tab, false);
                TextView appname = (TextView) item.findViewById(R.id.app_name);
                appname.setText(((JSONObject) res.get(i)).getString("name"));

                String imageUrl = ((JSONObject) res.get(i)).getString("image_url");
                ImageLoadTask imageTask = new ImageLoadTask((ImageView)item.findViewById(R.id.appLogo));
                imageTask.execute(imageUrl);

                String tagString = "";
                JSONArray tagsJSON = ((JSONObject) res.get(i)).getJSONArray("tags");
                for (int j = 0; j < tagsJSON.length(); j++) {
                    if(j!=0) tagString += ", ";
                    tagString += ((JSONObject)tagsJSON.get(j)).getString("name");
                }
                if (tagString == "") tagString = "No tags";
                TextView tagName = (TextView) item.findViewById(R.id.tags);
                tagName.setText(tagString);

                if (item == null) {
                    item = new View(this);
                }
                tab.addView(item);
                /*ListView list = (ListView)findViewById(R.id.appsList);
                final SimpleAdapter adapter = new SimpleAdapter(context,
                        list, list);
                list.setAdapter(adapter);*/

            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

}