package com.thrashplay.blittest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.thrashplay.luna.android.graphics.AndroidGraphics;
import com.thrashplay.luna.android.graphics.AndroidImageManager;
import com.thrashplay.luna.android.graphics.AndroidSpriteSheetConfigManager;
import com.thrashplay.luna.android.graphics.LunaSurfaceView;
import com.thrashplay.luna.api.graphics.Graphics;
import com.thrashplay.luna.api.graphics.ImageManager;
import com.thrashplay.luna.api.graphics.LunaImage;
import com.thrashplay.luna.engine.loop.Timing;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Add class documentation
 *
 * @author Sean Kleinjung
 */
public class MainActivity extends Activity {
    // FPS fields

    // the number of frames elapsed since the FPS was last updated
    private int frames = 0;
    // timer used to keep track of FPS update intervals
    private Timing timing = new Timing();
    // the current FPS
    private int fps = 0;

    private Rect source;
    private Rect destination;

    private ImageManager imageManager;
    private LunaImage spriteImage;
    private Bitmap spriteSheetBitmap;
    private Bitmap spriteBitmap;

    private LunaSurfaceView surfaceView;
    private PowerManager.WakeLock wakeLock;

    private volatile boolean running = false;
    private Thread thread;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageManager = new AndroidImageManager(getAssets(), new AndroidSpriteSheetConfigManager(getAssets()));
//        spriteImage = imageManager.createSpriteSheet("spritesheets/tile_spritesheet.json").getImage(1);
        spriteImage = imageManager.createImage("graphics/tile.png");

        source = new Rect(0, 0, 31, 31);
        destination = new Rect(0, 0, 0, 0);

        surfaceView = new LunaSurfaceView(this);
        surfaceView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setContentView(surfaceView);

        try {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            spriteBitmap = BitmapFactory.decodeStream(getAssets().open("graphics/tile.png"), null, options);

//            spriteSheetBitmap = BitmapFactory.decodeStream(getAssets().open("graphics/space_merc.png"));
            spriteSheetBitmap = BitmapFactory.decodeStream(getAssets().open("graphics/terrain_tiles.png"));
            spriteBitmap = Bitmap.createBitmap(spriteSheetBitmap, 0, 0, 32, 32);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + e.toString(), e);
        }

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "LunaGame");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onResume() {
        super.onResume();

        wakeLock.acquire();


        running = true;
        thread = new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                while (running) {
                    Graphics g = surfaceView.beginFrame();

                    if (g != null) {
                        Canvas canvas = ((AndroidGraphics) g).getCanvas();
                        System.out.println("fps hardwareaccelerated view = " + surfaceView.isHardwareAccelerated());
                        System.out.println("fps hardwareaccelerated canvas = " + canvas.isHardwareAccelerated());
                        for (int y = 0; y < canvas.getHeight(); y += 32) {
                            for (int x = 0; x < canvas.getWidth(); x += 32) {
//                                g.drawImage(spriteImage, x, y);

                                canvas.drawBitmap(spriteBitmap, x, y, null);

//                                destination.set(x, y, x + 31, y + 31);
//                                canvas.drawBitmap(spriteBitmap,
//                                        source,
//                                        destination,
//                                        null);

                            }
                        }
                    }

                    surfaceView.endFrame();

                    updateFps();
                }
            }
        });
        thread.start();
    }

    private void updateFps() {
        frames++;

        long elapsed = timing.elapsedAs(TimeUnit.MILLISECONDS);
        if (elapsed >= 1000) {
            fps = (int) Math.round(((double) frames / elapsed) * 1000);
            System.out.println("FPS: " + fps);
            frames = 0;
            timing.reset();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        wakeLock.release();

        running = false;
        while (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}