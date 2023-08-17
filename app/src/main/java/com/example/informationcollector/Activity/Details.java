package com.example.informationcollector.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.informationcollector.ModelClass.ImageData;
import com.example.informationcollector.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class Details extends AppCompatActivity {
AppCompatEditText name,address,mobile_no,organization_name,purpose,last_name;
Button submit;
Spinner spinner,id_type;
AppCompatImageView imageView,take_photo,who_r_you_arr,id_type_arr;
private static final int pic_id = 123;
Bitmap photo;
String temp;
String name_str,address_str,mobile_str,organization_str,purpose_str,custm_type,idproof_type,last_name_str;
String customer_type []={"Who are you","vendor","agency","others"};
String id_proof[]={"Select Your Id Type","Adhar Card","VoterId Card","Pan Card","Driving License","Other"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        name=findViewById(R.id.first_name);
        last_name=findViewById(R.id.last_name);
        address=findViewById(R.id.Address_editbox);
        mobile_no=findViewById(R.id.Mobile_editbox);
        organization_name=findViewById(R.id.Oganization_Name_editbox);
        purpose=findViewById(R.id.Purpose_editbox);
        submit=findViewById(R.id.submit_button);
        spinner=findViewById(R.id.Customer_type);
        id_type=findViewById(R.id.Id_Type);
         take_photo=findViewById(R.id.camera_button);
         imageView=findViewById(R.id.camera_img);



        ArrayAdapter aa = new ArrayAdapter(this, R.layout.spinner_layout,customer_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                 spinner.setAdapter(aa);
                 spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     @Override
                     public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                         custm_type= customer_type[i];
                         aa.setNotifyOnChange(true);
                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> adapterView) {
                         Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please select Customer Type", Snackbar.LENGTH_LONG);
                         snackbar.show();
                     }
                 });


        ArrayAdapter id = new ArrayAdapter(this,R.layout.spinner_layout,id_proof);
        id.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        id_type.setAdapter(id);
        id_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idproof_type= id_proof[i];
                id.setNotifyOnChange(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                take_photo.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit_Details();

            }
        });



            }



    private void Submit_Details() {
            name_str=name.getText().toString();
            address_str=address.getText().toString();
            mobile_str=mobile_no.getText().toString();
            organization_str=organization_name.getText().toString();
            purpose_str=purpose.getText().toString();
            last_name_str=last_name.getText().toString();
            if(name_str.equals("")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter your name", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(address_str.equals("")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter address", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(mobile_str.equals("")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter your mobile number", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(organization_str.equals(""))
            {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter your organization name", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(purpose_str.equals("")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please enter your purpose", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(custm_type.equals("Who are you")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please select Customer Type", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            else if(idproof_type.equals("Select Your Id Type")){
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please select id Type", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if(temp==null){
               temp="";
            }
            else{

                Intent intent=new Intent(getApplicationContext(),Print_Screen.class);
                intent.putExtra("name",name_str+last_name_str);
                intent.putExtra("address",address_str);
                intent.putExtra("mobile_no",mobile_str);
                intent.putExtra("org_name",organization_str);
                intent.putExtra("purpose",purpose_str);
                intent.putExtra("customer_type",custm_type);
                intent.putExtra("id_type",idproof_type);
                intent.putExtra("photo",temp);
                startActivity(intent);
                finish();
            }


        }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
             photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
             temp= Base64.encodeToString(b, Base64.DEFAULT);
            imageView.setImageBitmap(photo);
        }
    }

}