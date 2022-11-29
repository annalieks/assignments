package com.example.db_gps.contacts;

import android.os.Build;
import android.provider.ContactsContract;

public class ContactsHelper {

    private static final String[] CONTACT_PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            };

    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_KEY_INDEX = 1;

    private static final String CONTACT_SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";

    private String[] selectionArgs = { "Іван" };

}
