package com.fitwebappclient.adminmain.ui.allcourses;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.ui.allusers.AllUserViewModel;
import com.fitwebappclient.adminmain.ui.allusers.AllUsersRecyclerViewAdapter;
import com.fitwebappclient.databinding.FragmentAllCoursesBinding;
import com.fitwebappclient.databinding.FragmentAllUsersBinding;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AllCoursesFragment extends Fragment {
    private View root;
    private ProgressDialog progressDialog;

    private RecyclerView recyclerViewAllCourses;
    private AllCoursesRecyclerViewAdapter allCoursesRecyclerViewAdapter;
    private FragmentAllCoursesBinding binding;
    private AllCoursesViewModel viewModel;
    private FloatingActionButton addNewCourseFloatingButton;
    private DialogsHandling dialogsHandling;

    public FloatingActionButton getAddNewCourseFloatingButton() {
        return addNewCourseFloatingButton;
    }

    public void setAddNewCourseFloatingButton(FloatingActionButton addNewCourseFloatingButton) {
        this.addNewCourseFloatingButton = addNewCourseFloatingButton;
    }

    public AllCoursesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(AllCoursesViewModel.class);
        binding =  FragmentAllCoursesBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        this.progressDialog = new ProgressDialog(root.getContext());
        dialogsHandling = new DialogsHandling(root);
        setUpView();
        return root;
    }

    private void setUpView() {
        this.getDataFromServer();
        setAddNewCourseFloatingButton(binding.adminAddNewCourseFloatingbutton);

        getAddNewCourseFloatingButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.openCourseActivity(null,view);
            }
        });
    }

    @Override
    public void onResume () {
        getDataFromServer();
        super.onResume();
    }

    private void getDataFromServer() {
        LiveData<Boolean> status = viewModel.getStatus();
        viewModel.getAllCoursesFromServer();
        dialogsHandling.enableLoadingBar();
        if (!status.hasActiveObservers()) {
            status.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean hasChange) {
                    if(hasChange){
                        dialogsHandling.disableLoadingBar();
                        // TODO notify adapter data has changed
                        if(viewModel.getStatus().getValue()){
                            setUpRecyclerView();
                        }else{
                            dialogsHandling.handleErrorWhileLoadingData();
                        }
                    }
                }
            });
        }
    }

    private void setUpRecyclerView() {
        allCoursesRecyclerViewAdapter = new AllCoursesRecyclerViewAdapter(viewModel);
        this.recyclerViewAllCourses = binding.recyclerviewAllCourses;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.recyclerViewAllCourses.setHasFixedSize(true);
        this.recyclerViewAllCourses.setItemViewCacheSize(512);
        this.recyclerViewAllCourses.setLayoutManager(linearLayoutManager);
        this.recyclerViewAllCourses.setAdapter(allCoursesRecyclerViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewAllCourses);
    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            handleCourseStepOnServer(viewModel.getAllCourses().getValue().get(position));
        }
    };

    private void handleCourseStepOnServer(CourseDao courseDao) {
        LiveData<ServerResponse> serverResponse = viewModel.deleteCourseFromServer(courseDao);
        dialogsHandling.enableLoadingBar();
        if (!serverResponse.hasActiveObservers()) {
            serverResponse.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse response) {
                    dialogsHandling.disableLoadingBar();
                    if (response != null) {
                        getDataFromServer();
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

}