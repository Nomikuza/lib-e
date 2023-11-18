package com.codingstuff.loginandsignup.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.codingstuff.loginandsignup.Domain.ModelCategory;
import com.codingstuff.loginandsignup.databinding.ActivityDashboardUserBinding;
import com.codingstuff.loginandsignup.fragment.BookUserFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardUserActivity extends AppCompatActivity {

    public ActivityDashboardUserBinding binding;
    public ArrayList<ModelCategory> categoryArrayList;
    public ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPagerAdapter(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(DashboardUserActivity.this, IntroActivity.class));
                finish();
            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardUserActivity.this, ProfileUserActivity.class));
            }
        });
    }

    private void setupViewPagerAdapter(ViewPager viewPager){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);

        categoryArrayList = new ArrayList<>();

        //
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Android Category");//
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear
                categoryArrayList.clear();

                /*Load Categ, most viewed, most downloaded*/
                //Add data to models
                ModelCategory modelAll = new ModelCategory("01","All","",1);
                ModelCategory modelMostViewed = new ModelCategory("02","Most Viewed","",1);
                ModelCategory modelMostDownloaded = new ModelCategory("03","Most Downloaded","",1);
                //
                categoryArrayList.add(modelAll);
                categoryArrayList.add(modelMostViewed);
                categoryArrayList.add(modelMostDownloaded);
                //
                viewPagerAdapter.addFragment(BookUserFragment.newInstance(
                        ""+modelAll.getId(),
                        ""+modelAll.getCategory(),
                        ""+modelAll.getUid()
                ), modelAll.getCategory());
                viewPagerAdapter.addFragment(BookUserFragment.newInstance(
                        ""+modelMostViewed.getId(),
                        ""+modelMostViewed.getCategory(),
                        ""+modelMostViewed.getUid()
                ), modelMostViewed.getCategory());
                viewPagerAdapter.addFragment(BookUserFragment.newInstance(
                        ""+modelMostDownloaded.getId(),
                        ""+modelMostDownloaded.getCategory(),
                        ""+modelMostDownloaded.getUid()
                ), modelMostDownloaded.getCategory());
                //
                viewPagerAdapter.notifyDataSetChanged();

                //
                for (DataSnapshot ds: snapshot.getChildren()){
                    //
                    ModelCategory model = ds.getValue(ModelCategory.class);
                    //
                    categoryArrayList.add(model);
                    //
                    viewPagerAdapter.addFragment(BookUserFragment.newInstance(
                            ""+model.getId(),
                            ""+model.getCategory(),
                            ""+model.getUid()), model.getCategory());
                    //
                    viewPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //
        viewPager.setAdapter(viewPagerAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<BookUserFragment> fragmentsList = new ArrayList<>();
        private ArrayList<String> fragmentTitleList = new ArrayList<>();
        private Context context;
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        private void addFragment(BookUserFragment fragment, String title){
            //
            fragmentsList.add(fragment);
            //
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
