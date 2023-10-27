package com.valdiviezomazautp.inscribelifes.Perfil;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valdiviezomazautp.inscribelifes.R;

import java.util.HashMap;

public class Editar_imagen_perfil extends AppCompatActivity {

    ImageView ImagenPerfilActualizar;
    Button BtnElegirImagenDe, BtnActualizarImagen;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog_elegir_imagen;

    Uri imagenUri = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_imagen_perfil);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Seleccionar imagen");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImagenPerfilActualizar = findViewById(R.id.ImagenPerfilActualizar);
        BtnElegirImagenDe = findViewById(R.id.BtnElegirImagenDe);
        BtnActualizarImagen = findViewById(R.id.BtnActualizarImagen);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog_elegir_imagen = new Dialog(Editar_imagen_perfil.this);

        BtnElegirImagenDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Editar_imagen_perfil.this, "Elegir imagen de", Toast.LENGTH_SHORT).show();
                ElegirImagenDe();
            }
        });

        BtnActualizarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagenUri == null){
                    Toast.makeText(Editar_imagen_perfil.this, "Inserte una nueva imagen", Toast.LENGTH_SHORT).show();
                }else {
                    subirImagenStorage();
                }
            }
        });

        progressDialog = new ProgressDialog(Editar_imagen_perfil.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        LecturaDeImagen();

    }


    private void LecturaDeImagen(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Obtener el dato imagen
                String imagen_perfil = ""+snapshot.child("imagen_perfil").getValue();

                Glide.with(getApplicationContext())
                        .load(imagen_perfil)
                        .placeholder(R.drawable.user_lion)
                        .into(ImagenPerfilActualizar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void subirImagenStorage(){
        progressDialog.setMessage("Subiendo imagen");
        progressDialog.show();
        String carpetaImagenes = "ImagenesPerfil/";
        String NombreImagen = carpetaImagenes+firebaseAuth.getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference(NombreImagen);
        reference.putFile(imagenUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uriImagen = ""+uriTask.getResult();
                        ActualizarImagenBD(uriImagen);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Editar_imagen_perfil.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void ActualizarImagenBD(String uriImagen) {
        progressDialog.setMessage("Actualizando la imagen");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imagenUri != null){
            hashMap.put("imagen_perfil", ""+uriImagen);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(user.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Editar_imagen_perfil.this, "Imagen se ha actualizado con éxito", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Editar_imagen_perfil.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ElegirImagenDe() {

        Button Btn_Elegir_Galeria, Btn_Elegir_Camara;

        dialog_elegir_imagen.setContentView(R.layout.cuadro_dialogo_elegir_imagen);

        Btn_Elegir_Galeria = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Galeria);
        Btn_Elegir_Camara = dialog_elegir_imagen.findViewById(R.id.Btn_Elegir_Camara);

        Btn_Elegir_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Editar_imagen_perfil.this, "Elegir de galería", Toast.LENGTH_SHORT).show();
                //SeleccionarImagenGaleria();
                if (ContextCompat.checkSelfPermission(Editar_imagen_perfil.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    SeleccionarImagenGaleria();
                    dialog_elegir_imagen.dismiss();
                }else {
                    SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    dialog_elegir_imagen.dismiss();
                }

            }
        });

        Btn_Elegir_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Editar_imagen_perfil.this, "Elegir de cámara", Toast.LENGTH_SHORT).show();
                //SeleccionarImagenCamara();
                //dialog_elegir_imagen.dismiss();
                if (ContextCompat.checkSelfPermission(Editar_imagen_perfil.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    SeleccionarImagenCamara();
                    dialog_elegir_imagen.dismiss();
                }else {
                    SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                    dialog_elegir_imagen.dismiss();
                }
            }
        });

        dialog_elegir_imagen.show();
        dialog_elegir_imagen.setCanceledOnTouchOutside(true);

    }

    private void SeleccionarImagenGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> galeriaActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        //Obtener uri de la imagen
                        Intent data = result.getData();
                        imagenUri = data.getData();
                        //Setear la imagen seleccionada en el imageView
                        ImagenPerfilActualizar.setImageURI(imagenUri);

                    }else{
                        Toast.makeText(Editar_imagen_perfil.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    /*PERMISO PARA ACCEDER A LA GALERIA*/
    private ActivityResultLauncher<String> SolicitudPermisoGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted){
                    SeleccionarImagenGaleria();
                }else{
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    private void SeleccionarImagenCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de imagen");
        imagenUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri);
        camaraActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> camaraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        ImagenPerfilActualizar.setImageURI(imagenUri);
                    }
                    else {
                        Toast.makeText(Editar_imagen_perfil.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    /*PERMISO PARA ACCEDER A LA CAMARA*/
    private ActivityResultLauncher<String> SolicitudPermisoCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    SeleccionarImagenCamara();
                }else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}