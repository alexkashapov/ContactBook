package contactbook.fake.contactbook;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Created by Fake on 13.12.2016.
 */

public class ContactReader {
    ContentResolver resolver;

    public ContactReader(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public ArrayList<Contact> getContactsInfo() {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor cur = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                ArrayList<String> phones = new ArrayList<>();
                ArrayList<String> emails = new ArrayList<>();
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = resolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phones.add(phone);
                    }
                    pCur.close();
                }
                Cursor emailCur = resolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    String email = emailCur.getString(
                            emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    emails.add(email);
                }
                emailCur.close();
                contacts.add(new Contact(id, name, phones, emails));
            }

        }
        cur.close();
        return contacts;
    }

    public Contact getContactInfo(String contactID, String contactName) {
        Cursor cur = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        String id = contactID;
        String name = contactName;
        ArrayList<String> phones = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        if (cur.moveToNext()) {
            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                Cursor pCur = resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (pCur.moveToNext()) {
                    String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phones.add(phone);
                }
                pCur.close();
            }
            Cursor emailCur = resolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[]{id}, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(
                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                emails.add(email);
            }
            emailCur.close();
        }
        cur.close();
        return new Contact(id, name, phones, emails);
    }
}

