package com.example.android.camera2basic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.igenius.mqttservice.MQTTServiceCommand;
import net.igenius.mqttservice.MQTTServiceReceiver;

import java.io.UnsupportedEncodingException;

public class PrepareAndLoadSample extends Activity implements OnShowcaseEventListener {
    /** UI Elements */
    public Button placeSample;
    public Button ready;

    /** Showcase */
    public ShowcaseView sv;
    public RelativeLayout.LayoutParams lps;
    public ViewTarget target;
    public ViewTarget target2;

    /** Constants */
    public static String EXTRA_ACTIONS_TOPIC = "/extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Content */
        setContentView(R.layout.activity_prepare_and_load_sample);
        /** Orientation */
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /** UI elements */
        placeSample = (Button) findViewById(R.id.placeSample);
        ready = (Button) findViewById(R.id.ready);
        /** Initial states */
        placeSample.setEnabled(true);
        ready.setEnabled(false);
        /** Showcase */
        /** Show first button */
        /*sv = new ShowcaseView.Builder(PrepareAndLoadSample.this)
                .withMaterialShowcase()
                .setTarget(target)
                .setContentTitle("Prepare the sample")
                .setContentText("Press the button to take the stage out")
                .setStyle(R.style.CustomShowcaseTheme3)
                .setShowcaseEventListener(PrepareAndLoadSample.this)
                .replaceEndButton(R.layout.view_custom_button)
                .build();
        sv.setButtonPosition(lps);*/

        /** Callbacks */
        placeSample.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v) {
                /** Restart stage to its initial position */
                publishMessage(EXTRA_ACTIONS_TOPIC, "extra;home");
                ready.setEnabled(true);
                placeSample.setEnabled(false);
            }
        });

        ready.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v) {
                /** Once sample is prepared and locked, set to initial position */
                publishMessage(EXTRA_ACTIONS_TOPIC, "extra;startPosition");
                /** Start controller */
                /*Intent intent = new Intent(PrepareAndLoadSample.this, ControllerAndCamera.class);
                startActivity(intent);*/
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("pfm.improccameraautofocus", "pfm.improccameraautofocus.CameraAndController"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        /** Back operation is not allowed */
    }

    /** Showcase callbacks */
    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

    /** Publish a message
     * @param topic: input String that defines the target topic of the mqtt client
     * @param message: input String that contains a message to be published
     * @return no return
     * */
    public void publishMessage(String topic, String message) {
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = message.getBytes("UTF-8");
            MQTTServiceCommand.publish(PrepareAndLoadSample.this, topic, encodedPayload, 2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
