package edu.utsa.cs3443.msaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import edu.utsa.cs3443.msaid.model.Alarm;

public class AlarmList extends RecyclerView.Adapter<AlarmList.AlarmViewHolder> {
    private ArrayList<Alarm> alarms;

    public AlarmList(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.alarmTextView.setText(alarm.getName() + ": " + alarm.getTime() + " " + alarm.getAmPm());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView alarmTextView;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            alarmTextView = itemView.findViewById(R.id.alarmTextView);
        }
    }
}