# Android-ExoPlayer
HLS and other media play by Google Exoplayer

### optional requirements
```
- Add configChanges property to Activity Manifest to prevent activity restart on Orientation Change
android:configChanges="keyboardHidden|orientation|screenSize"

```

## Import Library to Gradle

1 - Download xExoplayer.zip

https://cdn.xtsmm.com/android/libraries/Exoplayer.zip


2 - Import Module

```
File -> New -> Import Module -> select Downloaded "xExoplayer" Folder
```

3 - Add to App Gradle (path : Folder Name)

```
implementation(project(path: ':Exoplayer'))
```


## Usage
You can set UserAgent, one header key and value.
```
package com.xtsmm.android.androidexoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xtsmm.android.library.exoplayer.xExoPlayer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int currentApiVersion = Build.VERSION.SDK_INT;


        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
        FrameLayout fl=new FrameLayout(this);
        String userAgent = null;
        String headerKey = null;
        String headerValue = null;

        fl = new xExoPlayer(this).play("https://content.jwplatform.com/manifests/yp34SRmf.m3u8",fl,true,userAgent,headerKey,headerValue);
        setContentView(fl);
    }
}
```

