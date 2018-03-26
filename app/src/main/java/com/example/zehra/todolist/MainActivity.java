package com.example.zehra.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private SwipeMenuListView todo;
    private EditText item_to_add;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayList;
    private Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrayList = new ArrayList<>();
        item_to_add = findViewById(R.id.newtodo);
        todo = findViewById(R.id.todolist);


        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                arrayList);

        todo.setAdapter(arrayAdapter);

        if (arrayAdapter.isEmpty()) {
            //write nothing to do if list empty
        }
        add = findViewById(R.id.add_to_list);
        add.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (item_to_add.getText().length() == 0) {
                   Toast.makeText(MainActivity.this,"Please enter a valid item", Toast.LENGTH_SHORT).show();
                            //pass
                } else {
                    String item = item_to_add.getText().toString();
                    arrayList.add(item);
                    arrayAdapter.notifyDataSetChanged();
                    item_to_add.setText("");
                }
            }
        });

        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0, 255,
                        0)));
                editItem.setIcon(android.R.drawable.ic_menu_edit);

                // set item width
                editItem.setWidth((120));
                // set a icon

                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(255, 0,
                        0)));
                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                // set item width
                deleteItem.setWidth((120));
                // set a icon

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        todo.setMenuCreator(creator);
        todo.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog, null);
                        dialogBuilder.setView(dialogView);

                        final EditText edt = dialogView.findViewById(R.id.edit1);

                        dialogBuilder.setTitle("Edit");
                        dialogBuilder.setMessage("Edit item");
                        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                arrayList.set(position,edt.getText().toString());
                                arrayAdapter.notifyDataSetChanged();
                                dialog.cancel();

                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //pass
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();

                        break;


                    case 1:

                        arrayList.remove(position);

                        arrayAdapter.notifyDataSetChanged();

                        break;

                    default:
                        break;
                }


                return true;
            }
        });

        // set SwipeListener
        todo.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                todo.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        // set MenuStateChangeListener
        todo.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {

            }

            @Override
            public void onMenuClose(int position) {
            }
        });
        try {
            Scanner scanner = new Scanner(openFileInput("Todo.txt"));
            while (scanner.hasNextLine()) {
                String todo_item = scanner.nextLine();
                arrayAdapter.add(todo_item);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

     /**
      *Following method is to save data when user paused or exit the app.
      *Items in array list will be written to the text file to make data persistent.
     **/
    @Override
    public void onPause() {
        super.onPause();
        try {
            PrintWriter printWriter = new PrintWriter(openFileOutput("Todo.txt", Context.MODE_PRIVATE));
            for (String data : arrayList) {
                printWriter.println(data);
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }
}
