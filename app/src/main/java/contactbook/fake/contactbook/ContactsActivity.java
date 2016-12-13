package contactbook.fake.contactbook;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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
        progress.setTitle("Грузим контакты");
        progress.setMessage("Активно грузим");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recycler.setLayoutManager(llm);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        getLoaderManager().initLoader(1, null, this).forceLoad();
        //ContactReader reader = new ContactReader(getContentResolver());
        //contacts = reader.getContactsInfo();
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
        // меняем стиль на индикатор
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
        this.contacts = contacts;
        recyclerViewAdapter.addItems(this.contacts);
        recyclerViewAdapter.notifyDataSetChanged();
        recycler.setAdapter(recyclerViewAdapter);
        progress.dismiss();

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Contact>> loader) {
        loader=null;
    }
}
