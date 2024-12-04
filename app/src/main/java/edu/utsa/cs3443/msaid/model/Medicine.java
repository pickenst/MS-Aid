package edu.utsa.cs3443.msaid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Represents a medicine with a name and usage instructions. This class implements the
 * Parcelable interface to allow Medicine objects to be passed between Android components.
 */
public class Medicine implements Parcelable {

    /**
     * The name of the medicine.
     */
    private String name;

    /**
     * Instructions on how to use the medicine.
     */
    private String instructions;

    /**
     * Constructs a Medicine object with the specified name and instructions.
     *
     * @param name         the name of the medicine
     * @param instructions the usage instructions for the medicine
     */
    public Medicine(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
    }

    /**
     * Constructs a Medicine object from a Parcel.
     *
     * @param in the Parcel containing the Medicine data
     */
    protected Medicine(Parcel in) {
        name = in.readString();
        instructions = in.readString();
    }

    /**
     * A Parcelable.Creator implementation to create Medicine objects from Parcels.
     */
    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        /**
         * Creates a Medicine object from a Parcel.
         *
         * @param in the Parcel containing the Medicine data
         * @return a new Medicine object
         */
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        /**
         * Creates a new array of Medicine objects.
         *
         * @param size the size of the array
         * @return an array of Medicine objects
         */
        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    /**
     * Gets the name of the medicine.
     *
     * @return the name of the medicine
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the usage instructions for the medicine.
     *
     * @return the usage instructions
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Provides a formatted string containing the medicine's name and instructions.
     *
     * @return a string representation of the medicine
     */
    public String getInfo() {
        return "Medicine: " + name + "\nInstructions: " + instructions;
    }

    /**
     * Describes the contents of the Parcelable. Always returns 0 as no special objects are present.
     *
     * @return 0 (default value for describeContents)
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the medicine's data to a Parcel.
     *
     * @param dest  the Parcel where the data should be written
     * @param flags additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(instructions);
    }

    /**
     * Checks if this Medicine object is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return Objects.equals(name, medicine.name) &&
                Objects.equals(instructions, medicine.instructions);
    }

    /**
     * Generates a hash code for the Medicine object based on its fields.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, instructions);
    }
}