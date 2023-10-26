package com.valdiviezomazautp.inscribelifes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText NombreEt, CorreoEt, ContraseñaEt, ConfirmarContraseñaEt;
    Button RegistrarUsuario;
    TextView TengounacuentaTXT;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombre = " ", correo = " ", password = " ", confirmarpassword = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //ActionBar actionBar = getActionBar();
        //actionBar.setTitle("Registrar");
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        NombreEt = findViewById(R.id.NombreEt);
        CorreoEt = findViewById(R.id.CorreoEt);
        ContraseñaEt = findViewById(R.id.ContraseñaEt);
        ConfirmarContraseñaEt = findViewById(R.id.ConfirmarContraseñaEt);
        RegistrarUsuario = findViewById(R.id.RegistrarUsuario);
        TengounacuentaTXT = findViewById(R.id.TengounacuentaTXT);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                ValidarDatos();
            }
        });

        TengounacuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                startActivity(new Intent(Registro.this, Login.class));
            }
        });

    }

    private void ValidarDatos() {
        nombre = NombreEt.getText().toString();
        correo = CorreoEt.getText().toString();
        password = ContraseñaEt.getText().toString();
        confirmarpassword = ConfirmarContraseñaEt.getText().toString();

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Por favor ingrese su nombre", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Por favor ingrese su contraseña", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmarpassword)) {
            Toast.makeText(this, "Por favor confirme su contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmarpassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else {
            CrearCuenta();
        }

    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando cuenta...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                      GuardarInformacion();

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando información...");
        progressDialog.dismiss();

        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombres", nombre);
        Datos.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                     progressDialog.dismiss();
                     Toast.makeText(Registro.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(Registro.this, MenuPrincipal.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                       public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}