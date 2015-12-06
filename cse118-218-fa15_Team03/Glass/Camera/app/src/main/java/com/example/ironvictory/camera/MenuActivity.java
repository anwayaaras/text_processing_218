package com.example.ironvictory.camera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends Activity {
    private static final String TAG = "MenuActivity";
    private boolean isActivityActive = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isActivityActive = true;
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Open the options menu right away.
        openOptionsMenu();
    }

//    @Override
//    protected void onPause() {
//        isActivityActive = false;
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        isActivityActive = true;
//        super.onResume();
//        if (!isFinishing()) {
//            openOptionsMenu();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop:
                stopService(new Intent(this, AppService.class));
                return true;
                
            case R.id.preview:
            	Intent intent = new Intent(this, MyPreviewActivity.class);
                startActivity(intent);  
                return true;
           
            case R.id.zoom:
            	Intent intent2 = new Intent(this, MyZoomActivity.class);
                startActivity(intent2);            	
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        finish();

    }
       
}
