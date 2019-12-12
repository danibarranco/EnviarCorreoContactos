package com.example.enviarcorreocontactos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.enviarcorreocontactos.model.data.Contacto;
import com.example.enviarcorreocontactos.model.view.ContactoAdapter;

import java.util.ArrayList;

public class Contactos extends AppCompatActivity {
    private ArrayList<Contacto> contactos = new ArrayList<>();
    private RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        contactos=getIntent().getParcelableArrayListExtra("contactos");
        System.out.println(contactos.toString());
        init();
    }

    private void init() {
        ContactoAdapter adapter= new ContactoAdapter(new ContactoAdapter.OnItemClickListenner() {
            @Override
            public void onItemClick(Contacto contacto, View v) {
                mandarEmail(contacto);
            }
        },this);
        rvList = findViewById(R.id.rvList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
        adapter.setContactoList(contactos);
    }

    public void mandarEmail(Contacto contacto){
        String TO[] = {contacto.getEmail()};
        String CC[] = {""};


        String mensaje = "Te envio un mensaje";

        Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
        mailIntent.setData(Uri.parse("mailto:"));

        mailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        mailIntent.putExtra(Intent.EXTRA_CC, CC);

        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Practica mensaje");
        mailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

        String title = "Mandar este email con....";

        Intent chooser = Intent.createChooser(mailIntent, title);
        if(mailIntent.resolveActivity(getPackageManager()) != null){
            startActivity(chooser);
        }
        finish();
    }


}
