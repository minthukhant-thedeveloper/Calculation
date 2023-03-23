package com.cyber.calculation.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.cyber.calculation.Models.Data;


import java.io.File;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelHelper{
    List<Data> dataList;
    Context context;

    DecimalFormat df;
    Uri uri;

    public ExcelHelper(List<Data> dataList, Context context, Uri uri) {
        this.dataList = dataList;
        this.context = context;
        this.df = new DecimalFormat("0.000");
        this.uri = uri;
    }

    public void createExcelSheet(){

        WorkbookSettings workbookSettings = new WorkbookSettings();
        workbookSettings.setLocale(new Locale("en", "EN"));

        try {
            OutputStream os = context.getContentResolver().openOutputStream(uri);

            WritableWorkbook workbook = Workbook.createWorkbook(os, workbookSettings);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);

            Label label0 = new Label(0, 0, "BS Result");
            Label label1 = new Label(1, 0, "BS Remark");
            Label label2 = new Label(2, 0, "FS Result");
            Label label3 = new Label(3, 0, "FS Remark");

            sheet.addCell(label0);
            sheet.addCell(label1);
            sheet.addCell(label2);
            sheet.addCell(label3);

            for(int i=0; i< dataList.size(); i++){
                int row = i+1;
                Data data = dataList.get(i);
                float bsTotal = Float.parseFloat(data.bsUp) +  Float.parseFloat(data.bsX) +  Float.parseFloat(data.bsDown);
                float bsResult = bsTotal/3;

                float fsTotal = Float.parseFloat(data.fsUp) +  Float.parseFloat(data.fsX) +  Float.parseFloat(data.fsDown);
                float fsResult = fsTotal/3;

                String bsFinal = df.format(bsResult);
                String fsFinal = df.format(fsResult);

                Label looplabel0 = new Label(0, row, bsFinal);
                Label looplabel1 = new Label(1, row, data.bsRemark);
                Label looplabel2 = new Label(2, row, fsFinal);
                Label looplabel3 = new Label(3,  row, data.fsRemark);
                sheet.addCell(looplabel0);
                sheet.addCell(looplabel1);
                sheet.addCell(looplabel2);
                sheet.addCell(looplabel3);
            }


            workbook.write();
            workbook.close();
            Toast.makeText(context, "Excel created successfully", Toast.LENGTH_SHORT).show();


        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
