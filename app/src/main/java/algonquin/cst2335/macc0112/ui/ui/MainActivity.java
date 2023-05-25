package algonquin.cst2335.macc0112.ui.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import algonquin.cst2335.macc0112.databinding.ActivityMainBinding;
import algonquin.cst2335.macc0112.ui.data.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        TextView mytext = variableBinding.textview;
        Button btn = variableBinding.mybutton;
        EditText myedit = variableBinding.myedittext;

        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has: " + s);
        });

    }

}
