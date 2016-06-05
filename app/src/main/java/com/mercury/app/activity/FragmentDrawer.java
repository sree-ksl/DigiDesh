package com.mercury.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import com.mercury.app.R;
import com.mercury.app.adapter.MyAdapter;
import com.mercury.app.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private RecyclerView dRecyclerView;
    private ActionBarDrawerToggle dDrawerToggle;
    private DrawerLayout dDrawerLayout;
    private MyAdapter mAdapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;

    public FragmentDrawer(){

    }

    public void setDrawerListener(FragmentDrawerListener drawerListener){
        this.drawerListener = drawerListener;
    }

    public static List<NavDrawerItem> getData(){
        List<NavDrawerItem> data = new ArrayList<NavDrawerItem>();

        //preparing navigation drawer items
        for(int i = 0; i<titles.length; i++){
            NavDrawerItem navDrawerItem = new NavDrawerItem();
            navDrawerItem.setTitle(titles[i]);
            data.add(navDrawerItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //inflate the view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        dRecyclerView = (RecyclerView)layout.findViewById(R.id.drawerList);

        mAdapter = new MyAdapter(getActivity(), getData());
        dRecyclerView.setAdapter(mAdapter);
        dRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dRecyclerView.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(getActivity(), dRecyclerView, new ClickListener(){
            @Override
            public void onClick(View view, int position){
                drawerListener.onDrawerItemSelected(view, position);
                dDrawerLayout.closeDrawer(containerView);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return layout;
    }

    public void dSetUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar){
        containerView = getActivity().findViewById(fragmentId);
        dDrawerLayout = drawerLayout;
        dDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        dDrawerLayout.setDrawerListener(dDrawerToggle);
        dDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                dDrawerToggle.syncState();
            }
        });
    }

    public static interface  ClickListener{
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

    public interface FragmentDrawerListener{
        public void onDrawerItemSelected(View view, int position);
    }
}
