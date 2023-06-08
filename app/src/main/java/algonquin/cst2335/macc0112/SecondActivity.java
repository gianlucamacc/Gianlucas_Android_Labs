package algonquin.cst2335.macc0112;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("Email Address");

        Button call_button = findViewById(R.id.call_button);
        Button changePicture = findViewById(R.id.change_picture);
        TextView textView = findViewById(R.id.textView);
        ImageView profileImage = findViewById(R.id.profileImage);
        textView.setText("Welcome back: " + emailAddress);

        EditText phoneNumber = findViewById(R.id.phoneNumber);



        call_button.setOnClickListener(clk->{
            String number = phoneNumber.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + number));
            startActivity(call);
        });


        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);


                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        changePicture.setOnClickListener(clk->{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });

        File file = new File( getFilesDir(), "Picture.png");
        if(file.exists()){
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage );

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String newPhoneNumber = phoneNumber.getText().toString();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", newPhoneNumber);
        editor.apply();
    }
}