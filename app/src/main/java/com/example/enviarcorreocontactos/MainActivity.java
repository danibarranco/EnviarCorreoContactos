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

    private void searchContact() {
        String name = null,phone= null,email= null;
        Contacto contacto;

        // Voy rellenando los datos de la query
        String[] proyeccion = new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Email.ADDRESS};


        String seleccion = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like '%" + nom +"%' or "+ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%" + num +"%'";

        Cursor micursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                proyeccion, seleccion, null, null);

        if (micursor != null){
            int cont=0;
            while (micursor.moveToNext()){

                name = micursor.getString(0);
                phone=micursor.getString(1);
                email = micursor.getString(2);
                contacto = new Contacto(name, phone,email) ;
                contactos.add(contacto);

            }
            System.out.println("hola"+contactos.toString());
            lanzarActivity(contactos);
        } else {
            System.out.println("hola");
            Toast.makeText(this,"El cursor está vacío", Toast.LENGTH_LONG);
        }
    }

    private void lanzarActivity(ArrayList<Contacto> contactos) {
        startActivity(new Intent(MainActivity.this,Contactos.class).putParcelableArrayListExtra("contactos",contactos));
    }
}
