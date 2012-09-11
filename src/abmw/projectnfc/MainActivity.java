package abmw.projectnfc;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        setContentView(R.layout.activity_main);
        
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
	        String value1 = extras.getString(Intent.EXTRA_TEXT);
	        
	        if (value1 != null) {
	        	TextView tv = (TextView) findViewById(R.id.txMain);
	        	tv.setText(value1);
        }}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
