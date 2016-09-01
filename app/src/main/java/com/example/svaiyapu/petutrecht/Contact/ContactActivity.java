package com.example.svaiyapu.petutrecht.Contact;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.GUIUtils;

public class ContactActivity extends AppCompatActivity {

    RelativeLayout mRlContainer;
    FloatingActionButton mFab;
    LinearLayout mLlContainer;
    ImageView mIvClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) &&
                (savedInstanceState == null)) {
            setupEnterAnimation();
            setupExitAnimation();
        } else {
            initViews();
        }
        mRlContainer = (RelativeLayout) findViewById(R.id.activity_contact_rl_container);
        mFab = (FloatingActionButton) findViewById(R.id.activity_contact_fab);
        mLlContainer = (LinearLayout) findViewById(R.id.activity_contact_ll_container);
        mIvClose = (ImageView) findViewById(R.id.activity_contact_iv_close);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        // start circular arc motion
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.changebounds_with_arcmotion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
				animateRevealShow();
            }
            @Override
            public void onTransitionCancel(Transition transition) {}
            @Override
            public void onTransitionPause(Transition transition) {}
            @Override
            public void onTransitionResume(Transition transition) {}
        });
    }

    // Start circular reveal
    private void animateRevealShow() {
        GUIUtils.animateRevealShow(this, mRlContainer, mFab,
                new GUIUtils.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {}

                    @Override
                    public void onRevealShow() {
                        initViews();
                    }
                });
    }

    private void initViews() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                animation.setDuration(300);
                mLlContainer.startAnimation(animation);
                mIvClose.startAnimation(animation);
                mLlContainer.setVisibility(View.VISIBLE);
                mIvClose.setVisibility(View.VISIBLE);
                mIvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GUIUtils.animateRevealHide(this, mRlContainer, mFab,
                    new GUIUtils.OnRevealAnimationListener() {
                        @Override
                        public void onRevealHide() {
                            backPressed();
                        }

                        @Override
                        public void onRevealShow() {
                        }
                    });
        } else {
            backPressed();
        }
    }

    private void backPressed() {
        super.onBackPressed();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupExitAnimation() {
        Fade fade = new Fade();
        getWindow().setReturnTransition(fade);
        fade.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

}
