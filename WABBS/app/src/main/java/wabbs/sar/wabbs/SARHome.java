package wabbs.sar.wabbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SARHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sarhome);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("Hello World");
    }

        public void SmileFace(View view)
        {
            Intent intent = new Intent(this, SmilingFace.class);
           startActivity(intent);

        }

    public void ColorIdentifier(View view)
    {
        Intent intent = new Intent(this, ColorIdentifier.class);
        startActivity(intent);

    }
}
