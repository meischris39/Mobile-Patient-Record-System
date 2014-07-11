package com.example.mobilepatientrecord;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.example.mobilepatientrecord.JSONParser;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * 
 * @version 1.0
 * @author Chris Dixon
 * This is the main application class and is what a user is first presented with upon logging into
 * the application. It provides a log in system that is connected to a MySQL database which can 
 * authenticate and check a user's credentials to see if they are a valid user and if so allow 
 * them to further use the program. If wrong credentials are entered then the program will prompt
 * the user to re-enter credentials and will not allow them to progress.
 */

public class Login extends Activity implements OnClickListener{
			
	private EditText userIdText, passwordText;
	private JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	private static final String LOGIN_URL = "http://homepages.cs.ncl.ac.uk/c.dixon4/";
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button loginB = (Button) findViewById(R.id.loginButton);
		loginB.setOnClickListener(this);
		userIdText = (EditText) findViewById(R.id.userIdentificationBox);
		passwordText = (EditText) findViewById(R.id.passwordBox);
}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.loginButton){
			new AttemptLogin().execute();
			Log.d("TEST","Login Button Was Clicked");
		}
	}
								
	  class AttemptLogin extends AsyncTask<String, String, String> {
				 /**
		         * Before starting background thread Show Progress Dialog
		         * */
				boolean failure = false;

		        @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(Login.this);
		            pDialog.setMessage("Please wait..logging in");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }

				@Override
				protected String doInBackground(String... args) {
					// TODO Auto-generated method stub
					 // Check for success tag
		            int success;
		            String username = userIdText.getText().toString();
		            String password = passwordText.getText().toString();
		            try {
		                // Building Parameters
		                List<NameValuePair> params = new ArrayList<NameValuePair>();
		                params.add(new BasicNameValuePair("username", username));
		                params.add(new BasicNameValuePair("password", password));

		                Log.d("request!", "starting");
		                // getting product details by making HTTP request
		                JSONObject json = jsonParser.makeHttpRequest(
		                       LOGIN_URL, "POST", params);

		                // check your log for json response
		                Log.d("Login attempt", json.toString());

		                // json success tag
		                success = json.getInt(TAG_SUCCESS);
		                if(success == 0){
		                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
		                	return json.getString(TAG_MESSAGE);
		                }
		                if (success == 1) {
		                	Log.d("Login Successful!", json.toString());
		                    Intent i = new Intent(Login.this, SelectPatient.class);
		                	finish();
		    				startActivity(i);
		                	return json.getString(TAG_MESSAGE);
		                }		                
		            } catch (JSONException e) {
		            	Log.d("TEST", "Caught JSON Exception");
		                e.printStackTrace();
		            }
		            return null;

				}
				/**
		         * After completing background task Dismiss the progress dialog
		         * **/
		        protected void onPostExecute(String file_url) {
		            // dismiss the dialog once product deleted
		            pDialog.dismiss();
		            if (file_url != null){
		            	Toast.makeText(Login.this, file_url, Toast.LENGTH_SHORT).show();
		            }

		        }

			}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
	 