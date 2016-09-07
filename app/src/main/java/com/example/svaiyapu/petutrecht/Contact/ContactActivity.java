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
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.GUIUtils;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.Util.PetUtil;

public class ContactActivity extends AppCompatActivity {

    RelativeLayout mRlContainer;
    FloatingActionButton mFab;
    LinearLayout mLlContainer;
    ImageView mCloseBtn;
    Button mSendMailButton;
    EditText mNameEditText;
    EditText mMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mRlContainer = (RelativeLayout) findViewById(R.id.activity_contact_container);
        mFab = (FloatingActionButton) findViewById(R.id.activity_contact_fab);
        mLlContainer = (LinearLayout) findViewById(R.id.activity_contact_ll_container);
        mCloseBtn = (ImageView) findViewById(R.id.activity_contact_close_btn);
        mSendMailButton = (Button) findViewById(R.id.activity_contact_send_btn);
        mNameEditText = (EditText) findViewById(R.id.etUsername);
        mMessageEditText = (EditText) findViewById(R.id.etMessage);

        final String petName = getIntent().getStringExtra(IntentUtil.DETAIL_TO_CONTACT_PET_NAME);

        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) &&
                (savedInstanceState == null)) {
            scheduleStartPostponedTransition(mFab);
            setupEnterAnimation();
            setupExitAnimation();
        } else {
            initViews();
        }

        if(savedInstanceState != null) {
            String name = savedInstanceState.getString("mNameEditText");
            String message = savedInstanceState.getString("mMessageEditText");
            mNameEditText.setText(name);
            mMessageEditText.setText(message);
        }

        mSendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                String message = mMessageEditText.getText().toString();
                PetUtil.composeEmail(view.getContext(), PetUtil.FAKE_PET_OWNER_MAIL, petName,
                        name, message);
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mNameEditText", mNameEditText.getText().toString());
        outState.putString("mMessageEditText", mMessageEditText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        // Postpone the shared element enter transition.
        postponeEnterTransition();
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
                // on the end of arc motion, start circular reveal
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
        // hop onto the main thread from a background thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                animation.setDuration(getApplicationContext().getResources()
                        .getInteger(android.R.integer.config_shortAnimTime));
                mLlContainer.startAnimation(animation);
                mCloseBtn.startAnimation(animation);
                mLlContainer.setVisibility(View.VISIBLE);
                mCloseBtn.setVisibility(View.VISIBLE);
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

    /**
     * Schedules the shared element transition to be started immediately
     * after the shared element has been measured and laid out within the
     * activity's view hierarchy.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

}
