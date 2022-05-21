package com.fitwebappclient.adminmain.ui.allusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.adminmain.ui.createuser.AdminActivityCreateUser;
import com.fitwebappclient.databinding.FragmentAllUsersBinding;
import com.fitwebappclient.loginandregistration.MainLoginActivity;
import com.fitwebappclient.loginandregistration.models.TokenJWN;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AllUsersFragment extends Fragment {

    private AllUserViewModel allUserViewModel;
    private FragmentAllUsersBinding binding;
    private RecyclerView allUsersRecyclerView;
    private ProgressDialog progressDialog;
    private AllUsersRecyclerViewAdapter allUsersRecyclerViewAdapter;
    private FloatingActionButton floatingActionButtonAddUser;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allUserViewModel =
                new ViewModelProvider(this).get(AllUserViewModel.class);
        binding = FragmentAllUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.progressDialog = new ProgressDialog(root.getContext());
        this.floatingActionButtonAddUser = binding.adminAddUserButton;

        setUpAddButton();


        return root;
    }

    @Override
    public void onResume () {
        enableLoadingBar();
        getDataFromServer();
        super.onResume();
    }


    private void setUpAddButton() {
        this.floatingActionButtonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity(AdminActivityCreateUser.class);
            }
        });
    }



    private void setUpRecyclerView() {
        allUsersRecyclerViewAdapter = new AllUsersRecyclerViewAdapter(allUserViewModel);
        this.allUsersRecyclerView = binding.recyclerviewAllUsers;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        allUsersRecyclerView.setHasFixedSize(true);
        allUsersRecyclerView.setItemViewCacheSize(512);
        allUsersRecyclerView.setLayoutManager(linearLayoutManager);
        allUsersRecyclerView.setAdapter(allUsersRecyclerViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(allUsersRecyclerView);
    }

    private void getDataFromServer() {
        LiveData<Boolean> status = allUserViewModel.getStatus();
        allUserViewModel.getAllUsersFromServer();

        if (!status.hasActiveObservers()) {
            enableLoadingBar();
            status.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean hasChange) {
                    if(hasChange){
                        disableLoadingBar();
                        // TODO notify adapter data has changed
                        if(allUserViewModel.getStatus().getValue() != null){
                            setUpRecyclerView();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void disableLoadingBar() {
        progressDialog.dismiss();
    }

    public void enableLoadingBar() {
        progressDialog.setTitle(getResources().getString(R.string.progress_dialog_title));
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog_describe));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            enableLoadingBar();
            allUserViewModel.deleteUser(allUserViewModel.getAllUsers().getValue().get(position).getId());
            LiveData<Boolean> status = allUserViewModel.getDeleteStatus();
            if (!status.hasActiveObservers()) {
                enableLoadingBar();
                status.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean hasChange) {
                        if(hasChange){
                            if(status.getValue()){
                                disableLoadingBar();
                                getDataFromServer();
                                if(MainAdminActivity.getUserData().getId() == allUserViewModel.getAllUsers().getValue().get(position).getId()){
                                    startLoginActivity(MainLoginActivity.class);
                                    getActivity().finish();
                                }
                            }else{
                                disableLoadingBar();
                                getDataFromServer();
                            }
                        }
                    }
                });
            }
        }
    };

    private void startLoginActivity(Class Class) {
        Intent intent = new Intent(getContext(), Class);
        startActivity(intent);
    }
}