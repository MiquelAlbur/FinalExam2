package com.example.finalexam2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnNoteListener {

    private Boolean all;
    private MyDB _db;
    private RecyclerView _rv;
    private ArrayList<Task> _tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        all = false;
        this._rv = findViewById(R.id.recycler);
        this._db = new MyDB(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        _db.createRecords(0,"Test", new Date().toString(),"false");
//        _db.createRecords(1,"Test2", new Date().toString(),"true");// PRIMERS DOS INSERTS DE PROVA(ja están insertats pero deixo el comentari per a tenir clar quines son les dades)
        setAdapter();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Create New Task", Snackbar.LENGTH_LONG)
                        .setAction("CREATE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                createTask();
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            switchall();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAdapter() {
        Cursor c = _db.selectRecords(all);
        _tasks = new ArrayList<Task>();
        if (c.getCount() > 0) {//Revisar Sintaxis?
            do {
                _tasks.add(new Task(c.getInt(0), c.getString(1), c.getString(2), c.getString(3)));
            } while (c.moveToNext());
        }
        RecyclerAdapter ra = new RecyclerAdapter(_tasks, this, this, c);
        _rv.setAdapter(ra);
        _rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateAdapter(int pos) {//Quan cambiam una checkbox a true
        _tasks.get(pos).setDone("true");
        _db.updateRecords(_tasks.get(pos).getId(), _tasks.get(pos).getName(), new Date().toString(), "true");
        setAdapter();
    }

    private void createTask() {
        Intent i = new Intent(this, EditTask.class);
        startActivityForResult(i, 1);
    }

    public void switchall() {
        all ^= true;
        setAdapter();
    }

    @Override
    public void onNoteClick(int position) {
        if(!_tasks.get(position).getDone().equals("true")) {
            Intent i = new Intent(this, EditTask.class);
            i.putExtra("id", _tasks.get(position).getId());
            i.putExtra("name", _tasks.get(position).getName());
            i.putExtra("date", _tasks.get(position).getDate());
            i.putExtra("done", _tasks.get(position).getDone());
            startActivityForResult(i, 2);
        }
        else{
            Toast.makeText(this, "Completed Tasks are not editable", Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){//Result de Crear Task
            if(resultCode == RESULT_OK){//Revisar tema size
                _db.createRecords(data.getStringExtra("name"), data.getStringExtra("date"), "false");
                Toast.makeText(this, "Task Saved", Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari
                setAdapter();
            }
        }
        if (requestCode == 2){//Result d'editar task
            if (resultCode == RESULT_OK){
                _db.updateRecords(data.getIntExtra("id",0), data.getStringExtra("name"), data.getStringExtra("date"), data.getStringExtra("done"));
                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();//Toast per a avisar a l'usuari
                setAdapter();
            }
        }
    }
}
