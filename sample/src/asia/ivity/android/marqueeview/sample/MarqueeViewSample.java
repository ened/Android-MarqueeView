package asia.ivity.android.marqueeview.sample;

import android.app.Activity;
import android.os.Bundle;
import asia.ivity.android.marqueeview.MarqueeView;

public class MarqueeViewSample extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Marquee #1: Configuration using code.
        final MarqueeView mv = (MarqueeView) findViewById(R.id.marqueeView100);
        mv.setPauseBetweenAnimations(500);
        mv.setSpeed(10);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mv.startMarquee();
            }
        });
    }
}
