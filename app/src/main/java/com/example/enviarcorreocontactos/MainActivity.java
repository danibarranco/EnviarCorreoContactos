package com.example.enviarcorreocontactos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.enviarcorreocontactos.model.data.Contacto;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    protected final int SOLICITUD_PERMISO_CONTACTOS=0;
    private static final String A1 ="ABC" ;
    private ArrayList<Contacto> contactos = new ArrayList<>();
    private EditText etNom,etNum;
    private String nom,num;
    private Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        etNom=findViewById(R.id.etNom);
        etNum=findViewById(R.id.etNum);
        search=findViewById(R.id.button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etNom.getText().toString().equals("")&&!etNum.getText().toString().equals("")){
                    nom=etNom.getText().toString();
                    num=etNum.getText().toString();
                    solicitarPermisos();
                }else{
                    Toast.makeText(view.getContext(),"No se admiten campos vacios",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void solicitarPermisos() {
        int tengo_permiso = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        // Here, thisActivity is the current activity
        if (tengo_permiso!= PackageManager.PERMISSION_GRANTED  ) {

            // ¿Enseñar explicación?
            Boolean deboexplicar = ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS);
            if (deboexplicar) {
                explain(R.string.tituloExpl, R.string.mensajeExpl,Manifest.permission.READ_CONTACTS);
            } else {
                // Solicito el permiso al usuario
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        SOLICITUD_PERMISO_CONTACTOS);
            }
        } else {
            searchContact();
        }
    }
    private void explain(int title, int message, final String permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.respSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permissions}, SOLICITUD_PERMISO_CONTACTOS);
            }
        });
        builder.setNegativeButton(R.string.respNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SOLICITUD_PERMISO_CONTACTOS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permiso concedido
                    searchContact();

                } else {
                    Toast.makeText(this,"Permiso no concedido", Toast.LENGTH_LONG);
                    finish();
                }

                return;
            }
        }
    }

    private void searchContact(){
        String name = null,phone= null,email= null;
        Contacto contact = null;
        Boolean existe=false;

        // If you need item type / label, add Data.DATA2 & Data.DATA3 to the projection
        String[] projection = {ContactsContract.Data.CONTACT_ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1};
        // Add more types to the selection if needed, e.g. StructuredName
        String selection = ContactsContract.Data.MIMETYPE + " IN ('" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "', '" + ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE + "')";
        Cursor cur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, selection, null, null);

        // Loop through the data
        while (cur.moveToNext()) {

            long id = cur.getLong(0);
             name = cur.getString(1);
            System.out.println(name);
            String mime = cur.getString(2); // email / phone
            String data = cur.getString(3);
            for (Contacto c:contactos
                 ) {
                if(c.getId()==id){
                    existe=true;
                }
            }
            // get the Contact class from the HashMap, or create a new one and add it to the Hash
            if (existe) {
                for (Contacto c:contactos
                ) {
                    if(c.getId()==id){
                        contact=c;
                    }
                }
                existe=false;
            } else {
                contact = new Contacto(id,name,phone,email);
            }

            switch (mime) {
                case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                    contact.setNumber(data);
                    break;
                case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                    contact.setEmail(data);
                    break;
            }
            contactos.add(contact);
        }
        cur.close();
        System.out.println(contactos.toString());
        lanzarActivity(contactos);
    }
    

    private void lanzarActivity(ArrayList<Contacto> contactos) {
        startActivity(new Intent(MainActivity.this,Contactos.class).putParcelableArrayListExtra("contactos",contactos));
    }
}
