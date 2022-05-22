package com.example.memorryaplicationproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ShoppingListActivity extends AppCompatActivity {

    private EditText item;
    private Button addItem;
    private ListView listView;
    private ArrayList <String> itemList = new ArrayList<>();
    private ArrayAdapter <String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        item = findViewById(R.id.et_shoppingList);
        addItem = findViewById(R.id.bt_shopList);
        this.listView = findViewById(R.id.list_shoppingList);

        this.itemList = FileHelper.readData(ShoppingListActivity.this);

        arrayAdapter = new ArrayAdapter<>(ShoppingListActivity.this,
                android.R.layout.simple_list_item_1,

                android.R.id.text1,
                itemList);
        listView.setAdapter(arrayAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = item.getText().toString();
                if (itemName.isEmpty()) {
                    Toast.makeText(ShoppingListActivity.this,
                            "Please enter an item",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                itemList.add(itemName);
                item.setText("");
                FileHelper.writeData(itemList, ShoppingListActivity.this);
                arrayAdapter.notifyDataSetChanged();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ShoppingListActivity.this);
                alert.setTitle("Delete item");
                alert.setMessage("Is the task you wish to delete completed? Are you finished with your shopping and has the item you want to remove been purchased? ");
                alert.setCancelable(false);
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                });

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        itemList.remove(i);
                        arrayAdapter.notifyDataSetChanged();
                        FileHelper.writeData(itemList, ShoppingListActivity.this);
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });



    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ShoppingListActivity.this,
                MainMenuActivity.class));
        finish();
    }
}