package com.valdiviezomazautp.inscribelifes.Detalle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valdiviezomazautp.inscribelifes.R;

import java.util.HashMap;

public class Detalle_Nota extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Button Boton_Importante;

    TextView Id_nota_Detalle, Uid_usuario_Detalle, Correo_usuario_Detalle, Titulo_Detalle, Descripcion_Detalle,
            Fecha_Registro_Detalle, Fecha_Nota_Detalle, Estado_Detalle;

    //DECLARAR LOS STRING PARA ALMACENAR LOS DATOS RECUPERADOS DE ACTIVIDAD ANTERIOR
    String id_nota_R , uid_usuario_R , correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    boolean ComprobarNotaImportante = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_nota);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detalle de nota");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        InicializarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();
        VerificarNotaImportante();

        Boton_Importante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ComprobarNotaImportante){
                    Eliminar_Nota_Importante();
                }else {
                    Agregar_Notas_Importantes();
                }
            }
        });
    }

    private void InicializarVistas(){
        Id_nota_Detalle = findViewById(R.id.Id_nota_Detalle);
        Uid_usuario_Detalle = findViewById(R.id.Uid_usuario_Detalle);
        Correo_usuario_Detalle = findViewById(R.id.Correo_usuario_Detalle);
        Titulo_Detalle = findViewById(R.id.Titulo_Detalle);
        Descripcion_Detalle = findViewById(R.id.Descripcion_Detalle);
        Fecha_Registro_Detalle = findViewById(R.id.Fecha_Registro_Detalle);
        Fecha_Nota_Detalle = findViewById(R.id.Fecha_Nota_Detalle);
        Estado_Detalle = findViewById(R.id.Estado_Detalle);
        Boton_Importante = findViewById(R.id.Boton_Importante);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    private void RecuperarDatos(){
        Bundle intent = getIntent().getExtras();

        id_nota_R = intent.getString("id_nota");
        uid_usuario_R = intent.getString("uid_usuario");
        correo_usuario_R = intent.getString("correo_usuario");
        fecha_registro_R = intent.getString("fecha_registro");
        titulo_R = intent.getString("titulo");
        descripcion_R = intent.getString("descripcion");
        fecha_R = intent.getString("fecha_nota");
        estado_R = intent.getString("estado");

    }

    private void SetearDatosRecuperados(){
        Id_nota_Detalle.setText(id_nota_R);
        Uid_usuario_Detalle.setText(uid_usuario_R);
        Correo_usuario_Detalle.setText(correo_usuario_R);
        Fecha_Registro_Detalle.setText(fecha_registro_R);
        Titulo_Detalle.setText(titulo_R);
        Descripcion_Detalle.setText(descripcion_R);
        Fecha_Nota_Detalle.setText(fecha_R);
        Estado_Detalle.setText(estado_R);
    }

    private void Agregar_Notas_Importantes(){
        if (user == null){
            //Si el usuario es nulo
            Toast.makeText(Detalle_Nota.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else {
            //Obtenemos los datos de la nota de la actividad anterior
            Bundle intent = getIntent().getExtras();

            id_nota_R = intent.getString("id_nota");
            uid_usuario_R = intent.getString("uid_usuario");
            correo_usuario_R = intent.getString("correo_usuario");
            fecha_registro_R = intent.getString("fecha_registro");
            titulo_R = intent.getString("titulo");
            descripcion_R = intent.getString("descripcion");
            fecha_R = intent.getString("fecha_nota");
            estado_R = intent.getString("estado");



            HashMap<String , String> Nota_Importante = new HashMap<>();
            Nota_Importante.put("id_nota", id_nota_R);
            Nota_Importante.put("uid_usuario", uid_usuario_R);
            Nota_Importante.put("correo_usuario", correo_usuario_R);
            Nota_Importante.put("fecha_hora_actual", fecha_registro_R);
            Nota_Importante.put("titulo", titulo_R);
            Nota_Importante.put("descripcion", descripcion_R);
            Nota_Importante.put("fecha_nota", fecha_R);
            Nota_Importante.put("estado", estado_R);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis notas importantes").child(id_nota_R)
                    .setValue(Nota_Importante)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detalle_Nota.this, "Se ha añadido a notas importantes", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detalle_Nota.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void Eliminar_Nota_Importante(){
        if (user == null){
            Toast.makeText(Detalle_Nota.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else {
            Bundle intent = getIntent().getExtras();
            id_nota_R = intent.getString("id_nota");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis notas importantes").child(id_nota_R)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Detalle_Nota.this, "La nota ya no es importante", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Detalle_Nota.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    private void VerificarNotaImportante(){
        if (user == null){
            Toast.makeText(Detalle_Nota.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }else {
            Bundle intent = getIntent().getExtras();
            id_nota_R = intent.getString("id_nota");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis notas importantes").child(id_nota_R)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ComprobarNotaImportante = snapshot.exists();
                            if (ComprobarNotaImportante){
                                String importante = "Importante";
                                Boton_Importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_nota_importante, 0 , 0);
                                Boton_Importante.setText(importante);
                            }else {
                                String no_importante = "No importante";
                                Boton_Importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_nota_no_importante, 0 , 0);
                                Boton_Importante.setText(no_importante);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}