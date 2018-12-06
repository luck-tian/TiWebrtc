package com.hhtc.dialer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hhtc.dialer.animation.DialerActionButtonAnimation;
import com.hhtc.dialer.main.DialerFragment;
import com.hhtc.dialer.main.DialerTabPagerAdapter;
import com.hhtc.dialer.main.TabSelectedListenerImpl;
import com.hhtc.dialer.main.collects.CollectsFragment;
import com.hhtc.dialer.main.contacts.ContactsFragment;
import com.hhtc.dialer.main.recent.RecentFragment;
import com.hhtc.dialer.permission.PermissionsUtil;
import com.hhtc.dialer.view.TableViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 新版本dialer
 */
public class DialerActivity extends AppCompatActivity {

    public static final int REQUEST_READ_CONTACTS = 1;

    private TextView title_tips;
    private ImageButton add_contacts;
    private TabLayout page_navigation;
    private TableViewPager content_pager;
    private FloatingActionButton action_button;
    private DialerTabPagerAdapter adapter;
    private String[] stringTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        initView();
        setData();
    }


    private void initView() {
        title_tips = findViewById(R.id.title_tips);
        add_contacts = findViewById(R.id.add_contacts);
        page_navigation = findViewById(R.id.page_navigation);
        content_pager = findViewById(R.id.content_pager);
        action_button = findViewById(R.id.action_button);
        String[] deniedContactsPermissions =
                PermissionsUtil.getPermissionsCurrentlyDenied(
                        this, PermissionsUtil.allContactsGroupPermissionsUsedInDialer);
        if (deniedContactsPermissions.length > 0) {
            ActivityCompat.requestPermissions(this, PermissionsUtil.allPhoneGroupPermissionsUsedInDialer.toArray(new String[PermissionsUtil.allPhoneGroupPermissionsUsedInDialer.size()]),
                    REQUEST_READ_CONTACTS);
        }
    }


    private void setData() {
        adapter = new DialerTabPagerAdapter(getSupportFragmentManager(), createPager());
        content_pager.setAdapter(adapter);
        page_navigation.setupWithViewPager(content_pager);
        content_pager.setCurrentItem(1);
        Objects.requireNonNull(page_navigation.getTabAt(0)).setIcon(R.drawable.dialer_main_collect_icon_selector);
        Objects.requireNonNull(page_navigation.getTabAt(1)).setIcon(R.drawable.dialer_main_recent_call_icon_selector);
        Objects.requireNonNull(page_navigation.getTabAt(2)).setIcon(R.drawable.dialer_main_contacts_icon_selector);

        page_navigation.addOnTabSelectedListener(tabSelectedListener);
        action_button.setOnClickListener(actionButtonListener);
        stringTips = getResources().getStringArray(R.array.dialer_title_tips);
    }

    private List<DialerFragment> createPager() {
        List<DialerFragment> pagers = new ArrayList<>();
        pagers.add(CollectsFragment.newInstance());
        pagers.add(RecentFragment.newInstance());
        pagers.add(ContactsFragment.newInstance());
        return pagers;
    }

    private TabSelectedListenerImpl tabSelectedListener = new TabSelectedListenerImpl() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            title_tips.setText(stringTips[tab.getPosition()]);
            if (tab.getPosition() == 2) {
                add_contacts.setVisibility(View.VISIBLE);
            } else {
                add_contacts.setVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener actionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialerActionButtonAnimation.scaleOut(v);
            //启动拨号界面
        }
    };

}
