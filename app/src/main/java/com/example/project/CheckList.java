package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project.Adapter.CheckListAdapter;
import com.example.project.Constants.MyConstants;
import com.example.project.Database.RoomDB;
import com.example.project.Models.Items;

import java.util.ArrayList;
import java.util.List;

public class CheckList extends AppCompatActivity {



    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    RoomDB database ;
    List<Items> itemsList = new ArrayList<>();
    String header , show ;

    EditText editText;
    ImageButton btnAdd ;
    LinearLayout linearLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        header = intent.getStringExtra(MyConstants.HEADER_SMALL);
        show = intent.getStringExtra(MyConstants.SHOW_SMALL);

        getSupportActionBar().setTitle(header);

        editText = findViewById(R.id.addtext);
        btnAdd = findViewById(R.id.addbtn);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linearLayout);

        database = RoomDB.getInstance(this);
        if (MyConstants.FALSE_STRING.equals(show)){
           linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getAllSelected(true);

        }else {
            itemsList = database.mainDao().getAll(header);
        }

        updateRecycle(itemsList);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = editText.getText().toString();
                if (!itemName.isEmpty()) {
                    addedNewItem(itemName);
                    Toast.makeText(CheckList.this,"Item Added", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CheckList.this,"Item should have a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



private void addedNewItem(String itemName){

        Items item = new Items();
    item.setChecked(false);
    item.setCategory(header);
    item.setItemname(itemName);
    item.setAddedby(MyConstants.USER_SMALL);
    database.mainDao().saveItem(item);
    itemsList = database.mainDao().getAll(header);
    updateRecycle(itemsList);
    recyclerView.scrollToPosition(checkListAdapter.getItemCount()-1);
    editText.setText("");


}
     private void updateRecycle(List<Items>itemList){
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1 , LinearLayoutManager.VERTICAL));
      checkListAdapter = new CheckListAdapter(CheckList.this , itemList, database , show);
      recyclerView.setAdapter(checkListAdapter);
     }


}