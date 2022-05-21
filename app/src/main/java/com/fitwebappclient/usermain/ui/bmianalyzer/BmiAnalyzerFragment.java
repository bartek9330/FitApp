package com.fitwebappclient.usermain.ui.bmianalyzer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fitwebappclient.R;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.usermain.models.UserBMI;
import com.fitwebappclient.usermain.services.ChartGenerator;
import com.github.mikephil.charting.charts.CombinedChart;
import java.util.ArrayList;

public class BmiAnalyzerFragment extends Fragment {

    private BmiAnalyzerViewModel viewModel;
    private View root;
    private RecyclerView recyclerView;
    private BmiAnalyzerCustomAdapter bmiAnalyzerCustomAdapter;
    private DialogsHandling dialogsHandling;
    private CombinedChart combinedChart;

    public static BmiAnalyzerFragment newInstance() {
        return new BmiAnalyzerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(BmiAnalyzerViewModel.class);
        root = inflater.inflate(R.layout.fragment_bmi_analyzer, container, false);
        ;
        this.dialogsHandling = new DialogsHandling(root);
        this.combinedChart = root.findViewById(R.id.combined_chart);
        combinedChart.setVisibility(View.INVISIBLE);

        prepareData();
        return root;
    }

    private void setUpRecyclerView(ArrayList<UserBMI> userBMIS) {
        this.recyclerView = root.findViewById(R.id.bmi_analyzer_recycler_view);
        bmiAnalyzerCustomAdapter = new BmiAnalyzerCustomAdapter(userBMIS, root, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(512);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(bmiAnalyzerCustomAdapter);
    }

    public BmiAnalyzerViewModel getViewModel() {
        return this.viewModel;
    }

    private void prepareData() {
        LiveData<ArrayList<UserBMI>> response = viewModel.getAllBMIs();
        dialogsHandling.enableLoadingBar();
        if (!response.hasActiveObservers()) {
            response.observe(getViewLifecycleOwner(), new Observer<ArrayList<UserBMI>>() {
                @Override
                public void onChanged(ArrayList<UserBMI> serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse != null) {
                        try {
                            ChartGenerator chartGenerator = new ChartGenerator();
                            chartGenerator.setUpCharts(combinedChart, serverResponse);
                            setUpRecyclerView(serverResponse);
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            dialogsHandling.handleErrorWhileLoadingData();
                        }
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }
}