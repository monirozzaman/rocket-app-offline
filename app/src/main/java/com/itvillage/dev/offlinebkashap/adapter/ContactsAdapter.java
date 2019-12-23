package com.itvillage.dev.offlinebkashap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itvillage.dev.offlinebkashap.R;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    private EditText numberGetEdit;

    public ContactsAdapter(Context context, ArrayList<Contact> contacts, EditText numberEditText) {
        super(context, 0, contacts);
        this.numberGetEdit = numberEditText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        final Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.single_contact_view, parent, false);
        }
        // Populate the data into the template view using the data object
        ImageView ivContactImage;
        LinearLayout linearLayout, layout1;
        TextView tvContactName;
        TextView tvPhoneNumber;
        // ivContactImage = (ImageView) view.findViewById(R.id.ivContactImage);
        linearLayout = view.findViewById(R.id.relativeLayout2);
        layout1 = view.findViewById(R.id.layout1);
        TextView tvName = (TextView) view.findViewById(R.id.tvContactName);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvPhoneNumber);
        tvName.setText(contact.name);

        tvPhone.setText("");
        if (contact.emails.size() > 0 && contact.emails.get(0) != null) {

        }
        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberGetEdit.setText(contact.numbers.get(0).number);
                }
            });
            layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberGetEdit.setText(contact.numbers.get(0).number);
                }
            });
            tvPhone.setText(contact.numbers.get(0).number);
        }
        return view;
    }

}
