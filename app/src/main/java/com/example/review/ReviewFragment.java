package com.example.review;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewFragment extends Fragment {

    private ReviewAdapter mReviewAdapter;
    private String photoPathorLink;

    private boolean locallyEdited;
    private boolean locallyChangedPhoto;
    private boolean signedInFB;

    private String name;
    List<Review> reviews;

    private SharedPreferences pref;

    public ReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        Intent intent = getActivity().getIntent();
        boolean sortFilter = intent.getBooleanExtra("sort&filter",false);
        boolean search = intent.getBooleanExtra("search", false);

        pref = getActivity().getSharedPreferences("searchSettings", Context.MODE_PRIVATE);

        ReviewDBHandler dbHandler = new ReviewDBHandler(getActivity());
        if (sortFilter){
            reviews = dbHandler.getFilteredReviews(pref.getString("sort",null),pref.getString("category",null),
                    pref.getInt("attr1",-1),pref.getFloat("min1",-1),pref.getFloat("max1",-1),
                    pref.getInt("attr2",-1),pref.getFloat("min2",-1),pref.getFloat("max2",-1),
                    pref.getInt("attr3",-1),pref.getFloat("min3",-1),pref.getFloat("max3",-1));
            if (reviews.size() == 0){
                TextView noReviewText = (TextView)rootView.findViewById(R.id.noresult);
                noReviewText.setVisibility(View.VISIBLE);
            }
        }
        else if(search){
            reviews = dbHandler.searchReview(intent.getStringExtra("query"));
            if (reviews.size() == 0){
                TextView noReviewText = (TextView)rootView.findViewById(R.id.noresult);
                noReviewText.setVisibility(View.VISIBLE);
            }
        }
        else{
            reviews = dbHandler.getAllReview();
            if (reviews.size() == 0){
                TextView noReviewText = (TextView)rootView.findViewById(R.id.noreview);
                noReviewText.setVisibility(View.VISIBLE);
            }
        }



        pref = this.getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        locallyEdited = pref.getBoolean("locallyEdited", false);
        signedInFB = pref.getBoolean("signedInAsFB", true);
        locallyChangedPhoto = pref.getBoolean("locallyChangedPhoto", false);

        if (locallyChangedPhoto) {
            photoPathorLink = pref.getString("profilePhoto", "NULL");
        }
        if (locallyEdited) {
            name = pref.getString("editName", "null");
        } else {
            name = pref.getString("name", "null");
        }

        mReviewAdapter = new ReviewAdapter(getActivity(), R.layout.list_item_review, reviews, name, photoPathorLink, locallyChangedPhoto, signedInFB);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_review);
        listView.setAdapter(mReviewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ReviewArticleActivity.class);
                intent.putExtra("reviewID",reviews.get(position).getReviewID());
                startActivity(intent);
            }
        });
        return rootView;
    }

}