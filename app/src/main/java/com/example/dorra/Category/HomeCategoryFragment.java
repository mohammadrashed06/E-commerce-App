package com.example.dorra.Category;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dorra.Model.Category;
import com.example.dorra.R;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class HomeCategoryFragment extends Fragment {
    public HomeCategoryFragment() {
    }

    List<Category> categoryList = new ArrayList<>();
    Category category1 = new Category();
    Category category2 = new Category();
    Category category3 = new Category();
    Category category4 = new Category();
    Category category5 = new Category();
    Category category6 = new Category();
    Category category7 = new Category();
    Category category8 = new Category();
    Category category9 = new Category();
    Category category10 = new Category();
    Category category11 = new Category();
    Category category12 = new Category();
    Category category13 = new Category();
    Category category14 = new Category();
    Category category15 = new Category();
    private RecyclerView categoryRecyclerView;
    private CategotyHomeAdapter categotyHomeAdapter;
}