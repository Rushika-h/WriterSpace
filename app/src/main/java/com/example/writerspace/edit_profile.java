package com.example.writerspace;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.writerspace.Utils.BitmapUtils;
import com.example.writerspace.model.user;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class edit_profile extends AppCompatActivity {

    ImageView image_profile,close;
    TextView save,change_img;
    EditText fullname,username,bio;

    private static int RESULT_LOAD_IMAGE = 1;

    FirebaseUser firebaseUser;
    Bitmap bitmapimg;

    PhotoEditorView image;
    PhotoEditor photoEditor;

    private Uri imageUri;
    private StorageTask uploadTask;
    StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);
            bitmapimg=bitmap.copy(Bitmap.Config.ARGB_8888,true);

            imageUri=data.getData();
            image_profile.setImageURI(imageUri);
            bitmap.recycle();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        image_profile=findViewById(R.id.image_edit_profile);
        save=findViewById(R.id.save_edit_profile);
        change_img=findViewById(R.id.change_photo);
        fullname=findViewById(R.id.edit_fullname);
        username=findViewById(R.id.edit_username);
        bio=findViewById(R.id.edit_bio);
        close=findViewById(R.id.close);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user=snapshot.getValue(user.class);
                fullname.setText(user.getFullname());
                username.setText(user.getUname());
                bio.setText(user.getBio());
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.writerspace.fragment.feed.class));
            }
        });
        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                updateProfile(fullname.getText().toString(),username.getText().toString(),bio.getText().toString());
                Toast.makeText(getApplicationContext(),"Saved!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), com.example.writerspace.fragment.feed.class));

            }

        });
    }

    private void updateProfile(String fullname, String username, String bio) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("fullname",fullname);
        hashMap.put("username",username);
        hashMap.put("bio",bio);
        reference.updateChildren(hashMap);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage(){
        if(imageUri!=null){
            StorageReference storageReference1=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask=storageReference1.putFile(imageUri);
            System.out.println("fileref"+storageReference1);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference1.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri= (Uri) task.getResult();
                        String myUrl=downloadUri.toString();
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageurl",""+myUrl);

                        Toast.makeText(getApplicationContext(),"Reached here",Toast.LENGTH_SHORT).show();
                        reference.updateChildren(hashMap);
                    }else
                    {
                        Toast.makeText(edit_profile.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(edit_profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(this,"No image selected",Toast.LENGTH_SHORT).show();
        }
    }

}
