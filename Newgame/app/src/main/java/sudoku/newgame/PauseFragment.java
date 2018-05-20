package sudoku.newgame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import sudoku.newgame.datahelpers.DataConstants;

public class PauseFragment extends Fragment {
    View fragment;
    boolean isActive = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_pause, container, false);
        RelativeLayout rl = fragment.findViewById(R.id.fragment_pause);
        rl.setBackgroundColor(DataConstants.getBackgroundColor(((GameActivity)getActivity()).theme));
        return fragment;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}