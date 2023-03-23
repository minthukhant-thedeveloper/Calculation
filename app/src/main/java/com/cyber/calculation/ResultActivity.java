package com.cyber.calculation;

import androidx.activity.result.ActivityResultCaller;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cyber.calculation.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type1Option = getIntent().getStringExtra(Constants.TYPE_1_OPTION);
        String type1Result = getIntent().getStringExtra(Constants.TYPE_1_RESULT);
        String type1Remark = getIntent().getStringExtra(Constants.TYPE_1_REMARK);

        String type2Option = getIntent().getStringExtra(Constants.TYPE_2_OPTION);
        String type2Result = getIntent().getStringExtra(Constants.TYPE_2_RESULT);
        String type2Remark = getIntent().getStringExtra(Constants.TYPE_2_REMARK);

        binding.type1Option.setText(type1Option);
        binding.type1Result.setText(type1Result);
        binding.type1Remark.setText(type1Remark);

        binding.type2Option.setText(type2Option);
        binding.type2Result.setText(type2Result);
        binding.type2Remark.setText(type2Remark);


    }
}