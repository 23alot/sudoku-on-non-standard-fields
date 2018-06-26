package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import sudoku.newgame.datahelpers.DataConstants;

public class TuturuActivity extends AppCompatActivity {
    public int theme = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_tuturu);
        SharedPreferences sharedPreferences = TuturuActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        tabLayout.setTabTextColors(DataConstants.getMainTextColor(theme),DataConstants.getMainTextColor(theme));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        LeaderBoardFragment leaderBoardFragment = new LeaderBoardFragment();
        UserStatFragment userStatFragment = new UserStatFragment();
        adapter.addFragment(leaderBoardFragment,  this.getResources().getString(R.string.leaderboard));
        adapter.addFragment(userStatFragment, this.getResources().getString(R.string.personal));
        viewPager.setAdapter(adapter);
    }
}
