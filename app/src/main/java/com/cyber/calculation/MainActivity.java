package com.cyber.calculation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cyber.calculation.Adapters.Adapter;
import com.cyber.calculation.Interfaces.ClickInterface;
import com.cyber.calculation.Interfaces.DataDao;
import com.cyber.calculation.Models.Data;
import com.cyber.calculation.Utils.AppDatabase;
import com.cyber.calculation.Utils.ExcelHelper;
import com.cyber.calculation.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity  {


    ActivityMainBinding binding;
    private Adapter adapter;
    private List<Data> dataList;
    private AppDatabase db;
    private DataDao dataDao;

    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onStart() {
        super.onStart();

       activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                List<Data> exportList = dataDao.getAll();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ExcelHelper excelHelper = new ExcelHelper(exportList, getApplicationContext(), uri);
                                        excelHelper.createExcelSheet();
                                    }
                                });

                            }
                        });
                    }
                }
        );



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ClickInterface clickInterface = new ClickInterface() {
            @Override
            public void onclick(Data data) {
                Log.i("TAG", new Gson().toJson(data));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dataDao.update(data);
                    }
                }).start();
            }
        };


     new Thread(() -> {
         db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "storage").build();
         dataDao = db.dataDao();
         dataList = dataDao.getAll();

         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 binding.mainRv.setHasFixedSize(true);
                 binding.mainRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                 adapter = new Adapter(getApplicationContext(), dataList, clickInterface );
                 binding.mainRv.setAdapter(adapter);
             }
         });
     }).start();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.mainRv);

        binding.btnAdd.setOnClickListener(v->{
            addItem();
        });

        binding.btnExport.setOnClickListener(v->{

            if(!checkEmtpyObject(dataList)){
                return;
            }

            String xlsName = "excelSheet" + System.currentTimeMillis() + ".xls";
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/xls");
            intent.putExtra(Intent.EXTRA_TITLE, xlsName);
            activityResultLauncher.launch(intent);


        });

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Please Confirm")
                                .setMessage("You are about to delete the whole database")
                                        .setNegativeButton("Cancel", null)
                                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        AsyncTask.execute(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                db.clearAllTables();
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        dataList.clear();
                                                                        adapter.notifyDataSetChanged();
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }).create().show();

            }
        });
    }

    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            binding.disabledLayer.setVisibility(View.VISIBLE);

            int pos = viewHolder.getAdapterPosition();
            String index = String.valueOf(pos + 1);
            Data oldData = dataList.get(pos);

            dataList.remove(pos);
            adapter.notifyItemRemoved(pos);

            Snackbar.make(binding.mainRv, index , Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 dataList.add(pos, oldData);
                 adapter.notifyItemInserted(pos);
                 binding.disabledLayer.setVisibility(View.GONE);
                }
            }).addCallback(
                    new Snackbar.Callback(){
                        @Override
                        public void onShown(Snackbar sb) {
                            super.onShown(sb);
                        }

                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                                new Thread(() -> {
                                    dataDao.delete(oldData);
                                    runOnUiThread(() -> {
                                        adapter.notifyDataSetChanged();
                                    binding.disabledLayer.setVisibility(View.GONE);
                                    });
                                }).start();
                            }
                        }
                    }
            ).show();


        }
    };

    private void addItem(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Data newData = new Data();
                dataDao.insert(newData);
                dataList.clear();
                List<Data> newdataList = dataDao.getAll();
                for(Data data: newdataList){
                    dataList.add(data);
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    private void calculate(){

    }


    private void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean checkEmtpyObject(List<Data> checkList){
        boolean  check = true;

        if(checkList.size() == 0 ){
            Toast.makeText(getApplicationContext(), "List is empty", Toast.LENGTH_SHORT).show();
            check= false;
        }

        for(int i=0; i<checkList.size(); i++){
            Data data = checkList.get(i);
            int index = i +1 ;
            if(TextUtils.isEmpty(data.getBsUp())
                    || TextUtils.isEmpty(data.getBsX())
                    || TextUtils.isEmpty(data.getBsDown())
                    || TextUtils.isEmpty(data.getFsUp())
                    || TextUtils.isEmpty(data.getFsX())
                    || TextUtils.isEmpty(data.getFsDown())
            ){
                Toast.makeText(getApplicationContext(), "Error in index " + index, Toast.LENGTH_SHORT).show();
                check = false;
                break;
            }
        }

        return check;
    }

}