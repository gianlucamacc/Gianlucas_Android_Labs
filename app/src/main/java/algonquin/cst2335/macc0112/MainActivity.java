package algonquin.cst2335.macc0112;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This page takes in a password for loggin in and validates it for certain criteria
 * @author gianl
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This holds the text in the center of the screen
     */
    private TextView tv = null;
    /**
     * This is the field that takes in the password
     */
    private EditText pass = null;
    /**
     * This is the button on the bottom of the phone screen that logs the user in
     */
    private Button login = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener( clk -> {
            String password = pass.getText().toString();

            boolean isComplex = checkPasswordComplexity(password, MainActivity.this);
            if (isComplex) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }
        });
    }

    /** This function's purpose is to check if the password contain the correct criteria to be used
     *
     * @param pw the String object that we are checking
     * @param context the context of the activity
     * @return Return true if password is complex enough
     */
    boolean checkPasswordComplexity(String pw, Context context) {
        boolean foundUpperCase = false;
        boolean foundLowerCase = false;
        boolean foundNumber = false;
        boolean foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);

            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            Toast.makeText(context, "Missing an uppercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(context, "Missing a lowercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundNumber) {
            Toast.makeText(context, "Missing a digit.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(context, "Missing a special character.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /** This function will sort the password and find if it has a special character
     *
     * @param c the character that is being filtered
     * @return return true is the string meets criteria
     */
    boolean isSpecialCharacter(char c) {
            switch(c)   {
                case '#':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '!':
                case '@':
                case '?':
                    return true;
                default:
                    return false;

            }
    }
}
