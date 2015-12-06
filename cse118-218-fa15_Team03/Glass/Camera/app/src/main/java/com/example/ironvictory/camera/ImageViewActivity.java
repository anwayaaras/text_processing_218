package com.example.ironvictory.camera;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class AsyncHttpPost extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {

		HttpClient httpClient = new DefaultHttpClient();
		// replace with your url
		HttpPost httpPost = new HttpPost("http://810f898f.ngrok.io/alert?token=Nadir001&filename="+params[0].substring(0, params[0].lastIndexOf('.')));

		//Post Data
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("token", "Nadir001"));
		nameValuePair.add(new BasicNameValuePair("filename", params[0].substring(0, params[0].lastIndexOf('.'))));


		//Encoding POST data
//		try {
////			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
//			Log.d("HTTP", httpPost.getURI().toString());
//		} catch (UnsupportedEncodingException e) {
//			// log exception
//			e.printStackTrace();
//			Log.e("Http Post Response:", e.toString());
//		}

		//making POST request.
		try {
			HttpResponse response = httpClient.execute(httpPost);
			// write response to log
			Log.d("Http Post Response:", response.getParams().toString());
		} catch (ClientProtocolException e) {
			// Log exception
			e.printStackTrace();
			Log.e("Http Post Response:", e.toString());
		} catch (IOException e) {
			// Log exception
			e.printStackTrace();
			Log.e("Http Post Response:", e.toString());
		}
		return "";
	}
	@Override
	protected void onPostExecute(String result) {
		// something...
	}
}

public class ImageViewActivity extends Activity {
	public static String TAG = "ImageViewActivity";
	ImageView mImageview;
	private GestureDetector mGestureDetector;
	File mPictureFilePath;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.imageview);
		Bundle extras = getIntent().getExtras();
		mPictureFilePath = (File)extras.get("picturefilepath");

		Log.v(TAG, "pictureFilePath=" + mPictureFilePath.getAbsolutePath());
		mImageview =  (ImageView) findViewById(R.id.picture);

		Bitmap myBitmap = BitmapFactory.decodeFile(mPictureFilePath.getAbsolutePath());
		int h = (int) ( myBitmap.getHeight() * (640.0 / myBitmap.getWidth()) );

		Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 640, h, true);
		mImageview.setImageBitmap(scaled);        

		mGestureDetector = new GestureDetector(this);

		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.TAP) {
					Log.v(TAG, "TAP");
					openOptionsMenu();

					return true;
				} else if (gesture == Gesture.TWO_TAP) {
					Log.v(TAG, "TWO_TAP");
					return true;
				} else if (gesture == Gesture.SWIPE_RIGHT) {
					Log.v(TAG, "SWIPE_RIGHT");
					return true;
				} else if (gesture == Gesture.SWIPE_LEFT) {
					return true;
				} else if (gesture == Gesture.LONG_PRESS) {
					Log.v(TAG, "LONG_PRESS");					
					return true;
				} else if (gesture == Gesture.SWIPE_DOWN) {
					Log.v(TAG, "SWIPE_DOWN");
					return false;
				} else if (gesture == Gesture.SWIPE_UP) {
					Log.v(TAG, "SWIPE_UP");
					return true;
				} else if (gesture == Gesture.THREE_LONG_PRESS) {
					Log.v(TAG, "THREE_LONG_PRESS");
					return true;
				} else if (gesture == Gesture.THREE_TAP) {
					Log.v(TAG, "THREE_TAP");
					return true;
				} else if (gesture == Gesture.TWO_LONG_PRESS) {
					Log.v(TAG, "TWO_LONG_PRESS");
					return true;
				} else if (gesture == Gesture.TWO_SWIPE_DOWN) {
					Log.v(TAG, "TWO_SWIPE_DOWN");
					return false;
				} else if (gesture == Gesture.TWO_SWIPE_LEFT) {
					Log.v(TAG, "TWO_SWIPE_LEFT");
					return true;
				} else if (gesture == Gesture.TWO_SWIPE_RIGHT) {
					Log.v(TAG, "TWO_SWIPE_RIGHT");
					return true;
				} else if (gesture == Gesture.TWO_SWIPE_UP) {
					Log.v(TAG, "TWO_SWIPE_UP");
					return true;
				}

				return false;
			}
		});
	}



	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.imageview, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.upload:
			Uri imgUri = Uri.parse("file://" + mPictureFilePath.getAbsolutePath());
			Intent shareIntent = ShareCompat.IntentBuilder.from(this)
					.setText("Share image taken by Glass")
					.setType("image/jpeg")
					.setStream(imgUri )
					.getIntent()
					.setPackage("com.google.android.apps.docs");

			startActivity(shareIntent);
			AsyncHttpPost asyncHttpPost = new AsyncHttpPost();
			asyncHttpPost.execute(mPictureFilePath.getName());

			return true;

		case R.id.delete:
			mPictureFilePath.delete();
			Toast.makeText(ImageViewActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
			finish();
			return true;


		default:
			return super.onOptionsItemSelected(item);
		}
	}    	      

}
