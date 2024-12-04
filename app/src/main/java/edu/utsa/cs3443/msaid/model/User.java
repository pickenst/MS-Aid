package edu.utsa.cs3443.msaid.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user with a name, age, and a list of medicines.
 * Implements Parcelable to allow passing objects between Android components.
 */
public class User implements Parcelable {

    private String name;
    private int age;
    private List<Medicine> medicines;

    /**
     * Constructs a User object with the specified name and age.
     *
     * @param name the name of the user
     * @param age the age of the user
     */
    public User(String name, int age) {
        this.name = name;
        this.age = age;
        this.medicines = new ArrayList<>();
    }

    /**
     * Constructs a User object from a Parcel. Used for Parcelable implementation.
     *
     * @param in the Parcel containing the user data
     */
    protected User(Parcel in) {
        name = in.readString();
        age = in.readInt();
        medicines = new ArrayList<>();
        in.readTypedList(medicines, Medicine.CREATOR);
    }

    /**
     * Creator for the Parcelable implementation. Used to regenerate the object from a Parcel.
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * Returns the name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the age of the user.
     *
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the list of medicines the user is taking.
     *
     * @return a list of medicines
     */
    public List<Medicine> getMedicines() {
        return medicines;
    }

    /**
     * Sets the list of medicines for the user.
     *
     * @param medicines the list of medicines to set
     */
    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    /**
     * Adds a medicine to the user's list of medicines.
     *
     * @param medicine the medicine to add
     */
    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    /**
     * Removes a medicine from the user's list of medicines.
     *
     * @param medicine the medicine to remove
     */
    public void removeMedicine(Medicine medicine) {
        medicines.remove(medicine);
    }

    /**
     * Describes the contents of the Parcelable object. Returns 0 as no special objects are used.
     *
     * @return a bitmask indicating the kind of special objects contained in the Parcelable object
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the User object to a Parcel.
     *
     * @param parcel the Parcel to write the object to
     * @param flags additional flags about how the object should be written
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeTypedList(medicines);
    }
}