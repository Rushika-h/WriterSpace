package com.example.writerspace;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.writerspace.fragment.feed;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class proceedimage extends AppCompatActivity {

    ImageView imageView,back;
    Uri imageUri;
    String myUrl="";
    StorageTask uploadTask;
    StorageReference storageReference;

    Button post;
    EditText description,imagetitle;
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
        else {
            Toast.makeText(this,"Something's wrong",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(proceedimage.this,login.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceedimage);
        String uri=getIntent().getStringExtra("path");
        System.out.println("uripath"+uri);

/*
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
*/
        imageView=findViewById(R.id.imgv);
        imageView.setImageURI(Uri.parse(uri));
        //imageView.setImageBitmap(bmp);
        description=findViewById(R.id.descriptionimg);
        imagetitle=findViewById(R.id.imagetitle);
        post=findViewById(R.id.postimg);
        back=findViewById(R.id.back_photo);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(proceedimage.this, image.class));

            }
        });

        storageReference= FirebaseStorage.getInstance().getReference("Images");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri= Uri.parse(uri);
                if(imageUri!=null)
                {
                    StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
                    uploadTask=filereference.putFile(imageUri);
                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filereference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){

                                Uri downloadUri=task.getResult();
                                myUrl=downloadUri.toString();

                                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts").child("Images");
                                String descp=description.getText().toString();
                                String imgtit=imagetitle.getText().toString();
                                String postid=reference.push().getKey();
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("postid",postid);
                                hashMap.put("postimage",myUrl);
                                hashMap.put("title",imgtit);
                                hashMap.put("description",descp);
                                hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.child(postid).setValue(hashMap);

                                DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference().child("Activities");
                                String activityid=databaseRef.push().getKey();
                                HashMap<String, Object> hashMap1=new HashMap<>();
                                hashMap1.put("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                hashMap1.put("activityid",activityid);
                                hashMap1.put("activitytype","Image posted");
                                hashMap1.put("postid",postid);
                                hashMap1.put("timestamp", ServerValue.TIMESTAMP);
                                databaseRef.child(activityid).setValue(hashMap1);

                                //progressDialog.dismiss();
                                Toast.makeText(proceedimage.this,"Success..",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(proceedimage.this, feed.class));
                            }else {
                                Toast.makeText(proceedimage.this,"Failure..",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(proceedimage.this,"Failure.."+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else {
                    Toast.makeText(proceedimage.this,"No Image..",Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(proceedimage.this,"Success..",Toast.LENGTH_SHORT).show();

            }
        });
    }
    private String getFileExtension(Uri imageUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }



}
