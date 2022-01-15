package com.example.scanqr.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scanqr.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRfragment extends Fragment {
    ImageView imgQR;
    String user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_qr,container,false);

        imgQR=(ImageView) view.findViewById(R.id.imageQR);
        Bundle bundle=getArguments();
        if(bundle!=null){
            user=bundle.getString("user");
        }

        MultiFormatWriter writer=new MultiFormatWriter();
        try {
            BitMatrix matrix =writer.encode(user, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder encoder=new BarcodeEncoder();
            Bitmap bitmap =encoder.createBitmap(matrix);
            imgQR.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return view;
    }
}
