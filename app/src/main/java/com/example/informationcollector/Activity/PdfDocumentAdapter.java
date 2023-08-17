package com.example.informationcollector.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfDocumentAdapter  extends PrintDocumentAdapter {
    Context context;
    String path;

    public PdfDocumentAdapter(Context context, String path) {
        this.context = context;
        this.path = path;
    }



    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
if(cancellationSignal.isCanceled()){
    layoutResultCallback.onLayoutCancelled();
}else{
    PrintDocumentInfo.Builder builder=new PrintDocumentInfo.Builder("file name");
    builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT);
    builder.setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN);
    builder.build();
    layoutResultCallback.onLayoutFinished(builder.build(),!printAttributes1.equals(printAttributes));
}
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in =null;
        OutputStream out=null;
        try{
            File file=new File(path);

            File[] listOfFiles = file.listFiles();

            for (File file1 : listOfFiles) {
                if (file1.isFile()) {
                    in=new FileInputStream(file1);
                }
            }


            out=new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
             byte[] buff=new byte[16384];
             int size;
             while((size=in.read(buff))>=0 && !cancellationSignal.isCanceled()){
                 out.write(buff,0,size);
             }
             if(cancellationSignal.isCanceled()){
                 writeResultCallback.onWriteCancelled();
             }else{
                 writeResultCallback.onWriteFinished(new PageRange[]{
                         PageRange.ALL_PAGES
                 });
             }
        } catch (Exception e) {

            writeResultCallback.onWriteFailed(e.getMessage());
            Log.d("write failed==",e.getMessage());
        }
        finally {
            try {
                if(in!=null)
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(out!=null)
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
