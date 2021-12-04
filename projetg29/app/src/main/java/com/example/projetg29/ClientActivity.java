package com.example.projetg29;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class ClientActivity extends AppCompatActivity {
    Client compte;

    EditText editText;
    Button btn;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Object StorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        editText = findViewById(R.id.editTextClient);
        btn = findViewById(R.id.button8);

        StorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");
        btn.setEnabled(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        @Override
        protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData()!=null){{
                btn.setEnabled(true);
                editText.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/")+1));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadPDFfileFirebase(data.getData());
                    }
                });
            }
            }


        }



        // Définir les éléments du layout
        TextView textView = findViewById(R.id.textView5);

        // Récuperer les informations de l'activité précédente
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("user");
        String password = extras.getString("password");

        compte = new Client(username, password);




        // Mettre à jour le texte
        textView.setText("Bienvenue "+compte.getUsername()+", vous êtes connecté en tant que Client");

    }

    private void uploadPDFfileFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();
        storageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".pdf");
        reference.File(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(uploadTask.TaskSnapshot taskSnapshot){

                Task<Uri> uriTask = taskSnapshot.getStrorage().getDownloadUri();
                while (!uriTask.isComplete());
                Uri uri = uriTask.getResult();

                putPDF putPDF = new putPDF(editText.getText().toString(),uri.toString());
                databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                Toast.makeText(ClientActivity.this, "file upload",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                )
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot){

                double progress = (100.0 * snapshot.getBytesTrandferres())/getTotalByteCount();
                progressDialog.setCancelMessage("File Upload..." + (int) progress+"%");


            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("applocation/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);
    }

}
