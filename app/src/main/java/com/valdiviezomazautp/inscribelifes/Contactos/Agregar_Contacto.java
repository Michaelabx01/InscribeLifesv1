package com.valdiviezomazautp.inscribelifes.Contactos;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;
import com.valdiviezomazautp.inscribelifes.Objetos.Contacto;
import com.valdiviezomazautp.inscribelifes.R;

public class Agregar_Contacto extends AppCompatActivity {

    TextView Uid_Usuario_C, Telefono_C;
    EditText Nombres_C, Apellidos_C, Correo_C, Edad_C, Direccion_C;
    ImageView Editar_Telefono_C;
    Button Btn_Guardar_Contacto;
    Dialog dialog_establecer_telefono;

    DatabaseReference BD_Usuarios;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Agregar contacto");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InicializarVariables();
        ObtenerUidUsuario();

        Editar_Telefono_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Establecer_telefono_contacto();
            }
        });

        Btn_Guardar_Contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarContacto();
            }
        });
    }

    private void InicializarVariables(){
        Uid_Usuario_C = findViewById(R.id.Uid_Usuario_C);
        Nombres_C = findViewById(R.id.Nombres_C);
        Apellidos_C = findViewById(R.id.Apellidos_C);
        Correo_C = findViewById(R.id.Correo_C);
        Telefono_C = findViewById(R.id.Telefono_C);
        Edad_C = findViewById(R.id.Edad_C);
        Direccion_C = findViewById(R.id.Direccion_C);
        Editar_Telefono_C = findViewById(R.id.Editar_Telefono_C);
        Btn_Guardar_Contacto = findViewById(R.id.Btn_Guardar_Contacto);

        dialog_establecer_telefono = new Dialog(Agregar_Contacto.this);
        BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog = new Dialog(Agregar_Contacto.this);
    }

    private void ObtenerUidUsuario(){
        String UidRecuperado = getIntent().getStringExtra("Uid");
        Uid_Usuario_C.setText(UidRecuperado);
    }

    private void Establecer_telefono_contacto(){
        CountryCodePicker ccp;
        EditText Establecer_Telefono;
        Button Btn_Aceptar_Telefono;

        dialog_establecer_telefono.setContentView(R.layout.cuadro_dialogo_establecer_telefono);

        ccp = dialog_establecer_telefono.findViewById(R.id.ccp);
        Establecer_Telefono = dialog_establecer_telefono.findViewById(R.id.Establecer_Telefono);
        Btn_Aceptar_Telefono = dialog_establecer_telefono.findViewById(R.id.Btn_Aceptar_Telefono);

        Btn_Aceptar_Telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo_pais = ccp.getSelectedCountryCodeWithPlus();
                String telefono = Establecer_Telefono.getText().toString();
                String codigo_pais_telefono = codigo_pais+telefono; //+51956605043

                if (!telefono.equals("")){
                    Telefono_C.setText(codigo_pais_telefono);
                    dialog_establecer_telefono.dismiss();
                }else {
                    Toast.makeText(Agregar_Contacto.this, "Ingrese un número telefónico", Toast.LENGTH_SHORT).show();
                    dialog_establecer_telefono.dismiss();
                }
            }
        });

        dialog_establecer_telefono.show();
        dialog_establecer_telefono.setCanceledOnTouchOutside(true);
    }

    private void AgregarContacto(){
        /*Obtener los datos*/
        String uid = Uid_Usuario_C.getText().toString();
        String nombres = Nombres_C.getText().toString();
        String apellidos = Apellidos_C.getText().toString();
        String correo = Correo_C.getText().toString();
        String telefono = Telefono_C.getText().toString();
        String edad = Edad_C.getText().toString();
        String direccion = Direccion_C.getText().toString();

        /*Creamos la cadena única*/
        String id_contacto = BD_Usuarios.push().getKey();

        /*Validar los datos*/
        if (!uid.equals("") && !nombres.equals("")){

            Contacto contacto = new Contacto(
                    id_contacto,
                    uid,
                    nombres,
                    apellidos,
                    correo,
                    telefono,
                    edad,
                    direccion,
                    "");

            /*Establecer el nombre de la bd*/
            String Nombre_BD = "Contactos";
            assert id_contacto != null;
            BD_Usuarios.child(user.getUid()).child(Nombre_BD).child(id_contacto).setValue(contacto);
            Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }
        else {
            //Toast.makeText(this, "Por favor complete al menos el nombre del contacto", Toast.LENGTH_SHORT).show();
            ValidarRegistroContacto();
        }

    }

    private void ValidarRegistroContacto(){

        Button Btn_Validar_Registro_C;

        dialog.setContentView(R.layout.cuadro_dialogo_validar_registro_c);

        Btn_Validar_Registro_C = dialog.findViewById(R.id.Btn_Validar_Registro_C);

        Btn_Validar_Registro_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}