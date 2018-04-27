package id.web.proditipolines.amop.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;

import id.web.proditipolines.amop.R;
import id.web.proditipolines.amop.util.Helper;

public class SplashActivity extends AppCompatActivity {

    Helper help;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        help = new Helper(getApplicationContext());

        avLoadingIndicatorView= (AVLoadingIndicatorView) findViewById(R.id.loading);
        avLoadingIndicatorView.smoothToShow();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                help.cekLogin();
                finish();
            }
            /*durasi 5000ms*/
        },2000);
    }

    @Override
    public void onBackPressed() {
    }
}
