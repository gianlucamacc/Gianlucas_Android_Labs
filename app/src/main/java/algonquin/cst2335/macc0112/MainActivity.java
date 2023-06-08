package algonquin.cst2335.macc0112;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        EditText editEmail = findViewById(R.id.editEmail);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(clk-> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra("Email Address", editEmail.getText().toString());
            startActivity(nextPage);

        } );


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "The application is now visible on screen." );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "The application is now responding to user input." );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "The application no longer responds to user input." );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "Any memory used by the application is freed." );

    }

}
