package edu.utsa.cs3443.msaid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.utsa.cs3443.msaid.model.Medicine;
import edu.utsa.cs3443.msaid.model.User;

import java.util.List;

/**
 * Adapter class for managing and displaying a list of medicines in a RecyclerView.
 * Provides functionality to handle click events based on the page context (delete or detail).
 */
public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicine> medicines;
    private User currentUser;
    private boolean isDeletePage;
    private OnMedicineClickListener onMedicineClickListener;

    /**
     * Constructor for MedicationAdapter.
     *
     * @param context                the context in which the adapter operates.
     * @param medicines              the list of Medicine objects to display.
     * @param onMedicineClickListener a listener for handling click events on a medicine item.
     * @param currentUser            the current user managing the medicines.
     * @param isDeletePage           flag indicating if the adapter is used in the delete page context.
     */
    public MedicationAdapter(Context context, List<Medicine> medicines, OnMedicineClickListener onMedicineClickListener, User currentUser, boolean isDeletePage) {
        this.context = context;
        this.medicines = medicines;
        this.onMedicineClickListener = onMedicineClickListener;
        this.currentUser = currentUser;
        this.isDeletePage = isDeletePage;
    }

    /**
     * Inflates the view for each item in the RecyclerView.
     *
     * @param parent   the parent ViewGroup.
     * @param viewType the view type of the new View.
     * @return a new MedicineViewHolder.
     */
    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_medication, parent, false);
        return new MedicineViewHolder(itemView);
    }

    /**
     * Binds data to the views in each item of the RecyclerView.
     *
     * @param holder   the ViewHolder to bind the data to.
     * @param position the position of the current item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.medicineName.setText(medicine.getName());

        // Handle item click based on the context (delete or detail page)
        holder.itemView.setOnClickListener(v -> {
            if (!isDeletePage) {
                // Navigate to the detail page
                Intent intent = new Intent(context, MedicationDetailActivity.class);
                intent.putExtra("medicine", medicine);  // Ensure Medicine is Parcelable
                intent.putExtra("user", currentUser);
                context.startActivity(intent);
            } else {
                // Navigate to the confirmation page for deletion
                Intent intent = new Intent(context, MedicationConfirmationActivity.class);
                intent.putExtra("medicine", medicine);
                intent.putExtra("user", currentUser);
                context.startActivity(intent);
            }
        });
    }

    /**
     * Returns the total number of items in the list.
     *
     * @return the size of the medicines list.
     */
    @Override
    public int getItemCount() {
        return medicines.size();
    }

    /**
     * Updates the list of medicines and refreshes the RecyclerView.
     *
     * @param newMedicines the updated list of medicines.
     */
    public void updateMedicines(List<Medicine> newMedicines) {
        this.medicines = newMedicines;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for holding the views of a single medicine item.
     */
    public class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName;

        /**
         * Constructor for MedicineViewHolder.
         *
         * @param itemView the item view layout.
         */
        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicine_name);
        }
    }

    /**
     * Interface for handling click events on medicine items.
     */
    public interface OnMedicineClickListener {
        /**
         * Handles a click event on a medicine item.
         *
         * @param medicine the Medicine object that was clicked.
         */
        void onMedicineClick(Medicine medicine);
    }
}