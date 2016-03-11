package com.thrashplay.saltar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import com.thrashplay.luna.android.engine.LunaGame;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MainActivity extends LunaGame {

//    private Rectangle gameBoardDimensions;
//    private TouchManager touchManager;
//    private SoundManager soundManager;
//
//    private Player lastPlayerToScore = null;
//    private int leftPlayerScore = 0;
//    private int rightPlayerScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                getScreenManager().setCurrentScreen(new TitleScreen(MainActivity.this));
            }
        });

//        touchManager = new TouchManager(getSurfaceView());
//        soundManager = new SoundManager(this);
    }
}
