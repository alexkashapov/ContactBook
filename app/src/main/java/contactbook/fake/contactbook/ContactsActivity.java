package contactbook.fake.contactbook;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity implements RecyclerViewAdapter.OnRecyclerCallback, LoaderManager.LoaderCallbacks<ArrayList<Contact>> {
    private RecyclerView recycler;
    private RecyclerViewAdapter recyclerViewAdapter;
    ProgressDialog progress;
    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_contacts);

        progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.pd_title));
        progress.setMessage(getString(R.string.pd_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recycler.setLayoutManager(llm);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recycler.setAdapter(recyclerViewAdapter);
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().getLoader(1).forceLoad();
    }

    @Override
    public void onItemClick(int position) {
        Contact contact = recyclerViewAdapter.getContact(position);
        Intent intent = new Intent(this, ContactInfoActivity.class);
        intent.putExtra(Contact.class.getCanonicalName(), contact);
        startActivity(intent);
    }

    @Override
    public Loader<ArrayList<Contact>> onCreateLoader(int i, Bundle bundle) {
        progress.show();
        Loader<ArrayList<Contact>> loader = null;
        loader = new AsyncTaskLoader<ArrayList<Contact>>(this) {
            @Override
            public ArrayList<Contact> loadInBackground() {
                ContactReader reader = new ContactReader(getContentResolver());
                return reader.getContactsInfo();
            }
        };
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Contact>> loader, ArrayList<Contact> contacts) {
        progress.dismiss();
        this.contacts = contacts;
        recyclerViewAdapter.addItems(this.contacts);
        recyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Contact>> loader) {
        loader = null;

    }
}
