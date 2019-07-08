package com.example.cameraclick;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
Button btnTakePic;
ImageView imageView;
String pathToFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTakePic=findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakeAction();
            }
        });
if(Build.VERSION.SDK_INT>=23){
    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
}
        imageView=findViewById(R.id.image);
    }
@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK){
            if(requestCode==1){
                Bitmap bitmap= BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);
            }
        }
}
    private void dispatchPictureTakeAction() {
        Intent takePic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//to open camera
        if(takePic.resolveActivity(getPackageManager())!=null){
            File photoFile=null;
            photoFile=createPhotoFile();
            if(photoFile!=null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri= FileProvider.getUriForFile(MainActivity.this,"com.example.cameraclick.fileprovider",photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(takePic,1);
            }
        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_MMmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        }
        catch (IOException e){
            Log.d("mylog","Except : "+e.toString());
        }
        return image;
    }


}

