package com.khadas.ksettings;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.preference.Preference;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khadas.util.GPIOUtils;

import java.util.List;

public class RecyclerViewPreference extends Preference{
    private  List<GPIOPin> myGPIOPins;
    private RecyclerView recyclerView;

    public RecyclerViewPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.recycler_preference_layout); // Set your custom layout here
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            myGPIOPins = GPIOUtils.getExportedGPIOPins(); // Initialize with your GPIO pin data
            GPIOPinAdapter adapter = new GPIOPinAdapter(myGPIOPins);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}
