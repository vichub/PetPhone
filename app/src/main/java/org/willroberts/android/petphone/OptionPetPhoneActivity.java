package org.willroberts.android.petphone;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

@SuppressWarnings("unused")
public class OptionPetPhoneActivity extends Activity implements Button.OnClickListener{
	Button but1;
	CheckBox cb1;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
		Bundle starter = getIntent().getExtras();
		String vibs =  starter.getString("vibstat");
        cb1 = (CheckBox) findViewById(R.id.vibrate);
//        Toast.makeText(this,"vib is "+vibs+ " in Options",5000).show();
        if (vibs.equalsIgnoreCase("ON")){
        	cb1.setChecked(true);
        	cb1.setText("Clear check to turn vibrate OFF");
        }else{
        	cb1.setChecked(false);
        	cb1.setText("Check to turn vibrate ON");
        }
        but1 = (Button) findViewById(R.id.button1);
        but1.setOnClickListener(this);
    }
    @Override
    public void onClick(View arg0) {
    Intent data = new Intent();
    if (cb1.isChecked()){
    	data.putExtra("vibStatus", "ON");
    } else {
    	data.putExtra("vibStatus", "OFF");
    }
	setResult(RESULT_OK, data);
	super.finish();
    }
}