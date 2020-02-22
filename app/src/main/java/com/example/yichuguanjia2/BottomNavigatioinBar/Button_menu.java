package com.example.yichuguanjia2.BottomNavigatioinBar;

import android.R.anim;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import androidx.appcompat.app.AppCompatActivity;
import com.example.yichuguanjia2.R;

public class Button_menu extends AppCompatActivity
{
    private Button buttonDelete, buttonMusic, buttonThought, buttonSleep;
    private Animation animationRotate;
    private static int width, height;
    private LayoutParams params = new LayoutParams(0, 0);
    private static Boolean isClick = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialButton();
    }
    private void initialButton()
    {
        Display display = getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
        Log.v("width  & height is:", width + ", " + height);

        params.height = 128;
        params.width = 128;
        //设置边距  (int left, int top, int right, int bottom)
        params.setMargins(0, 0, 0, 0);

        buttonSleep = findViewById(R.id.button_composer_sleep);
        buttonSleep.setLayoutParams(params);

        buttonThought = findViewById(R.id.button_composer_thought);
        buttonThought.setLayoutParams(params);

        buttonMusic = findViewById(R.id.button_composer_music);
        buttonMusic.setLayoutParams(params);

        buttonDelete = findViewById(R.id.btn_cancel);
        buttonDelete.setLayoutParams(params);

        buttonDelete.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(isClick == false)
                {
                    isClick = true;
                    buttonDelete.startAnimation(animRotate(45.0f, 0.5f, 0.45f));
                    buttonMusic.startAnimation(animTranslate(80.0f, -110.0f, 150, height - 180, buttonMusic, 140));
                    buttonThought.startAnimation(animTranslate(90.0f, -60.0f, 175, height - 135, buttonThought, 160));
                    buttonSleep.startAnimation(animTranslate(170.0f, -30.0f, 190, height - 90, buttonSleep, 180));
                }
                else
                {
                    isClick = false;
                    buttonDelete.startAnimation(animRotate(0.0f, 0.5f, 0.45f));
                    buttonMusic.startAnimation(animTranslate(-140.0f, 80.0f, 10, height - 98, buttonMusic, 120));
                    buttonThought.startAnimation(animTranslate(-160.0f, 40.0f, 10, height - 98, buttonThought, 80));
                    buttonSleep.startAnimation(animTranslate(-170.0f, 0.0f, 10, height - 98, buttonSleep, 50));

                }

            }
        });

        buttonMusic.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonMusic.startAnimation(setAnimScale(2.5f, 2.5f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
            }
        });
        buttonThought.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonThought.startAnimation(setAnimScale(2.5f, 2.5f));

                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonSleep.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
            }
        });
        buttonSleep.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                buttonSleep.startAnimation(setAnimScale(2.5f, 2.5f));

                buttonMusic.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonThought.startAnimation(setAnimScale(0.0f, 0.0f));
                buttonDelete.startAnimation(setAnimScale(0.0f, 0.0f));
            }
        });

    }

    protected Animation setAnimScale(float toX, float toY)
    {
        // TODO Auto-generated method stub
        Animation animationScale = new ScaleAnimation(1f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
        animationScale.setInterpolator(Button_menu.this, anim.accelerate_decelerate_interpolator);
        animationScale.setDuration(500);
        animationScale.setFillAfter(false);
        return animationScale;

    }

    protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue)
    {
        // TODO Auto-generated method stub
        animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        animationRotate.setAnimationListener(new AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                // TODO Auto-generated method stub
                animationRotate.setFillAfter(true);
            }
        });
        return animationRotate;
    }
    //移动的动画效果
    /*
     * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
     *
     * float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
     *
     * float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
     *
     * float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
     *
     * float toYDelta)这个参数表示动画开始的点离当前View Y坐标上的差值；
     */
    protected Animation animTranslate(float toX, float toY, final int lastX, final int lastY,
                                      final Button button, long durationMillis)
    {
        // TODO Auto-generated method stub
        Animation animationTranslate = new TranslateAnimation(0, toX, 0, toY);
        animationTranslate.setAnimationListener(new AnimationListener()
        {

            @Override
            public void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                // TODO Auto-generated method stub
                params = new LayoutParams(0, 0);
                params.height = 100;
                params.width = 100;
                params.setMargins(lastX, lastY, 0, 0);
                button.setLayoutParams(params);
                button.clearAnimation();

            }
        });
        animationTranslate.setDuration(durationMillis);
        return animationTranslate;
    }

}