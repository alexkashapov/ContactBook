package contactbook.fake.contactbook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Fake on 13.12.2016.
 */

public class Contact implements Parcelable {
    private ArrayList<String> emails = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();
    private String name;
    private String id;

    public Contact(Contact contact) {
        name = contact.name;
        emails = contact.emails;
        numbers = contact.numbers;
        id = contact.id;
    }



    public Contact() {
        emails = new ArrayList<>();
        numbers = new ArrayList<>();
    }

    public Contact(String id, String name, ArrayList<String> phones, ArrayList<String> emails) {
        this.id = id;
        this.name = name;
        this.numbers.addAll(phones);
        this.emails.addAll(emails);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEmail(ArrayList<String> emails) {
        this.emails.addAll(emails);
    }

    public void addNumber(ArrayList<String> numbers) {
        this.numbers.addAll(numbers);
    }

    public ArrayList<String> getNumbers() {
        return new ArrayList<>(numbers);
    }

    public ArrayList<String> getEmails() {
        return new ArrayList<>(emails);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeStringList(emails);
        parcel.writeStringList(numbers);
    }

    public static  final Parcelable.Creator<Contact> CREATOR
            = new Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Contact(Parcel in) {
        name = in.readString();
        id = in.readString();
        in.readStringList(emails);
        in.readStringList(numbers);
    }
}

