package com.group11.shoppuka.project.view.about;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.group11.shoppuka.databinding.ActivityAboutBinding;

public class ActivityAboutPage extends AppCompatActivity {
    private ActivityAboutBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(Color.parseColor("#cf052d"));
    }
}
