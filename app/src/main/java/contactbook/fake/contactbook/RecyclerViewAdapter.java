package contactbook.fake.contactbook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fake on 13.12.2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<Contact> contacts;
    private OnRecyclerCallback onRecyclerCallback;

    public RecyclerViewAdapter(OnRecyclerCallback onRecyclerCallback) {
        contacts = new ArrayList<>();
        this.onRecyclerCallback = onRecyclerCallback;
    }

    public void addItems(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public Contact getContact(int position) {
        return new Contact((contacts.get(position)));
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_contact, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.bind(contacts.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecyclerCallback.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(Contact contact) {
            name.setText(contact.getName());
        }
    }

    interface OnRecyclerCallback {
        void onItemClick(int position);
    }
}

