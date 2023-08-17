package com.example.informationcollector.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.informationcollector.ModelClass.Clintdata;
import com.example.informationcollector.ModelClass.ImageData;
import com.example.informationcollector.R;
import com.example.informationcollector.SigletonClass.Apiinterface;
import com.example.informationcollector.SigletonClass.BaseUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfDocument.PdfInfo;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Print_Screen extends AppCompatActivity {
AppCompatImageView back_button;
AppCompatTextView print;
String name,address,mobile_no,Org_name,purpose,custm_type,id_type,photo,temp;
JSONObject jsonObject;
Bitmap bitphoto;
ImageData imageData;
@SuppressLint("WrongThread")
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_screen);
        back_button=findViewById(R.id.print_backbutton);
        print=findViewById(R.id.print_textbox);
        name=getIntent().getStringExtra("name");
        address=getIntent().getStringExtra("address");
        mobile_no=getIntent().getStringExtra("mobile_no");
        Org_name=getIntent().getStringExtra("org_name");
        purpose=getIntent().getStringExtra("purpose");
        custm_type=getIntent().getStringExtra("customer_type");
        id_type=getIntent().getStringExtra("id_type");
        photo=getIntent().getStringExtra("photo");
     /* byte[] strBytes = Base64.decode(photo, Base64.DEFAULT);
       byte[] encoded = Base64.encode(strBytes, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
        temp  = new String(encoded);*/
       jsonObject=new JSONObject();

    try {
        jsonObject.put("Name",name);
        jsonObject.put("Address",address);
        jsonObject.put("Mobile_no",mobile_no);
        jsonObject.put("Org_name",Org_name);
        jsonObject.put("Customer_type",custm_type);
        jsonObject.put("purpose",purpose);
        jsonObject.put("Id_type",id_type);
        jsonObject.put("Image",photo);
    } catch (JSONException e) {
        e.printStackTrace();
    }


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            print.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    insert_data();


                                }
                            });



                    }


                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showSettingsDialog();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Details.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void insert_data() {

        Apiinterface saveData = BaseUrl.getClient().create(Apiinterface.class);
        Call<Clintdata> call= saveData.insertdata(jsonObject.toString());
          call.enqueue(new Callback<Clintdata> () {
              @Override
              public void onResponse(Call<Clintdata> call, Response<Clintdata> response) {
                  if(response.isSuccessful()){
                        Clintdata clintdata= response.body();
                     // Toast.makeText(Print_Screen.this, ""+clintdata.getMessage(), Toast.LENGTH_SHORT).show();
                      Log.i("response==",clintdata.getMessage());
                      if(clintdata.getMessage().equals("Your data is uploaded successfully")) {
                          try {
                              createPDFfile(Common.getAppPath(getApplicationContext()) + "test_pdf.pdf");
                          } catch (BadElementException e) {
                              e.printStackTrace();
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }else{
                          Toast.makeText(Print_Screen.this, "your data is not uploaded", Toast.LENGTH_SHORT).show();
                      }
                  }else{
                      System.out.print("response=="+"is not successful");
                  }
              }

              @Override
              public void onFailure(Call <Clintdata>call, Throwable t) {
                  Log.i("response==", String.valueOf(call));
                  Log.i("response==", String.valueOf(t));
                  Toast.makeText(Print_Screen.this, "response is failed", Toast.LENGTH_SHORT).show();
              }

          });
    }

    private void createPDFfile(String path) throws BadElementException, IOException {
       if(new File(path).exists()){
           new File(path).delete();
       }
       try {
          Document document = new Document();

           PdfWriter.getInstance(document, new FileOutputStream(path));
           document.open();
           document.setPageSize(PageSize.A5);
           document.addCreationDate();
           document.addCreator("Kryp Media");
           document.addAuthor("Kryp Media");
           BaseColor baseColor=new BaseColor(0,153,204,255);
           float fontSize= 18.0f;
            BaseFont baseFont= BaseFont.createFont(BaseFont.HELVETICA,"UTF-8",BaseFont.NOT_EMBEDDED);
           Font title_font =new Font(baseFont,24.0f,Font.BOLD,baseColor);
           addNewItem(document,"Details", Element.ALIGN_CENTER,title_font);
           addLineSaparator(document);
           addSpace(document);
            Font description_font= new Font(baseFont,fontSize,Font.NORMAL,BaseColor.BLACK);
            addDescItem(document,"Name :",name,Element.ALIGN_BASELINE,description_font);
           addSpace(document);
           addDescItem(document,"Mobile Number :",mobile_no,Element.ALIGN_BASELINE,description_font);
           addSpace(document);
           addDescItem(document,"Organization Name :",Org_name,Element.ALIGN_BASELINE,description_font);
           addSpace(document);
           addDescItem(document,"Customer Type : ",custm_type,Element.ALIGN_BASELINE,description_font);
           addSpace(document);

           Font address_font = new Font(baseFont, fontSize, Font.NORMAL, BaseColor.BLACK);
           if(address.length()>30) {
               addNewItem(document, "Address :", Element.ALIGN_CENTER, address_font);
               addNewItem(document, address, Element.ALIGN_CENTER, address_font);
               addSpace(document);
           }else{
                addDescItem(document,"Address :",address,Element.ALIGN_BASELINE,description_font);
               addSpace(document);
           }
           if(purpose.length()>30){
               addNewItem(document,"Purpose:",Element.ALIGN_CENTER,address_font);
               addNewItem(document,purpose,Element.ALIGN_CENTER,address_font);
               addSpace(document);
           }else{
               addDescItem(document,"Purpose : ",purpose,Element.ALIGN_BASELINE,description_font);
               addSpace(document);
           }
           addDescItem(document,"Id Type : ",id_type,Element.ALIGN_BASELINE,description_font);
           addSpace(document);

           addDocumentImage(document);

           addLineSaparator(document);
            addSpace(document);
            document.close();
            print_document();
       }catch (FileNotFoundException e){
           Toast.makeText(this, "file not found==="+e, Toast.LENGTH_SHORT).show();
           Log.d("file not found==", String.valueOf(e));
       } catch (DocumentException e) {
           Toast.makeText(this, "document exception=="+e, Toast.LENGTH_SHORT).show();

           e.printStackTrace();
       } catch (IOException e) {
           Toast.makeText(this, "io exception==="+e, Toast.LENGTH_SHORT).show();
           Log.d("io exception===",e.getMessage());
           e.printStackTrace();
       }

    }

   // @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDocumentImage(Document document) throws DocumentException, IOException {
      try {
          byte [] encodeByte = Base64.decode(photo,Base64.DEFAULT);
          Image image=Image.getInstance(encodeByte);
          image.scaleAbsolute(200,100);
          image.setAlignment(Element.ALIGN_CENTER);
          document.add(image);

      }catch(Exception e){

      }




    }

    private void print_document() {
       // Toast.makeText(this, "print document==", Toast.LENGTH_SHORT).show();
        try{
            PrintManager printManager= (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printDocumentAdapter=new PdfDocumentAdapter(getApplicationContext(),Common.getAppPath(getApplicationContext()));
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void addSpace(Document document) throws DocumentException {
       // Toast.makeText(this, "add space==", Toast.LENGTH_SHORT).show();
        document.add(new Paragraph(" "));
    }

    private void addLineSaparator(Document document) throws DocumentException {
       // Toast.makeText(this, "add line saparator==", Toast.LENGTH_SHORT).show();
        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,64));
        addSpace(document);
        document.add(new Chunk(lineSeparator));
        addSpace(document);

    }

    private void addDescItem(Document document,String text_left,String text_right, int alignBaseline, Font description_font) throws DocumentException {
        Chunk chunk=new Chunk(text_left,description_font);
        Chunk chunk1=new Chunk(text_right,description_font);
        Paragraph paragraph=new Paragraph();
        paragraph.setAlignment(alignBaseline);
        paragraph.add(chunk);
        paragraph.add(new Chunk(new VerticalPositionMark()));
        paragraph.setAlignment(alignBaseline);
        paragraph.add(chunk1);
        document.add(paragraph);

    }


    private void addNewItem(Document document, String details, int alignCenter, Font title_font) throws DocumentException {
        Chunk chunk=new Chunk(details,title_font);
        Paragraph paragraph=new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        document.add(paragraph);
    }
    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Print_Screen.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {

            dialog.cancel();
        });

        builder.show();
    }
}