package com.hhtc.dialer;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hhtc.dialer.main.DialerTabPagerAdapter;
import com.hhtc.dialer.main.collects.CollectsFragment;
import com.hhtc.dialer.main.contacts.ContactsFragment;
import com.hhtc.dialer.main.recent.RecentFragment;
import com.hhtc.dialer.view.TableViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 新版本dialer
 */
public class DialerActivity extends AppCompatActivity {

    private TextView title_tips;
    private ImageButton add_contacts;
    private TabLayout page_navigation;
    private TableViewPager content_pager;
    private FloatingActionButton action_button;
    private DialerTabPagerAdapter adapter;

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
    }


    private void setData() {
        adapter = new DialerTabPagerAdapter(getSupportFragmentManager(), createPager());
        content_pager.setAdapter(adapter);
        page_navigation.setupWithViewPager(content_pager);
        Objects.requireNonNull(page_navigation.getTabAt(0)).setIcon(R.drawable.dialer_main_collect_icon_selector);
        Objects.requireNonNull(page_navigation.getTabAt(1)).setIcon(R.drawable.dialer_main_recent_call_icon_selector);
        Objects.requireNonNull(page_navigation.getTabAt(2)).setIcon(R.drawable.dialer_main_contacts_icon_selector);


    }

    private List<Fragment> createPager() {
        List<Fragment> pagers = new ArrayList<>();
        pagers.add(CollectsFragment.newInstance());
        pagers.add(RecentFragment.newInstance());
        pagers.add(ContactsFragment.newInstance());
        return pagers;
    }
}
