package edu.utsa.cs3443.msaid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import edu.utsa.cs3443.msaid.model.Alarm;

/**
 * AlarmList is a RecyclerView adapter that binds a list of Alarm objects to the views
 * displayed in a RecyclerView. It manages the display of each alarm item in the list.
 */
public class AlarmList extends RecyclerView.Adapter<AlarmList.AlarmViewHolder> {
    // List of the Alarm objects that represents all the alarms that will be displayed in the RecyclerView
    private ArrayList<Alarm> alarms;

    /**
     * Constructor that initializes the adapter with the list of alarms.
     *
     * @param alarms The list of Alarm objects to be displayed.
     */
    public AlarmList(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    /**
     * Updates the list of alarms and notifies the adapter about any data changes.
     *
     * @param updatedAlarms The updated list of alarms.
     */
    public void setAlarms(ArrayList<Alarm> updatedAlarms) {
        this.alarms = updatedAlarms; // Updates the list of alarms
        notifyDataSetChanged(); // Notifies the RecyclerView that data has changed
    }

    /**
     * Called when a new ViewHolder is created to represent an item.
     *
     * @param parent   The parent ViewGroup that the ViewHolder will be attached to.
     * @param viewType The type of view.
     * @return A new instance of AlarmViewHolder.
     */
    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the item layout for each alarm and creates a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    /**
     * Binds the data from the alarms list to the views inside the ViewHolder.
     *
     * @param holder   The ViewHolder to bind data to.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        // Gets the alarm at the specified position
        Alarm alarm = alarms.get(position);
        // Sets the text view to display the alarm details using a string resource
        holder.alarmTextView.setText(
                holder.itemView.getContext().getString(R.string.alarm_display_format, alarm.getName(), alarm.getTime(), alarm.getAmPm()));
    }

    /**
     * Returns the total number of items (alarms) in the list.
     *
     * @return The number of alarms in the list.
     */
    @Override
    public int getItemCount() {
        return alarms.size();
    }

    /**
     * ViewHolder that holds references to the views for each item in the RecyclerView.
     * It represents each item in the alarm list and allows the adapter to easily reference views for binding data.
     */
    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView alarmTextView; // TextView that displays the alarm information

        /**
         * Constructor for the AlarmViewHolder. Gets the reference to the TextView used to display the alarm info.
         *
         * @param itemView The view for each individual item in the RecyclerView.
         */
        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            // Finds the TextView in the item layout using its ID
            alarmTextView = itemView.findViewById(R.id.alarmTextView);
        }
    }
}