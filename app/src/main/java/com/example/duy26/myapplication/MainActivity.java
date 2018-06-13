package com.example.duy26.myapplication;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    Button uploadpic;
    ImageView imagebox;
    ProgressBar progressBar;
    byte[] byteArray;
    Connectionclass connectionclass;
    String encodedImage;

    Connection con;
    String un, ip, db, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadpic = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imagebox = (ImageView) findViewById(R.id.imageView);

        progressBar.setVisibility(View.GONE);

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Opening the Gallery and selecting media
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE );
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
                    // this will jump to onActivity Function after selecting image
                } else {
                    Toast.makeText(MainActivity.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
                }
                // End Opening the Gallery and selecting media
            }
        });
        Button button = (Button)findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MainActivity.this,REcyclerview.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            // getting the selected image, setting in imageview and converting it to byte and base 64
            progressBar.setVisibility(View.VISIBLE);
            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage().toString());
            }
            if (originBitmap != null) {
                this.imagebox.setImageBitmap(originBitmap);
                Log.w("Image Setted in", "Done Loading Image");
                try {
                    Bitmap image = ((BitmapDrawable) imagebox.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    // Calling the background process so that application wont slow down
                    UploadImage uploadImage = new UploadImage();
                    uploadImage.execute("");
                    //End Calling the background process so that application wont slow down
                } catch (Exception e) { }
                Toast.makeText(MainActivity.this, "Conversion Done", Toast.LENGTH_SHORT).show();
            }
        } else {
            System.out.println("Error Occured");
        }
    }

    public class UploadImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String r) {
            // After successful insertion of image
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Image Inserted Succesfully", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            // Inserting in the database
            String msg = "unknown";
            try {
                connectionclass = new Connectionclass();
                Connection connection = connectionclass.CONN();
                String insertTableSQL = "INSERT INTO [IMG] "
                        + "(Img) VALUES"
                        + "(?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);
                    preparedStatement.setString(1, encodedImage);
                    preparedStatement.executeUpdate();
                    msg = "Inserted Successfully";
                } catch (SQLException ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 1:", msg);
                } catch (IOError ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 2:", msg);
                } catch (AndroidRuntimeException ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 3:", msg);
                } catch (NullPointerException ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 4:", msg);
                } catch (Exception ex) {
                    msg = ex.getMessage().toString();
                    Log.d("Error no 5:", msg);
                }
                System.out.println(msg);
                return "";
                //End Inserting in the database
            } catch (Exception e) {
            }
            return msg;
        }
    }
}