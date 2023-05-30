package com.group11.shoppuka.project.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.group11.shoppuka.databinding.FragmentProgressPageBinding;

public class ProgressPageFragment extends Fragment {

    private FragmentProgressPageBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProgressPageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();
        return view;
    }
}
