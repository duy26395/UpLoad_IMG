package com.example.duy26.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class REcyclerview extends AppCompatActivity {
    Connectionclass connectionclass;
    ArrayList<Student> students;
    ResultSet resultSet;
    byte[] byteArray;

    StudentAdapter studentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_student);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        students = new ArrayList<>();
        getdata();
        studentAdapter = new StudentAdapter(students,getApplicationContext());
        recyclerView.setAdapter(studentAdapter);
    }

    private void getdata() {



        connectionclass = new Connectionclass();
        Connection connection = connectionclass.CONN();
        //Db have url http//:
        String query = "SELECT Image FROM [Food]";
        // Db url bitmap decode base64
        //String query = "SELECT Img FROM [IMG]";

        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    try {
                        students.add(new Student(resultSet.getString("Image")));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
