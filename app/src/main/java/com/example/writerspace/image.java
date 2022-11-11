package com.example.writerspace;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.writerspace.Interface.colorfragListener;
import com.example.writerspace.Utils.BitmapUtils;
import com.example.writerspace.fragment.colorfrag;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class image extends AppCompatActivity implements colorfragListener {
    private static int RESULT_LOAD_IMAGE = 1;
    public static final String pictureName="backgroudn.jpg";
    Button bckimg, font,txtcolor,postimg;
    ImageView close;
    StorageReference storageReference;
    Uri imageUri;

    PhotoEditorView image;
    PhotoEditor photoEditor;

    Bitmap originalBitmap,finalBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        bckimg = findViewById(R.id.backg);
        image = findViewById(R.id.img);
        photoEditor=new PhotoEditor.Builder(this,image).setPinchTextScalable(true).build();
        txtcolor=findViewById(R.id.textcolor);
        postimg=findViewById(R.id.postimg);
        close=findViewById(R.id.close_photo);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), com.example.writerspace.fragment.feed.class));

            }
        });

        loadImage();

        storageReference = FirebaseStorage.getInstance().getReference("Images");

        bckimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });
        txtcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorfrag cf= colorfrag.getInstance();
                cf.setListener(com.example.writerspace.image.this);
                cf.show(getSupportFragmentManager(),cf.getTag());

            }
        });
        postimg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    photoEditor.saveAsBitmap(new OnSaveBitmap() {
                        @Override
                        public void onBitmapReady(Bitmap saveBitmap) {
                            try {

                                image.getSource().setImageBitmap(saveBitmap);
                                String path=MediaStore.Images.Media.insertImage(getContentResolver(), saveBitmap, "image" , "");
                                //System.out.println("savee"+s);
                                //String path = BitmapUtils.insertImage(getContentResolver(), saveBitmap, "title" + "_h.jpg");
                                System.out.println("Path"+path);

                                if(!TextUtils.isEmpty(path))
                                {
                                    Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_LONG).show();
                                }
                                else {

                                    Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG).show();
                                }

                                //System.out.println("UriPAth"+imageUri);


                                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                System.out.println("byteeeee"+byteArray);
                                */Intent intent=new Intent(com.example.writerspace.image.this,proceedimage.class);
                                //intent.putExtra("image",byteArray);
                                intent.putExtra("path",path);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }
            });
        }


    /*private void openImage(String path){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }
*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {

            final Uri uri = data.getData();
            useImage(uri);

/*
            Bitmap bitmap=BitmapUtils.getBitmapFromGallery(this,data.getData(),800,800);

            originalBitmap.recycle();
            originalBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);

            image.getSource().setImageBitmap(originalBitmap);
            bitmap.recycle();
*/


        }
}
    private void useImage(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            System.out.println("bitt"+bitmap.toString());
            originalBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            image.getSource().setImageBitmap(originalBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //use the bitmap as you like
        }
    private void loadImage(){
        originalBitmap=BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        image.getSource().setImageBitmap(originalBitmap);
    }

    @Override
    public void oncolorfragListenerClick(String text, int color) {
        photoEditor.addText(text,color);

    }


}
