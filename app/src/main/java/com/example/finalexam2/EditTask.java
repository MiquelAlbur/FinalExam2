package com.example.finalexam2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class EditTask extends AppCompatActivity {
    private TextView _tv;
    private EditText _et;
    private String _date,_name,_type;
    private CheckBox _ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        this._ch = findViewById(R.id.taskchecked);
        this._tv = findViewById(R.id.etaskdate);
        this._et = findViewById(R.id.etaskname);
        if(getIntent().getExtras() != null){
            this._name = getIntent().getStringExtra("name");
            this._date = getIntent().getStringExtra("date");
            this._et.setText(this._name);
            if(getIntent().getStringExtra("done").equals("true")) {
                this._ch.setChecked(true);
            }
            this._type = "edit";
        }else{
            _date = new Date().toString();
            this._ch.setEnabled(false);
            this._type = "create";
        }
        this._tv.setText(_date);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        return true;
    }
    public void save(MenuItem mi){
        _name = _et.getText().toString();
        if(_name.equals("")){
            Toast.makeText(EditTask.this, "No has insertat totes les dades", Toast.LENGTH_SHORT).show();
        }else{
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name",_name);
            if(_type.equals("edit")){
                if(this._ch.isChecked()) {
                    resultIntent.putExtra("done", "true");
                    _date = new Date().toString();
                }else{
                    resultIntent.putExtra("done","false");
                }
                resultIntent.putExtra("id",getIntent().getIntExtra("id",0));
            }
            resultIntent.putExtra("date",_date);
            setResult(RESULT_OK,resultIntent);
            finish();
        }
    }
    public void close(MenuItem mi){
        onBackPressed();
    }
}
