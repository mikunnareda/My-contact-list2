package com.example.mycontactlist;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<Contact> contactData;
    private View.OnClickListener mOnItemClickListener;
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textContactName;
        public TextView textPhone;
        public TextView textEmail;
        public Button deleteButton;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textContactName = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            textEmail = itemView.findViewById(R.id.textEmail);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getContactTextView() {
            return textContactName;
        }
        public TextView getPhoneTextView() {
            return textPhone;
        }
        public TextView getEmailTextView(){return textEmail;}

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public ContactAdapter(ArrayList<Contact> arrayList) {
        contactData = arrayList;
    }
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false); //change simple item view to list item
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getEmailTextView().setText(contactData.get(position).getEMail());
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }
}
