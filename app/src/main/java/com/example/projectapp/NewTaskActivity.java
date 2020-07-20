package com.example.projectapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class NewTaskActivity extends AppCompatActivity{

    DatePickerDialog picker;
    Bundle bundle;
    Intent intent;
    ProgressBar Prog;
    ScrollView storedOrders;
    String prior;
    MenuItem save_item;
    int project_id;
    EditText name, date;
    Spinner priority;
    String[] priorityArray = { "Low", "Medium", "High"};
    HttpOperations IO = new HttpOperations();
    HashMap<String, String> params = new HashMap<>();
    private static final int CODE_POST_REQUEST = 1025;
    int OpType = -1;
    int taskId;
    String task_Name, task_Date;
    String task_Priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.newtask_activity);

        Intent intent = getIntent();

        project_id = intent.getIntExtra("TaskListId", -1);
        OpType =  intent.getIntExtra("OpType", -1);
        taskId =  intent.getIntExtra("TaskId", -1);

        task_Name = intent.getStringExtra("TaskName");
        task_Date = intent.getStringExtra("TaskDate");
        task_Priority = intent.getStringExtra("TaskPriority");

        System.out.println(task_Name);

        name  = (EditText) findViewById(R.id.edit_name);
        date  = (EditText) findViewById(R.id.edit_date);
        priority  = (Spinner) findViewById(R.id.edit_priority);




        name.setText(task_Name);
        date.setText(task_Date);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        if(OpType == 1) {
            date.setText(formattedDate);
        }
        date.setInputType(InputType.TYPE_NULL);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(NewTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                picker.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorityArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priority.setAdapter(adapter);
        priority.setPrompt("Title");

        if(task_Priority != null) {
            if (task_Priority.equals("Low")) {
                priority.setSelection(0);
            } else if (task_Priority.equals("Medium")) {
                priority.setSelection(1);
            } else if (task_Priority.equals("High")) {
                priority.setSelection(2);
            }
        }
        else
        {
            priority.setSelection(0);
        }

        if(OpType == 1) {
            priority.setSelection(0);
        }
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                prior = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation3, menu);
        save_item = menu.findItem(R.id.save);
        save_item.setVisible(true);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(getApplicationContext(),TasksActivity.class);
                bundle = new Bundle();
                bundle.putInt("TaskListId", project_id);
                intent.putExtras(bundle);

                startActivity(intent);
                return true;

            case R.id.save:
                if(name.getText().equals("") || prior.equals(""))
                {
                    Toast.makeText(getBaseContext(), "Some value is empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Date currentTime = Calendar.getInstance().getTime();
                    params = new HashMap<>();

                    if(OpType == 1) {
                        params.put("name", name.getText().toString());
                        params.put("done", "0");
                        params.put("tasklist_id", Integer.toString(project_id));
                        params.put("user_id", "1");
                        params.put("date", date.getText().toString());
                        params.put("priority", prior);
                        params.put("createtime", currentTime.toString());
                        Context mC = getApplicationContext();
                        IO.T(Api.URL_POST_TASK, params, CODE_POST_REQUEST, "InsTask", Prog, storedOrders, mC, NewTaskActivity.this, IO.delegate, false, 0);
                    }
                    else
                    {
                        params.put("id", Integer.toString(taskId));
                        params.put("name", name.getText().toString());
                        params.put("date", date.getText().toString());
                        params.put("priority", prior);
                        Context mC = getApplicationContext();
                        IO.T(Api.URL_SERVER_TASK_UPDATES, params, CODE_POST_REQUEST, "UpdateTask", Prog, storedOrders, mC, NewTaskActivity.this, IO.delegate, false, taskId);

                    }
                    intent = new Intent(getApplicationContext(),TasksActivity.class);
                    bundle = new Bundle();
                    bundle.putInt("TaskListId", project_id);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

                return (true);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
