package com.example.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ProviderActivity;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.hook.binder_hook.BinderHookActivity;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        textView.setOnClickListener(v -> {
            // TODO 跳转ProviderActivity  测试ContentProvider
            Intent intent = new Intent(getActivity(), ProviderActivity.class);
            startActivity(intent);
        });

        final TextView textView2 = binding.textTest;
        textView2.setOnClickListener(v -> {
            // TODO
            Intent intent = new Intent(getActivity(), BinderHookActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}