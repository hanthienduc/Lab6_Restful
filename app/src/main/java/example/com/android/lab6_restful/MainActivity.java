package example.com.android.lab6_restful;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView uiUpdate;
    TextView jsonParsed;
    int sizeData = 0;
    EditText serverText;
    LongOperationTask operationTask;
    String serverURL = "http://192.168.80.1:8080/restful_webservice/JsonReturn.php";
    Button btnGetServerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiUpdate = (TextView) findViewById(R.id.output);
        jsonParsed = (TextView) findViewById(R.id.jsonParsed);
        serverText = (EditText) findViewById(R.id.serverText);
        btnGetServerData = (Button)findViewById(R.id.GetServerData);
        operationTask = new LongOperationTask(this, uiUpdate, jsonParsed);

        btnGetServerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationTask.execute(serverURL,serverText.getText().toString());
            }
        });

    }
}
