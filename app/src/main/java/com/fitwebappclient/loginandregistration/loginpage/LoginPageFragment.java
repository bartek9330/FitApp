package com.fitwebappclient.loginandregistration.loginpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.fitwebappclient.interfaces.Checker;
import com.fitwebappclient.R;
import com.fitwebappclient.loginandregistration.models.UserData;
import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.usermain.MainUserActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

public class LoginPageFragment extends Fragment implements Serializable, Checker {
    private LoginPageViewModel viewModel;
    private ProgressDialog progressDialog;

    public LoginPageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_page, container, false);
        this.progressDialog = new ProgressDialog(root.getContext());
        this.viewModel =  new LoginPageViewModel(getActivity());
        handleLoginButton(root);
        handleRegistrationButton(root);

        return root;
    }

    private void handleRegistrationButton(View root) {
        Button signUpButton = root.findViewById(R.id.login_page_button_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LoginPageFragment.this)
                        .navigate(R.id.action_fragment_login_to_fragment_registration);
            }
        });
    }

    private void handleLoginButton(View root) {
        TextInputLayout userLoginLayout = root.findViewById(R.id.dadad);
        TextInputLayout userPasswordLayout = root.findViewById(R.id.login_page_user_password_input);
        Button logInButton = root.findViewById(R.id.login_page_button_sign_in);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleLoginAction(userLoginLayout, userPasswordLayout);
                } catch (Exception exception) {
                    throw exception;
                }
            }
        });
    }

    private void handleLoginAction(TextInputLayout userLoginLayout, TextInputLayout userPasswordLayout) {
        View view = getView();
        String userLogin = checkValueInTextInputLayout(userLoginLayout,view);
        String userPassword = checkValueInTextInputLayout(userPasswordLayout,view);
        if (userLogin != null && userPassword != null) {
            LiveData<Boolean> loginStatus = viewModel.getLoginStatus();
            viewModel.logIntoAccount(userLoginLayout.getEditText().getText().toString(),
                    userPasswordLayout.getEditText().getText().toString());
            enableLoadingBar();
            if (!loginStatus.hasActiveObservers()) {
                loginStatus.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean status) {
                        if (status) {
                            disableLoadingBar();
                            if(viewModel.getUserData().getUserRoles().getId() == 1){
                                startAdminMainActivity(viewModel.getUserData());
                            }else if(viewModel.getUserData().getUserRoles().getId() == 2){
                                startUserMainActivity(viewModel.getUserData());
                            }
                        } else {
                            disableLoadingBar();
                            showDialogWrongUserData(

                            );
                        }
                    }
                });
            }
        }
    }

    private void showDialogWrongUserData() {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.alert_wrong_user_data_title))
                .setMessage(getResources().getString(R.string.alert_wrong_user_data_describe))
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void startAdminMainActivity(UserData userData) {
        Intent intent = new Intent(getContext(), MainAdminActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        getActivity().finish();
    }

    private void startUserMainActivity(UserData userData) {
        Intent intent = new Intent(getContext(), MainUserActivity.class);
        intent.putExtra("userData", userData);
        startActivity(intent);
        getActivity().finish();
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

//    private String checkValue(TextInputLayout textInputLayout) {
//        String value = textInputLayout.getEditText().getText().toString();
//        if (value.isEmpty()) {
//            textInputLayout.setError(getResources().getString(R.string.error_empty_field));
//            textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    textInputLayout.setError(null);
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//            return null;
//        } else {
//            return value;
//        }
//    }
}