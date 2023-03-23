package com.cyber.calculation.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cyber.calculation.Interfaces.ClickInterface;
import com.cyber.calculation.Models.Data;
import com.cyber.calculation.R;
import com.cyber.calculation.Utils.Utils;
import com.cyber.calculation.databinding.ItemMainBinding;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    @NonNull
    ItemMainBinding binding;
    private Context context;
    private List<Data> dataList;
    private ArrayAdapter<String> arrayAdapter;

    ClickInterface clickInterface;

    public Adapter(Context context, List<Data> dataList, ClickInterface clickInterface){
        this.context = context;
        this.dataList = dataList;
        this.clickInterface = clickInterface;

        String[] options = context.getResources().getStringArray(R.array.options);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, options);
        this.arrayAdapter = arrayAdapter;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         binding = ItemMainBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
         return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data data = dataList.get(position);

        holder.binding.itemTilBs1.getEditText().setText(data.bsUp);
        holder.binding.itemTilBs2.getEditText().setText(data.bsX );
        holder.binding.itemTilBs3.getEditText().setText(data.bsDown);
        holder.binding.itemTilBs4.getEditText().setText(data.bsRemark);

        holder.binding.itemTilFs1.getEditText().setText(data.fsUp );
        holder.binding.itemTilFs2.getEditText().setText(data.fsX);
        holder.binding.itemTilFs3.getEditText().setText(data.fsDown );
        holder.binding.itemTilFs4.getEditText().setText(data.fsRemark);


        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(holder.binding.inputHolder .getVisibility() == View.VISIBLE){

                   if(!validate(holder)){
                       Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                       return;
                   }

                   holder.binding.inputHolder.setVisibility(View.GONE);
                   holder.binding.itemArrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_drop_down_24));
               } else {
                   holder.binding.inputHolder.setVisibility(View.VISIBLE);
                   holder.binding.itemArrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_drop_up_24));
               }
            }
        });

        if(holder.binding.inputHolder .getVisibility() == View.VISIBLE){
            holder.binding.itemArrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_drop_up_24));
        } else {
            holder.binding.itemArrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_drop_down_24));
        }

        holder.binding.btnSave.setOnClickListener(v->{
            if(!validate(holder)){
                return;
            }


            String bsUp = holder.binding.itemTilBs1.getEditText().getText().toString().trim();
            String bsX = holder.binding.itemTilBs2.getEditText().getText().toString().trim();
            String bsDown = holder.binding.itemTilBs3.getEditText().getText().toString().trim();
            String bsRemark = holder.binding.itemTilBs4.getEditText().getText().toString().trim();

            String fsUp = holder.binding.itemTilFs1.getEditText().getText().toString().trim();
            String fsX = holder.binding.itemTilFs2.getEditText().getText().toString().trim();
            String fsDown = holder.binding.itemTilFs3.getEditText().getText().toString().trim();
            String fsRemark = holder.binding.itemTilFs4.getEditText().getText().toString().trim();



            data.setBsUp(bsUp);
            data.setBsX(bsX);
            data.setBsDown(bsDown);
            data.setBsRemark(bsRemark);
            data.setFsUp(fsUp);
            data.setFsX(fsX);
            data.setFsDown(fsDown);
            data.setFsRemark(fsRemark);

            clickInterface.onclick(data);

            holder.binding.itemArrow.setImageDrawable(context.getDrawable(R.drawable.baseline_arrow_drop_down_24));
            holder.binding.inputHolder.setVisibility(View.GONE);

        });

        int index = position + 1;
        holder.binding.itemTitle.setText(index + ".");
    }

    private boolean validate(ViewHolder holder){
        boolean check  = true;
        if(!Utils.hasText(holder.binding.itemTilBs1)) check = false;
        if(!Utils.hasText(holder.binding.itemTilBs2)) check = false;
        if(!Utils.hasText(holder.binding.itemTilBs3)) check = false;

        if(!Utils.hasText(holder.binding.itemTilFs1)) check = false;
        if(!Utils.hasText(holder.binding.itemTilFs2)) check = false;
        if(!Utils.hasText(holder.binding.itemTilFs3)) check = false;

        return check;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemMainBinding binding;

        public ViewHolder(ItemMainBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
