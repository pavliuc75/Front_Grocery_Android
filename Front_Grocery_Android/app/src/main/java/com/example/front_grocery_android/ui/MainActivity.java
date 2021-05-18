package com.example.front_grocery_android.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.front_grocery_android.R;
import com.example.front_grocery_android.models.Item;
import com.example.front_grocery_android.models.List;
import com.example.front_grocery_android.models.Lists;
import com.example.front_grocery_android.repository.ListsRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TODO:testing
    //TODO:git readme
    private MainActivityViewModel viewModel;
    private ImageButton imageButtonHelp;
    private Button buttonGoToList;
    private Button buttonGenerate;
    private EditText editTextListId;
    private Lists listsScoped;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //disable night mode globally
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.init();

        imageButtonHelp = findViewById(R.id.imageButtonHelp);
        buttonGoToList = findViewById(R.id.button_go_to_list);
        buttonGenerate = findViewById(R.id.button_generate);
        editTextListId = findViewById(R.id.editText_list_id);
        preferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();

        //no internet
        if (!isNetworkConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.no_internet);
            AlertDialog dialog = builder.create();
            dialog.setOnCancelListener(dialog1 -> onBackPressed());
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        //read shared preferences
        int localId = preferences.getInt("id", -1); //-1 is the default value.
        if (localId != -1 && isNetworkConnected()) {
            viewModel.setSelectedListId(localId);
            startActivity(new Intent(this, ListActivity.class));
            finish();
        }

        //get listener
        viewModel.getLists().observe(this, lists -> {
            listsScoped = lists;
        });


        //help button
        imageButtonHelp.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.main_help_text);
            builder.setPositiveButton(R.string.close_alert_text, (dialog, id) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        //set last list id when activity called from list activity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("listId")) {
            int listId = bundle.getInt("listId");
            editTextListId.setText(String.valueOf(listId));
            editTextListId.requestFocus();
            editTextListId.setSelection(editTextListId.getText().length());
        }


        //go to list button
        buttonGoToList.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editTextListId.getText())) {
                editTextListId.setError(getString(R.string.field_required));
            } else if ((Integer.parseInt(editTextListId.getText().toString()) > 9999) || (Integer.parseInt(editTextListId.getText().toString()) < 1000)) {
                editTextListId.setError(getString(R.string.main_set_error_2));
            } else if (!checkIfListExists(Integer.parseInt(editTextListId.getText().toString()))) {
                editTextListId.setError(getString(R.string.main_set_error_3));
            } else {
                Intent toList = new Intent(this, ListActivity.class);
                viewModel.setSelectedListId(Integer.parseInt(editTextListId.getText().toString()));

                //save shared preferences
                editor.putInt("id", viewModel.getSelectedListId());
                editor.apply();

                startActivity(toList);
            }
        });

        //generate button
        buttonGenerate.setOnClickListener(v -> {
            if (listsScoped != null) {
                int lastListId = listsScoped.getBody().get(listsScoped.lists.size() - 1).id;
                if (lastListId <= 9998) {
                    viewModel.setSelectedListId(lastListId + 1);
                    viewModel.generateNewList(viewModel.getSelectedListId());
                    editor.putInt("id", viewModel.getSelectedListId());
                    editor.apply();
                    Intent toList = new Intent(this, ListActivity.class);
                    startActivity(toList);
                } else {
                    Toast.makeText(this, R.string.secret_text, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkIfListExists(int listId) {
        if (listsScoped != null) {
            for (int i = 0; i < listsScoped.getBody().size(); i++) {
                if (listsScoped.getBody().get(i).id == listId)
                    return true;
            }
        }
        return false;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}