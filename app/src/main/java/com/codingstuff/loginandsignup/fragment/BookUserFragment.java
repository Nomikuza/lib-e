package com.codingstuff.loginandsignup.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codingstuff.loginandsignup.Domain.ModelPDF;
import com.codingstuff.loginandsignup.databinding.FragmentBookUserBinding;
import com.codingstuff.loginandsignup.recyclerview.AdapterPdfUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookUserFragment extends Fragment {

    //
    private String categoryId;
    private String category;
    private String uid;

    private ArrayList<ModelPDF> pdfArrayList;
    private AdapterPdfUser adapterPdfUser;

    //
    private FragmentBookUserBinding binding;

    private static final String TAG = "BOOKS_USER_TAG";

    public BookUserFragment() {
        // Required empty public constructor
    }


    public static BookUserFragment newInstance(String categoryId, String category, String uid) {
        BookUserFragment fragment = new BookUserFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("category", category);
        args.putString("uid", uid);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            category = getArguments().getString("category");
            uid = getArguments().getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookUserBinding.inflate(LayoutInflater.from(getContext()), container, false);

        Log.d(TAG, "onCreateView: Category: "+category);
        if (category.equals("All")){
            //
            loadAllBooks();
        } else if (category.equals("Most Viewed")) {
            //
            loadMostViewedDownloadedBooks("viewsCount");
        } else if (category.equals("Most Downloaded")) {
            //
            loadMostViewedDownloadedBooks("downloadsCount");
        } else {
            // load selected category Books
            loadCategorizedBooks();
        }

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterPdfUser.getFilter().filter(s);
                }
                catch (Exception e){
                    Log.d(TAG, "onTextChanged "+e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }

    private void loadAllBooks() {
        //
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
                pdfArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //
                    ModelPDF model = ds.getValue(ModelPDF.class);
                    //
                    pdfArrayList.add(model);
                }
                //
                adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);
                //
                binding.rvBook.setAdapter(adapterPdfUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadMostViewedDownloadedBooks(String orderBy) {
        //
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild(orderBy).limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //
                pdfArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //
                    ModelPDF model = ds.getValue(ModelPDF.class);
                    //
                    pdfArrayList.add(model);
                }
                //
                adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);
                //
                binding.rvBook.setAdapter(adapterPdfUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadCategorizedBooks() {
        //
        pdfArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //
                        pdfArrayList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            //
                            ModelPDF model = ds.getValue(ModelPDF.class);
                            //
                            pdfArrayList.add(model);
                        }
                        //
                        adapterPdfUser = new AdapterPdfUser(getContext(), pdfArrayList);
                        //
                        binding.rvBook.setAdapter(adapterPdfUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}