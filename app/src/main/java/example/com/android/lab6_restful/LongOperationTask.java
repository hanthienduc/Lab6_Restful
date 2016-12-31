package example.com.android.lab6_restful;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Han on 31/12/2016.
 */

public class LongOperationTask extends AsyncTask<String, Void, String> {

    Context context;
    private String Content;
    private String Error = null;
    private ProgressDialog Dialog;
    String data = "";
    TextView uiUpdate, jsonParsed;
    String OutputData = "";

    public LongOperationTask(Context context, TextView uiUpdate, TextView jsonParsed) {
        this.context = context;
        this.uiUpdate = uiUpdate;
        this.jsonParsed = jsonParsed;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Dialog = new ProgressDialog(context);
        Dialog.setMessage("Please wait..");
        Dialog.show();


    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            // Set Request parameter
            data += "&" + URLEncoder.encode("data", "UTF-8") + "=" + strings[1];

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {

            // Defined URL  where to send data
            URL url = new URL(strings[0]);

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "");
            }
            // Append Server Response To Content String
            Content = sb.toString();

            /****************** Start Parse Response JSON Data *************/

            JSONObject jsonResponse;


            /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            jsonResponse = new JSONObject(Content);

            /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
            /*******  Returns null otherwise.  *******/
            JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

            /*********** Process each JSON Node ************/

            int lengthJsonArr = jsonMainNode.length();

            for (int i = 0; i < lengthJsonArr; i++) {
                /****** Get Object for each JSON node.***********/
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                /******* Fetch node values **********/
                String name = jsonChildNode.optString("name").toString();
                String number = jsonChildNode.optString("number").toString();
                String date_added = jsonChildNode.optString("date_added").toString();


                OutputData += "Name          : " + name + "\n\n"
                        + "Number      : " + number + "\n\n"
                        + "Time           : " + date_added + "\n\n"
                        + "--------------------------------------------------\n\n";

            }

            /****************** End Parse Response JSON Data *************/


        } catch (Exception ex) {
            Error = ex.getMessage();
        } finally {
            try {

                reader.close();
            } catch (Exception ex) {
            }
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // Close progress dialog
        Dialog.dismiss();

        if (Error != null) {

            uiUpdate.setText("Output : " + Error);

        } else {

            // Show Response Json On Screen (activity)
            uiUpdate.setText(Content);

            //Show Parsed Output on screen (activity)
            jsonParsed.setText(OutputData);


        }
    }
}
