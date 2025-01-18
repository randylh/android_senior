package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.component.service.ServiceActivity;
import com.example.myapplication.component.service.binder.BinderActivity;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.hook.ams_pms_hook.AmsPmsHookActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.testUiThread;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        textView.setOnClickListener(v -> requestQuestion());

        final TextView testService = binding.testService;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), testService::setText);
        testService.setOnClickListener(v -> {
            // TODO 跳转ServiceActivity
            Intent intent = new Intent(getActivity(), ServiceActivity.class);
            startActivity(intent);
        });

        final TextView testBindService = binding.testBindService;
        testBindService.setOnClickListener(v -> {
            // TODO 跳转BindServiceActivity
            Intent intent = new Intent(getActivity(), BinderActivity.class);
            startActivity(intent);
        });

        final TextView testAmsHook = binding.testAmsHook;
        testAmsHook.setOnClickListener(v -> {
            // TODO 跳转AmsHookActivity
            Intent intent = new Intent(getActivity(), AmsPmsHookActivity.class);
            startActivity(intent);
        });

        return root;
    }



    private void requestQuestion() {
        // 子线程
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }

                // 模拟服务器请求，返回问题
                String title = "我帅气不？";
                showQuestionInDialog(title);
            }
        }.start();
    }


    private void showQuestionInDialog(String title) {
        Looper.prepare();
        QuestionDialog questionDialog = new QuestionDialog(getActivity());
        questionDialog.show(title);
        Looper.loop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}