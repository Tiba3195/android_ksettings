package com.khadas.ksettings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class GPIOPinAdapter extends RecyclerView.Adapter<GPIOPinAdapter.ViewHolder> {
    private List<GPIOPin> gpioPins; // List of GPIO pin data

    public GPIOPinAdapter(List<GPIOPin> gpioPins) {
        this.gpioPins = gpioPins;
    }

    @Override
    public GPIOPinAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gpio_pin_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GPIOPinAdapter.ViewHolder holder, int position) {
        GPIOPin pin = gpioPins.get(position);
        holder.pinNumberTextView.setText(String.valueOf(pin.getPinNumber()));
        holder.directionTextView.setText(pin.getDirection().toString());
        holder.valueTextView.setText(pin.getValue().toString());
    }

    @Override
    public int getItemCount() {
        return gpioPins.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pinNumberTextView;
        public TextView directionTextView;
        public TextView valueTextView;

        public ViewHolder(View view) {
            super(view);
            pinNumberTextView = view.findViewById(R.id.pinNumberTextView);
            directionTextView = view.findViewById(R.id.directionTextView);
            valueTextView = view.findViewById(R.id.valueTextView);
        }
    }
}