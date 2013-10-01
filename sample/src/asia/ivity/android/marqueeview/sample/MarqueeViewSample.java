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

        final MarqueeView mv = (MarqueeView) findViewById(R.id.marqueeView100);
        mv.setPauseBetweenAnimations(500);
        mv.setSpeed(10);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mv.startMarquee();
            }
        });
        final MarqueeView mv150 = (MarqueeView) findViewById(R.id.marqueeView150);
        mv150.setPauseBetweenAnimations(1000);
        mv150.setSpeed(90);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mv150.startMarquee();
            }
        });
    }
}
