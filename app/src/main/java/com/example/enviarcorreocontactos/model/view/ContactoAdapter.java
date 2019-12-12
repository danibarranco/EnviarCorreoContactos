package com.example.enviarcorreocontactos.model.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.enviarcorreocontactos.R;
import com.example.enviarcorreocontactos.model.data.Contacto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.MyViewHolder>{
    private LayoutInflater inflaterC;
    private OnItemClickListenner listener;
    private List<Contacto> contactos;
    private Context context;


    public interface OnItemClickListenner{
        void onItemClick(Contacto contacto, View v);
    }

    public ContactoAdapter(OnItemClickListenner listener, Context context) {
        this.listener=listener;
        inflaterC=LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ContactoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflaterC.inflate(R.layout.item_contacto,parent,false);
        ContactoAdapter.MyViewHolder vh = new ContactoAdapter.MyViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoAdapter.MyViewHolder holder, int position) {
        if(contactos != null){
            final Contacto current = contactos.get(position);
            holder.tvNom.setText(current.getName());
            holder.tvTelef.setText(current.getNumber());
            if(current.getEmail()!=null){
                holder.tvEmail.setText(current.getEmail());
                holder.cl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(current ,v);
                    }
                });
            }else{
                holder.tvEmail.setText("No tiene correo");
            }
        }

    }
    public void setContactoList(List<Contacto>contactoList){
        this.contactos=contactoList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int elementos=0;
        if(contactos!=null){
            elementos=contactos.size();
        }
        return elementos;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvTelef,tvEmail;
        ConstraintLayout cl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvTelef = itemView.findViewById(R.id.tvTelef);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            cl=itemView.findViewById(R.id.cl);

        }
    }
}
