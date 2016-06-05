package com.mercury.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.mercury.app.R;
import com.mercury.app.activity.FragmentDrawer.FragmentDrawerListener;

public class SliderActivity extends AppCompatActivity implements FragmentDrawerListener {

    //Defining Variables
    private Toolbar toolbar;
    private FragmentDrawer fragmentDrawer;

    String LocFragB;

    public void setLocFragB(String t){
        LocFragB = t;
    }

    public String getLocFragB(){
        return LocFragB;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //Assigning toolbar object to view and setting the actionbar to our toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);


        fragmentDrawer = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragmentNavDrawer);
        fragmentDrawer.dSetUp(R.id.fragmentNavDrawer, (DrawerLayout)findViewById(R.id.drawerLayout), toolbar);
        fragmentDrawer.setDrawerListener(this);

        displayView(0);
    }


    @Override
    public void onDrawerItemSelected(View view, int position){
        displayView(position);
    }

    private void displayView(int position){
        Fragment mFragment = null;
        String title = "HOLA";
        switch (position) {
            case 0:
                mFragment = new HelpLocationFragment();
                title = getString(R.string.help_fragment);
                break;
            case 1:
                mFragment = new AlertFragment();
                title = getString(R.string.alert_fragment);
                break;
            case 2:
                mFragment = new AddContactsFragment();
                title = getString(R.string.settings_fragment);
                break;
            default:
                break;

        }

        if(mFragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, mFragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(title);
        }
    }

}
