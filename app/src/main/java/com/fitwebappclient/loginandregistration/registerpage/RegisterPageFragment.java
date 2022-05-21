package com.fitwebappclient.loginandregistration.registerpage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.fitwebappclient.R;
import com.fitwebappclient.interfaces.Checker;
import com.fitwebappclient.loginandregistration.loginpage.LoginPageFragment;
import com.fitwebappclient.loginandregistration.loginservice.LogApiService;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterPageFragment extends Fragment implements Checker {
    private RegistrationPageViewModel registrationPageViewModel;
    private ProgressDialog progressDialog;

    public RegisterPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_page, container, false);
        handleRegistrationButton(root);
        registrationPageViewModel = new RegistrationPageViewModel(getActivity());
        this.progressDialog = new ProgressDialog(root.getContext());

        return root;
    }

    private void handleRegistrationButton(View root) {
        TextInputLayout userLoginLayout = root.findViewById(R.id.registration_page_user_login_input);
        TextInputLayout userEmailLayout = root.findViewById(R.id.registration_page_user_email_input);
        TextInputLayout userPasswordLayout = root.findViewById(R.id.registration_page_user_password_input);
        Button logInButton = root.findViewById(R.id.registration_page_button_sign_up);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    handleValueForRegister(userLoginLayout, userEmailLayout, userPasswordLayout,root);
                } catch (Exception exception) {
                    throw exception;
                }
            }
        });
    }

    private void handleValueForRegister(TextInputLayout userLoginLayout,
                                        TextInputLayout userEmailLayout,
                                        TextInputLayout userPasswordLayout,View root) {
        String userLogin = checkValueInTextInputLayout(userLoginLayout,root);
        String userEmail = checkValueInTextInputLayout(userEmailLayout,root);
        String userPassword = checkValueInTextInputLayout(userPasswordLayout,root);

        if(userLogin != null && userEmail != null && userPassword != null  ){
            LiveData<Boolean> registrationStatus = registrationPageViewModel.getRegistrationStatus();
            enableLoadingBar();
            registrationPageViewModel.registerUser(
                    userLoginLayout.getEditText().getText().toString(),
                    userEmailLayout.getEditText().getText().toString(),
                    userPasswordLayout.getEditText().getText().toString());
            if (!registrationStatus.hasActiveObservers()) {
                registrationStatus.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean status) {
                        if (status) {
                            disableLoadingBar();
                            Toast.makeText(root.getContext(), getResources().getString(R.string.user_register_success), Toast.LENGTH_SHORT).show();
                            backToLoginPage();
                        } else {
                            disableLoadingBar();
                            Toast.makeText(root.getContext(), "Error on Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void backToLoginPage() {
        NavHostFragment.findNavController(RegisterPageFragment.this)
                .navigate(R.id.action_fragment_registration_to_fragment_login);
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
}