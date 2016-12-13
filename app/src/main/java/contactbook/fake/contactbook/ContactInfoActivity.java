package contactbook.fake.contactbook;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Fake on 13.12.2016.
 */

public class ContactInfoActivity extends AppCompatActivity {

    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_contact_info);
        TextView contactName = (TextView) findViewById(R.id.label_name);
        LinearLayout infoContainer = (LinearLayout) findViewById(R.id.linear);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 10);
        contact = getIntent().getParcelableExtra(
                Contact.class.getCanonicalName());
        contactName.setText(contact.getName());
        if (contact.getNumbers().size() > 0) {
            infoContainer.addView(createLabelCard("Phones:"), params);

            for (int i = 0; i < contact.getNumbers().size(); i++) {
                CardView card = initPhoneCard(contact.getNumbers().get(i));
                card.setUseCompatPadding(true);
                card.setLayoutParams(params);
                card.setRadius(9);
                card.setContentPadding(15, 15, 15, 15);
                card.setBackgroundColor(Color.WHITE);
                card.setMaxCardElevation(15);
                card.setCardElevation(9);

                infoContainer.addView(card, params);
            }
        }
        if (contact.getEmails().size() > 0) {
            infoContainer.addView(createLabelCard("Emails:"), params);
            for (int i = 0; i < contact.getEmails().size(); i++) {
                CardView card = initEmailCard(contact.getEmails().get(i));
                card.setUseCompatPadding(true);
                card.setLayoutParams(params);
                card.setRadius(9);
                card.setContentPadding(15, 15, 15, 15);
                card.setBackgroundColor(Color.WHITE);
                card.setMaxCardElevation(15);
                card.setCardElevation(9);

                infoContainer.addView(card, params);
            }
        }

        setContactPhoto();
        this.getSupportActionBar().
                setDisplayHomeAsUpEnabled(true);


    }

    private CardView initEmailCard(String email) {
        LinearLayout cardLinear = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLinear.setOrientation(LinearLayout.HORIZONTAL);

        CardView card = new CardView(getApplicationContext());

        //card.setCardBackgroundColor(Color.parseColor("#E6E6E6"));

        final TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setText(email);
        tv.setTextColor(Color.BLUE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        cardLinear.addView(tv);

        //card.addView(tv,0);

        Button sendEmailButton = new Button(getApplicationContext());
        sendEmailButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sendEmailButton.setText("Send Email");
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", tv.getText().toString(), null)));
            }
        });
        cardLinear.addView(sendEmailButton);

        card.addView(cardLinear, paramsLinear);
        return card;
    }

    private InputStream openPhoto(long contactId) {
        ContentResolver cr = getContentResolver();
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId));
        InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(cr, contactUri, true);
        return photo_stream;
    }

    private void setContactPhoto() {
        Bitmap photo = null;

        try {
            InputStream inputStream = openPhoto(Long.parseLong(contact.getId()));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                ImageView imageView = (ImageView) findViewById(R.id.contact_photo);
                imageView.setImageBitmap(photo);
                assert inputStream != null;
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private CardView initPhoneCard(final String phone) {
        LinearLayout cardLinear = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams paramsLinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLinear.setOrientation(LinearLayout.HORIZONTAL);

        CardView card = new CardView(getApplicationContext());

        //card.setCardBackgroundColor(Color.parseColor("#E6E6E6"));

        final TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setText(phone);
        tv.setTextColor(Color.BLUE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

        cardLinear.addView(tv);

        //card.addView(tv,0);

        Button callButton = new Button(getApplicationContext());
        callButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        callButton.setText("Call");
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", tv.getText().toString(), null)));
            }
        });
        cardLinear.addView(callButton);

        Button sendSmsButton = new Button(getApplicationContext());
        sendSmsButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sendSmsButton.setText("Send");
        sendSmsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", tv.getText().toString(), null)));
            }
        });
        cardLinear.addView(sendSmsButton);

        card.addView(cardLinear, paramsLinear);
        return card;
    }

    private CardView createLabelCard(String text) {
        CardView card = new CardView(getApplicationContext());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        card.setLayoutParams(params);
        card.setRadius(9);
        card.setContentPadding(15, 15, 15, 15);
//        card.setBackgroundColor(Color.BLUE);
        card.setMaxCardElevation(15);
        card.setCardElevation(9);

        TextView tv = new TextView(this);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        card.addView(tv);
        return card;
    }
}

