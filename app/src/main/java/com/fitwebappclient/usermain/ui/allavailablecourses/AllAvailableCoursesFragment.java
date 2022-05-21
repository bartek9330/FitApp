package com.fitwebappclient.usermain.ui.allavailablecourses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.ui.allusers.AllUsersRecyclerViewAdapter;
import com.fitwebappclient.databinding.FragmentAllAvailableCoursesBinding;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.loginandregistration.models.UserData;
import com.fitwebappclient.usermain.MainUserActivity;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;

import java.util.ArrayList;


public class AllAvailableCoursesFragment extends Fragment {

    private AllAvailableCoursesViewModel viewModel;
    private FragmentAllAvailableCoursesBinding binding;
    private DialogsHandling dialogsHandling;
    private RecyclerView recyclerView;
    private AllAvailableCoursesCustomAdapter viewAdapter;
    private TextView textViewNotifyNoCoursess;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(AllAvailableCoursesViewModel.class);

        binding = FragmentAllAvailableCoursesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dialogsHandling = new DialogsHandling(root);

        setUpViews();
        return root;
    }

    private void setUpViews() {
        this.textViewNotifyNoCoursess = binding.allAvailableCoursesNoCourseNotify;
        this.recyclerView = binding.allAvailableCoursesRecyclerView;
        viewAdapter = new AllAvailableCoursesCustomAdapter(viewModel, dialogsHandling, this, binding.getRoot(),textViewNotifyNoCoursess);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(512);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(viewAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void addUserToCourse(CourseWithStepsDao courseWithStepsDao) {
        LiveData<ServerResponse> response = viewModel.addUserToCourse(courseWithStepsDao);
        dialogsHandling.enableLoadingBar();
        if (!response.hasActiveObservers()) {
            response.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse != null) {
                        handleUserAddedToCourse();
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    private void handleUserAddedToCourse() {
        Toast.makeText(binding.getRoot().getContext(), getText(R.string.user_assigned_to_course), Toast.LENGTH_SHORT).show();
        viewAdapter.prepareData();
    }
}