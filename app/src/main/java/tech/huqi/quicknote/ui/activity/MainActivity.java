package tech.huqi.quicknote.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import tech.huqi.quicknote.R;
import tech.huqi.quicknote.ui.fragment.AboutFragment;
import tech.huqi.quicknote.ui.fragment.AllNoteFragment;
import tech.huqi.quicknote.ui.fragment.PurchaseFragment;
import tech.huqi.quicknote.ui.fragment.RecentEditFragment;
import tech.huqi.quicknote.ui.fragment.SettingsFragment;
import tech.huqi.quicknote.ui.fragment.WasteBasketFragment;
import tech.huqi.quicknote.util.CommonUtil;

import static tech.huqi.quicknote.config.Constants.INTENT_QUICK_ACTION;
import static tech.huqi.quicknote.config.Constants.INTENT_QUICK_ACTION_CAMERA;
import static tech.huqi.quicknote.config.Constants.INTENT_QUICK_ACTION_FREEHAND;
import static tech.huqi.quicknote.config.Constants.INTENT_QUICK_ACTION_RECORD;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final String ALL_NOTE_FRAGMENT = "all_note";
    public static final String RECENT_EDIT_FRAGMENT = "note_recent_edit";
    public static final String WASTE_BASKET_FRAGMENT = "waste_basket";
    public static final String SETTINGS_FRAGMENT = "settings";
    public static final String ABOUT_FRAGMENT = "about";
    public static final String PURCHASE_FRAGMENT = "purchase";
    private HashMap<String, Fragment> mFragments = new HashMap<>();
    private Fragment mCurrentFragment;
    private Toolbar mToolbar;
    private FloatingActionMenu mFabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestPermissions();
    }

    /**
     * onStart -> onRestoreInstanceState -> onResume
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        switchFragment(mCurrentFragment, mFragments.get(SETTINGS_FRAGMENT));
    }

    private void initView() {
        initBaseLayout();
        initFragments();
    }

    private void initFragments() {
        mFragments.put(ALL_NOTE_FRAGMENT, new AllNoteFragment());
        mFragments.put(RECENT_EDIT_FRAGMENT, new RecentEditFragment());
        mFragments.put(WASTE_BASKET_FRAGMENT, new WasteBasketFragment());
        mFragments.put(SETTINGS_FRAGMENT, new SettingsFragment());
        mFragments.put(ABOUT_FRAGMENT, new AboutFragment());
        mFragments.put(PURCHASE_FRAGMENT, new PurchaseFragment());
        showFragment(ALL_NOTE_FRAGMENT);
    }

    private void showFragment(String fragmentTag) {
        Fragment fragment = mFragments.get(fragmentTag);
        mCurrentFragment = fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
//        ft.addToBackStack(null);
        ft.replace(R.id.fl_main_page_content, fragment, null);
        ft.commit();
    }

    private void initBaseLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initFabMenu();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        initNavigationHeadView(navigationView);
    }

//    private void initNavigationHeadView(NavigationView navigationView) {
//        ImageView headerBackground = navigationView.findViewById(R.id.iv_nav_header_bg);
//        ImageView userAvatar = navigationView.findViewById(R.id.iv_nav_header_user_avatar);
//        TextView userNick = navigationView.findViewById(R.id.tv_nav_header_nick);
//
//    }

    private void switchFragment(Fragment from, Fragment to) {
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                ft.hide(from).add(R.id.fl_main_page_content, to).commit();
            } else {
                ft.hide(from).show(to).commit();
            }
        }
    }

    public void switchFragmentByTag(String fragmentTag) {
        switchFragment(mCurrentFragment, mFragments.get(fragmentTag));
        if (SETTINGS_FRAGMENT.equals(fragmentTag)) {
            getSupportActionBar().setTitle(R.string.settings);
        } else if (ABOUT_FRAGMENT.equals(fragmentTag)) {
            getSupportActionBar().setTitle(R.string.about);
        } else if (PURCHASE_FRAGMENT.equals(fragmentTag)) {
            getSupportActionBar().setTitle(R.string.purchase);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public FloatingActionMenu getFabMenu() {
        return mFabMenu;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_notes) {
            switchFragment(mCurrentFragment, mFragments.get(ALL_NOTE_FRAGMENT));
            getSupportActionBar().setTitle(R.string.all_notes);
        } else if (id == R.id.nav_recent_edit) {
            switchFragment(mCurrentFragment, mFragments.get(RECENT_EDIT_FRAGMENT));
            getSupportActionBar().setTitle(R.string.recent_edit);
        } else if (id == R.id.nav_waste_basket) {
            switchFragment(mCurrentFragment, mFragments.get(WASTE_BASKET_FRAGMENT));
            getSupportActionBar().setTitle(R.string.waste_basket);
        } else if (id == R.id.nav_settings) {
            switchFragment(mCurrentFragment, mFragments.get(SETTINGS_FRAGMENT));
            getSupportActionBar().setTitle(R.string.settings);
        } else if (id == R.id.nav_share) {
            CommonUtil.shareApp(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_common, menu);
        return true;
    }

    @Override
    public void onClick(View menuItem) {
        switch (menuItem.getId()) {
            case R.id.fab_menu_item_camera: {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                intent.putExtra(INTENT_QUICK_ACTION, INTENT_QUICK_ACTION_CAMERA);
                startActivity(intent);
            }
            case R.id.fab_menu_item_attachment: {
            }
            break;
            case R.id.fab_menu_item_record: {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                intent.putExtra(INTENT_QUICK_ACTION, INTENT_QUICK_ACTION_RECORD);
                startActivity(intent);
            }
            break;
            case R.id.fab_menu_item_freehand: {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                intent.putExtra(INTENT_QUICK_ACTION, INTENT_QUICK_ACTION_FREEHAND);
                startActivity(intent);
            }
            break;
            case R.id.fab_menu_item_text: {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                startActivity(intent);
            }
            break;
        }
        mFabMenu.hideMenu(true);
    }

    private void initFabMenu() {
        mFabMenu = findViewById(R.id.floating_action_menu);
        FloatingActionButton fabBtnCamera = findViewById(R.id.fab_menu_item_camera);
        FloatingActionButton fabBtnAttachment = findViewById(R.id.fab_menu_item_attachment);
        FloatingActionButton fabBtnRecord = findViewById(R.id.fab_menu_item_record);
        FloatingActionButton fabBtnFreehand = findViewById(R.id.fab_menu_item_freehand);
        FloatingActionButton fabBtnText = findViewById(R.id.fab_menu_item_text);
        fabBtnCamera.setOnClickListener(this);
        fabBtnAttachment.setOnClickListener(this);
        fabBtnRecord.setOnClickListener(this);
        fabBtnFreehand.setOnClickListener(this);
        fabBtnText.setOnClickListener(this);
    }

    @AfterPermissionGranted(RC_ALL_PERM)
    public void requestPermissions() {
        if (!EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_tip),
                    RC_ALL_PERM, PERMISSIONS);
        }
    }
}
