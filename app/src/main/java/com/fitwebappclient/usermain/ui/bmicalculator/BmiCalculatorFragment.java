package com.fitwebappclient.usermain.ui.bmicalculator;

import android.annotation.SuppressLint;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fitwebappclient.R;
import com.fitwebappclient.databinding.FragmentBmiCalculatorBinding;
import com.fitwebappclient.interfaces.Checker;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.usermain.services.BMI;
import com.fitwebappclient.usermain.models.UserBMI;
import com.google.android.material.textfield.TextInputLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


public class BmiCalculatorFragment extends Fragment implements DatePickerDialog.OnDateSetListener, Checker, BMI {

    private BmiCalculatorViewModel viewModel;
    private TextInputLayout textInputLayoutHeight;
    private TextInputLayout textInputLayoutWeight;
    private Button takeDateButton;
    private Button saveButton;
    private UserBMI userBMI;
    private DialogsHandling dialogsHandling;
    private Button calcBmiButton;
    private TextView bmiValueTextview;
    private TextView bmiStatusTextview;

    private FragmentBmiCalculatorBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(BmiCalculatorViewModel.class);

        binding = FragmentBmiCalculatorBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dialogsHandling = new DialogsHandling(root);
        userBMI = new UserBMI();

        setUpViews();

        return root;
    }

    private void setUpViews() {
        textInputLayoutHeight = binding.textinputlayoutBmiCalculatorHeight;
        textInputLayoutWeight = binding.textinputlayoutBmiCalculatorWeight;
        takeDateButton = binding.bmiCalculatorDatePicker;
        saveButton = binding.bmiCalculatorSave;
        bmiStatusTextview = binding.bmiCalculatorStatus;
        bmiValueTextview = binding.bmiCalculatorValue;
        calcBmiButton = binding.bmiCalculatorCalcMeBmi;
        getBmiValueTextview().setVisibility(View.INVISIBLE);
        getBmiStatusTextview().setVisibility(View.INVISIBLE);

        setUpDefaultDate();
        handlingButtons();
    }

    private void handlingButtons() {
        getCalcBmiButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCalcBmi();
            }
        });

        getTakeDateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDateDialog();
            }
        });

        getSaveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSaveButton();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void handleCalcBmi() {
        try {
            View root = binding.getRoot();
            String weight = checkValueInTextInputLayout(getTextInputLayoutWeight(), root);
            String height = checkValueInTextInputLayout(getTextInputLayoutHeight(), root);
            if (weight != null && height != null) {
                getBmiValueTextview().setVisibility(View.VISIBLE);
                getBmiStatusTextview().setVisibility(View.VISIBLE);
                double bmi = calcBMI(Double.valueOf(weight), Double.parseDouble(height) / 100);
                getBmiValueTextview().setText("BMI: " + String.format("%.2f", bmi));
                checkBmiValue(bmi);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }



    @SuppressLint("SetTextI18n")
    private void checkBmiValue(double bmi) {
        if (bmi >= 18.5 && bmi <= 25.0) {
            getBmiStatusTextview().setText(getText(R.string.bmi_status) + " " + getText(R.string.bmi_status_normal));
        } else if (bmi <= 18.5) {
            getBmiStatusTextview().setText(getText(R.string.bmi_status) + " " + getText(R.string.bmi_status_UnderWeight));
        } else if (bmi >= 25.0 && bmi <= 30.0) {
            getBmiStatusTextview().setText(getText(R.string.bmi_status) + " " + getText(R.string.bmi_status_ovrWeight));
        } else if (bmi > 30.0) {
            getBmiStatusTextview().setText(getText(R.string.bmi_status) + " " +  getText(R.string.bmi_status_obese));
        }
    }

    private void handleSaveButton() {
        try {
            View root = binding.getRoot();
            String weight = checkValueInTextInputLayout(getTextInputLayoutWeight(), root);
            String height = checkValueInTextInputLayout(getTextInputLayoutHeight(), root);
            if (weight != null && height != null) {
                this.userBMI.setDate(getTakeDateButton().getText().toString());
                this.userBMI.setHeight(Integer.parseInt(height));
                double grams = Double.parseDouble(weight) * 1000;
                this.userBMI.setWeight((int) grams);
                saveUserBMIOnServer(this.userBMI);
            }
        } catch (Exception exception) {
            System.out.println(exception);
            dialogsHandling.handleErrorWhileLoadingData();
        }
    }

    public void saveUserBMIOnServer(UserBMI userBMI) {
        LiveData<ServerResponse> response = viewModel.saveUserBMI(userBMI);
        dialogsHandling.enableLoadingBar();
        if (!response.hasActiveObservers()) {
            response.observe(getViewLifecycleOwner(), new Observer<ServerResponse>() {
                @Override
                public void onChanged(ServerResponse serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse != null) {
                        handleUserBmiSaved();
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    private void handleUserBmiSaved() {
        Toast.makeText(binding.getRoot().getContext(), getText(R.string.user_bmi_saved), Toast.LENGTH_SHORT).show();
    }

    private void setUpDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        month = month + 1;
        String monthString = ((String.valueOf(month).length() == 1) ? "0" + month : String.valueOf(month));
        getTakeDateButton().setText(day + "-" + monthString + "-" + year);
    }

    public TextInputLayout getTextInputLayoutHeight() {
        return textInputLayoutHeight;
    }

    public TextInputLayout getTextInputLayoutWeight() {
        return textInputLayoutWeight;
    }

    public Button getTakeDateButton() {
        return takeDateButton;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCalcBmiButton() {
        return calcBmiButton;
    }

    public TextView getBmiValueTextview() {
        return bmiValueTextview;
    }

    public TextView getBmiStatusTextview() {
        return bmiStatusTextview;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openDateDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        month = month + 1;
        String monthString = ((String.valueOf(month).length() == 1) ? "0" + month : String.valueOf(month));
        getTakeDateButton().setText(day + "-" + monthString + "-" + year);
    }
}