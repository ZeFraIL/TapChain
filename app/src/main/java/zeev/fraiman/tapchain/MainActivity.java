package zeev.fraiman.tapchain;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int level = 1;
    private int n = 5;
    private int k;
    private int nextNumber;
    private final ArrayList<Integer> numbers = new ArrayList<>();
    private final ArrayList<TextView> numberViews = new ArrayList<>();
    private RelativeLayout mainLayout;
    private TextView levelTextView;
    private final Random random = new Random();
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main);
        levelTextView = findViewById(R.id.levelTextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            v.post(this::startLevel);
            return insets;
        });
    }

    private void startLevel() {
        if (n > 50) {
            Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        k = random.nextInt(100) + 1;
        nextNumber = k;

        levelTextView.setText("Level: " + level);

        for (TextView tv : numberViews) {
            mainLayout.removeView(tv);
        }
        numberViews.clear();

        numbers.clear();
        for (int i = 0; i < n; i++) {
            numbers.add(k + i);
        }
        Collections.shuffle(numbers);

        for (int number : numbers) {
            createNumberView(number);
        }
    }

    private void createNumberView(int number) {
        TextView numberView = new TextView(this);
        numberView.setText(String.valueOf(number));
        numberView.setTextSize(24);
        numberView.setTextColor(Color.WHITE);
        numberView.setGravity(Gravity.CENTER);
        numberView.setBackgroundResource(R.drawable.circle_background);
        numberView.setOnClickListener(v -> onNumberClick(number, (TextView) v));
        numberViews.add(numberView);

        int sizeInDp = 64;
        float scale = getResources().getDisplayMetrics().density;
        int sizeInPixels = (int) (sizeInDp * scale + 0.5f);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                sizeInPixels,
                sizeInPixels
        );

        int layoutWidth = mainLayout.getWidth() > 0 ? mainLayout.getWidth() : getResources().getDisplayMetrics().widthPixels;
        int layoutHeight = mainLayout.getHeight() > 0 ? mainLayout.getHeight() : getResources().getDisplayMetrics().heightPixels;

        int xMax = layoutWidth - mainLayout.getPaddingLeft() - mainLayout.getPaddingRight() - sizeInPixels;
        int yMax = layoutHeight - mainLayout.getPaddingTop() - mainLayout.getPaddingBottom() - sizeInPixels;

        params.leftMargin = mainLayout.getPaddingLeft() + (xMax > 0 ? random.nextInt(xMax) : 0);
        params.topMargin = mainLayout.getPaddingTop() + (yMax > 0 ? random.nextInt(yMax) : 0);

        numberView.setLayoutParams(params);
        mainLayout.addView(numberView);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(300);
        numberView.startAnimation(fadeIn);
    }

    private void onNumberClick(int number, TextView numberView) {
        if (number == nextNumber) {
            numberView.setClickable(false);
            nextNumber++;

            AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
            fadeOut.setDuration(300);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    numberView.setVisibility(View.INVISIBLE);
                    if (nextNumber == k + n) {
                        level++;
                        n++;
                        Toast.makeText(MainActivity.this, "Next Level!", Toast.LENGTH_SHORT).show();
                        startLevel();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            numberView.startAnimation(fadeOut);

        } else {
            flashErrorAndRestart();
        }
    }

    private void flashErrorAndRestart() {
        for (TextView tv : numberViews) {
            tv.setClickable(false);
            if (tv.getVisibility() == View.VISIBLE) {
                tv.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            }
        }

        handler.postDelayed(this::startLevel, 500);
    }
}
