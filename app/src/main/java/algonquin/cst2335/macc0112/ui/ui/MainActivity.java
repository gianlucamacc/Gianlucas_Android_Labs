package algonquin.cst2335.macc0112.ui.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


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

        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has: " + s);
        });

        model.isSelected.observe(this, selected -> {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);

            String message = "The value is now: " + selected;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                });
        variableBinding.checkBox.setOnCheckedChangeListener((checkBox, isChecked)-> {
            model.isSelected.postValue(variableBinding.checkBox.isChecked());
        });

        variableBinding.radioButton.setOnCheckedChangeListener((radioButton, isChecked)-> {
            model.isSelected.postValue(variableBinding.radioButton.isChecked());
        });

        variableBinding.switch1.setOnCheckedChangeListener((switch1, isChecked)-> {
            model.isSelected.postValue(variableBinding.switch1.isChecked());
        });

        variableBinding.imageView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "ImageView clicked", Toast.LENGTH_SHORT).show();
        });

        variableBinding.myimagebutton.setOnClickListener(view -> {
            int width = view.getWidth();
            int height = view.getHeight();
            String message = "The width = " + width + " and height = " + height;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }

}
