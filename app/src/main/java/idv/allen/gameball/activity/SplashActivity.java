package idv.allen.gameball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import idv.allen.gameball.MainActivity;
import idv.allen.gameball.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView gameballLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gameballLogo = (ImageView) findViewById(R.id.gameballLogo);

        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.gameball_splash);
        gameballLogo.startAnimation(myAnim);
        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread() {
            public void run () {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
