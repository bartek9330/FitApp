package com.fitwebappclient.adminmain.ui.allusers;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.models.UserRoleDaoShort;
import com.fitwebappclient.loginandregistration.MainLoginActivity;

import java.util.ArrayList;

public class AllUsersRecyclerViewAdapter extends RecyclerView.Adapter<AllUsersRecyclerViewAdapter.ViewHolder> {
    private ArrayList<UserDaoShort> dataSet;
    private AllUserViewModel allUserViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userEmailTextView;
        private final TextView userLoginTextView;
        private final TextView userRoleTextView;
        private final ConstraintLayout row;
        private final View view;

        public ViewHolder(View view) {
            super(view);
            userEmailTextView = (TextView) view.findViewById(R.id.allusers_row_email);
            userLoginTextView = (TextView) view.findViewById(R.id.allusers_row_login);
            userRoleTextView = (TextView) view.findViewById(R.id.allusers_row_user_role);
            row = (ConstraintLayout) view.findViewById(R.id.admin_all_users_constraintlayout);
            this.view = view;

        }

        public TextView getUserLoginTextView() {
            return userLoginTextView;
        }

        public TextView getUserRoleTextView() {
            return userRoleTextView;
        }

        public TextView getUserEmailTextView() {
            return userEmailTextView;
        }

        public View getView() {
            return view;
        }

        public ConstraintLayout getRow() {
            return row;
        }
    }

    public AllUsersRecyclerViewAdapter(AllUserViewModel allUserViewModel) {
        this.dataSet = allUserViewModel.getAllUsers().getValue();
        this.allUserViewModel = allUserViewModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_users_recyclerview_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getUserEmailTextView().setText(dataSet.get(position).getEmail());
        viewHolder.getUserRoleTextView().setText(getRoleById(dataSet.get(position).getRoleId()));
        viewHolder.getUserLoginTextView().setText(dataSet.get(position).getLogin());
        if(dataSet.get(position).getId() == MainAdminActivity.getUserData().getId()){
            viewHolder.getRow().setBackgroundColor(viewHolder.getView().getResources().getColor(R.color.admin_row_color));
        }

    }

    private String getRoleById(int roleId) {
        return UserRoleDaoShort.fromId(roleId);
    }

    @Override
    public int getItemCount() {
        this.dataSet = allUserViewModel.getAllUsers().getValue();
        return allUserViewModel.getAllUsers().getValue().size();
    }
}
