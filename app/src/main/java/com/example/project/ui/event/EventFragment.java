package com.example.project.ui.event;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project.GasEvent;
import com.example.project.SmokeEvent;
import com.example.project.TempEvent;
import com.example.project.VlagEvent;
import com.example.project.databinding.EventFragmentBinding;

import com.example.project.R;

public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;
    private EventFragmentBinding binding;
    public static EventFragment newInstance() {
        return new EventFragment();
    }

    private Button button_gas;
    private Button button_temp;
    private Button button_vlag;
    private Button button_smoke;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        binding = EventFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        button_gas=root.findViewById(R.id.button_eventgas);
        View.OnClickListener cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), GasEvent.class);
                startActivity(i);

            }
        };
        button_gas.setOnClickListener(cl);

        button_temp=root.findViewById(R.id.button_eventtemp);

        View.OnClickListener cl1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), TempEvent.class);
                startActivity(i);

            }
        };
        button_temp.setOnClickListener(cl1);

        button_vlag=root.findViewById(R.id.button_eventvlag);

        View.OnClickListener cl2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), VlagEvent.class);
                startActivity(i);

            }
        };
        button_vlag.setOnClickListener(cl2);

        button_smoke=root.findViewById(R.id.button_eventsmoke);

        View.OnClickListener cl3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SmokeEvent.class);
                startActivity(i);

            }
        };
        button_smoke.setOnClickListener(cl3);


        return root;
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}