package com.example.review;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

/**
 * Fragment dialog displaying tab host...
 */
public class SearchSettings extends DialogFragment
{

    private Intent intent;

    private SharedPreferences pref;


    private SectionsPagerAdapter sectionsPagerAdapter;
    private WrapViewPager viewPager;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(300,300);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_settings, container);

        pref = this.getActivity().getSharedPreferences("searchSettings" , Context.MODE_PRIVATE);
        // tab slider
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (WrapViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        PagerTitleStrip pagerTitleStrip = (PagerTitleStrip) view.findViewById(R.id.pager_title_strip);
        pagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        pagerTitleStrip.setGravity(Gravity.CENTER_VERTICAL);

        Button done = (Button) view.findViewById(R.id.done_button);
        intent = new Intent(getActivity(), MainActivity.class);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("sort&filter",true);
                startActivity(intent);
                dismiss();

            }
        });
        return view;
    }

    // ------------------------------------------------------------------------
    // inner classes
    // ------------------------------------------------------------------------

    /**
     * Used for tab paging...
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        private int mCurrentPosition = -1;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
            {
                // find first fragment...
                SortFragment ft1 = new SortFragment();
                return ft1;
            }
            else if (position == 1)
            {
                // find first fragment...
                FilterFragment ft2 = new FilterFragment();
                return ft2;
            }

            return null;
        }
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (position != mCurrentPosition) {
                Fragment fragment = (Fragment) object;
                WrapViewPager pager = (WrapViewPager) container;
                if (fragment != null && fragment.getView() != null) {
                    mCurrentPosition = position;
                    pager.measureCurrentView(fragment.getView());
                }
            }
        }
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sort by";
                case 1:
                    return "Filter by";
            }
            return null;
        }
    }
}