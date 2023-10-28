package com.valdiviezomazautp.inscribelifes.Perfil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.valdiviezomazautp.inscribelifes.ActualizarPass.ActulizarPassUsuario;
import com.valdiviezomazautp.inscribelifes.MenuPrincipal;
import com.valdiviezomazautp.inscribelifes.R;

import java.util.Calendar;
import java.util.HashMap;

public class Perfil_Usuario extends AppCompatActivity {

    ImageView Imagen_Perfil;
    TextView Correo_Perfil, Uid_Perfil, Telefono_Perfil, Fecha_Nacimiento_Perfil;
    EditText Nombres_Perfil, Apellidos_Perfil, Edad_Perfil,
            Domicilio_Perfil, Universidad_Perfil,
            Profesion_Perfil;

    ImageView Editar_Telefono, Editar_fecha, Editar_imagen;

    Button Guardar_Datos;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    Dialog dialog_establecer_telefono;

    int dia, mes , anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Perfil de usuario");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InicializarVariables();

        Editar_Telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Establecer_telefono_usuario();
            }
        });

        Editar_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Abrir_Calendario();
            }
        });

        Guardar_Datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarDatos();
            }
        });

        Editar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Perfil_Usuario.this, Editar_imagen_perfil.class));
            }
        });

    }

    private void InicializarVariables(){
        Imagen_Perfil = findViewById(R.id.Imagen_Perfil);
        Correo_Perfil = findViewById(R.id.Correo_Perfil);
        Uid_Perfil = findViewById(R.id.Uid_Perfil);
        Nombres_Perfil = findViewById(R.id.Nombres_Perfil);
        Apellidos_Perfil = findViewById(R.id.Apellidos_Perfil);
        Edad_Perfil = findViewById(R.id.Edad_Perfil);
        Telefono_Perfil = findViewById(R.id.Telefono_Perfil);
        Domicilio_Perfil = findViewById(R.id.Domicilio_Perfil);
        Universidad_Perfil = findViewById(R.id.Universidad_Perfil);
        Profesion_Perfil = findViewById(R.id.Profesion_Perfil);
        Fecha_Nacimiento_Perfil = findViewById(R.id.Fecha_Nacimiento_Perfil);

        Editar_Telefono = findViewById(R.id.Editar_Telefono);
        Editar_fecha = findViewById(R.id.Editar_fecha);
        Editar_imagen = findViewById(R.id.Editar_imagen);

        dialog_establecer_telefono = new Dialog(Perfil_Usuario.this);

        Guardar_Datos = findViewById(R.id.Guardar_Datos);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    private void LecturaDeDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //Obtener sus datos
                    String uid = ""+snapshot.child("uid").getValue();
                    String nombre = ""+snapshot.child("nombres").getValue();
                    String apellidos = ""+snapshot.child("apellidos").getValue();
                    String correo = ""+snapshot.child("correo").getValue();
                    String edad = ""+snapshot.child("edad").getValue();
                    String telefono = ""+snapshot.child("telefono").getValue();
                    String domicilio = ""+snapshot.child("domicilio").getValue();
                    String universidad = ""+snapshot.child("universidad").getValue();
                    String profesion = ""+snapshot.child("profesion").getValue();
                    String fecha_nacimiento = ""+snapshot.child("fecha_de_nacimiento").getValue();
                    String imagen_perfil = ""+snapshot.child("imagen_perfil").getValue();

                    //Seteo de datos
                    Uid_Perfil.setText(uid);
                    Nombres_Perfil.setText(nombre);
                    Apellidos_Perfil.setText(apellidos);
                    Correo_Perfil.setText(correo);
                    Edad_Perfil.setText(edad);
                    Telefono_Perfil.setText(telefono);
                    Domicilio_Perfil.setText(domicilio);
                    Universidad_Perfil.setText(universidad);
                    Profesion_Perfil.setText(profesion);
                    Fecha_Nacimiento_Perfil.setText(fecha_nacimiento);

                    Cargar_Imagen(imagen_perfil);

                }
                else {
                    Toast.makeText(Perfil_Usuario.this, "Esperando datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil_Usuario.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Cargar_Imagen(String imagen_perfil) {
        try {
            /*Cuando la imagen ha sido traida exitosamente desde Firebase*/
            Glide.with(getApplicationContext()).load(imagen_perfil).placeholder(R.drawable.ic_lion).into(Imagen_Perfil);

        }catch (Exception e){
            /*Si la imagen no fue traida con éxito*/
            Glide.with(getApplicationContext()).load(R.drawable.ic_lion).into(Imagen_Perfil);

        }
    }

    private void Establecer_telefono_usuario(){
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
                    Telefono_Perfil.setText(codigo_pais_telefono);
                    dialog_establecer_telefono.dismiss();
                }else {
                    Toast.makeText(Perfil_Usuario.this, "Ingrese un número telefónico", Toast.LENGTH_SHORT).show();
                    dialog_establecer_telefono.dismiss();
                }
            }
        });

        dialog_establecer_telefono.show();
        dialog_establecer_telefono.setCanceledOnTouchOutside(true);
    }

    private void Abrir_Calendario(){
        final Calendar calendario = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anio = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Perfil_Usuario.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int AnioSeleccionado, int MesSeleccionado, int DiaSeleccionado) {

                String diaFormateado, mesFormateado;

                //OBTENER DIA
                if (DiaSeleccionado < 10){
                    diaFormateado = "0"+String.valueOf(DiaSeleccionado);
                    // Antes: 9/11/2022 -  Ahora 09/11/2022
                }else {
                    diaFormateado = String.valueOf(DiaSeleccionado);
                    //Ejemplo 13/08/2022
                }

                //OBTENER EL MES
                int Mes = MesSeleccionado + 1;

                if (Mes < 10){
                    mesFormateado = "0"+String.valueOf(Mes);
                    // Antes: 09/8/2022 -  Ahora 09/08/2022
                }else {
                    mesFormateado = String.valueOf(Mes);
                    //Ejemplo 13/10/2022 - 13/11/2022 - 13/12/2022

                }

                //Setear fecha en TextView
                Fecha_Nacimiento_Perfil.setText(diaFormateado + "/" + mesFormateado + "/"+ AnioSeleccionado);

            }
        }
                ,anio,mes,dia);
        datePickerDialog.show();
    }

    private void ActualizarDatos(){

        String A_Nombre = Nombres_Perfil.getText().toString().trim();
        String A_Apellidos = Apellidos_Perfil.getText().toString().trim();
        String A_Edad = Edad_Perfil.getText().toString().trim();
        String A_Telefono = Telefono_Perfil.getText().toString().trim();
        String A_Domicilio = Domicilio_Perfil.getText().toString();
        String A_Universidad = Universidad_Perfil.getText().toString();
        String A_Profesion = Profesion_Perfil.getText().toString();
        String A_Fecha_N = Fecha_Nacimiento_Perfil.getText().toString();

        HashMap<String, Object> Datos_Actualizar = new HashMap<>();

        Datos_Actualizar.put("nombres", A_Nombre);
        Datos_Actualizar.put("apellidos", A_Apellidos);
        Datos_Actualizar.put("edad", A_Edad);
        Datos_Actualizar.put("telefono", A_Telefono);
        Datos_Actualizar.put("domicilio", A_Domicilio);
        Datos_Actualizar.put("universidad", A_Universidad);
        Datos_Actualizar.put("profesion", A_Profesion);
        Datos_Actualizar.put("fecha_de_nacimiento", A_Fecha_N);

        Usuarios.child(user.getUid()).updateChildren(Datos_Actualizar)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Perfil_Usuario.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Perfil_Usuario.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actualizar_pass, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Actualizar_pass){
            startActivity(new Intent(Perfil_Usuario.this, ActulizarPassUsuario.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void ComprobarInicioSesion(){
        if (user!=null){
            LecturaDeDatos();
        }else {
            startActivity(new Intent(Perfil_Usuario.this, MenuPrincipal.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}