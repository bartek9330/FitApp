package com.fitwebappclient.usermain.ui.mycourses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.databinding.FragmentMyCoursesBinding;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.MainLoginActivity;
import com.fitwebappclient.usermain.ui.allavailablecourses.AllAvailableCoursesCustomAdapter;


public class MyCoursesFragment extends Fragment {

    private MyCoursesViewModel viewModel;
    private FragmentMyCoursesBinding binding;
    private DialogsHandling dialogsHandling;
    private RecyclerView recyclerView;
    private MyCoursesCustomAdapter viewAdapter;
    private TextView textViewNotifyNoCoursess;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(MyCoursesViewModel.class);

        binding = FragmentMyCoursesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dialogsHandling = new DialogsHandling(root);

        setUpViews();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setUpViews() {
        this.textViewNotifyNoCoursess = binding.myCoursesNoCourseNotify;

        this.recyclerView = binding.myCoursesRecyclerview;
        viewAdapter = new MyCoursesCustomAdapter(viewModel, dialogsHandling, this, binding.getRoot(),recyclerView,textViewNotifyNoCoursess);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(512);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(viewAdapter);
    }


}