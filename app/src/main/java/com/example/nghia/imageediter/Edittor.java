package com.example.nghia.imageediter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.nghia.fragment.EffectsFilterFragment;
import com.example.nghia.fragment.fragment_sticker;
import com.example.nghia.sticker.StickerImageView;
import com.example.nghia.sticker.StickerTextView;
import com.example.nghia.utttilities.GLToolbox;
import com.example.nghia.utttilities.TextureRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class Edittor extends AppCompatActivity implements View.OnClickListener,GLSurfaceView.Renderer,
        EffectsFilterFragment.Send,fragment_sticker.Send{
    Toolbar toolbar;
    FrameLayout canvas;
    int index_sticker;
    Button btn_effect,btn_crop,btn_eraser,btn_sticker;
    static public  int mCurrentEffect;
    public GLSurfaceView mEffectView;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private TextureRenderer mTexRenderer = new TextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mInitialized = false;
    private volatile boolean saveFrame;
    FragmentManager fragmentManager= getFragmentManager();
    int itemData[] = {R.drawable.st1,
            R.drawable.st2,
            R.drawable.st3,
            R.drawable.st4,
            R.drawable.st5,
            R.drawable.st6,
            R.drawable.st7,
            R.drawable.st8,
            R.drawable.st9,
            R.drawable.st10,
            R.drawable.st11,
            R.drawable.st12,
            R.drawable.st13,
            R.drawable.st14,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittor);
        toolbar= (Toolbar) findViewById(R.id.toolBar_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tb_Title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        canvas = (FrameLayout) findViewById(R.id.frame_layout);

        btn_effect= (Button) findViewById(R.id.btn_effect);
        btn_sticker= (Button) findViewById(R.id.btn_sticker);
        btn_crop= (Button) findViewById(R.id.btn_crop);
        btn_eraser= (Button) findViewById(R.id.btn_eraser);
        btn_effect.setOnClickListener(this);
        btn_sticker.setOnClickListener(this);
        btn_crop.setOnClickListener(this);
        btn_eraser.setOnClickListener(this);

        mEffectView = (GLSurfaceView) findViewById(R.id.effectsview);
        mEffectView.setEGLContextClientVersion(2);
        mEffectView.setRenderer((GLSurfaceView.Renderer) this);
        mEffectView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_effect:
                FragmentTransaction fragmentTransaction1= fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.layout_fragment,new EffectsFilterFragment()).commit();
                break;
            case R.id.btn_sticker:
                FragmentTransaction fragmentTransaction2= fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.layout_fragment,new fragment_sticker()).commit();
                break;
            case R.id.btn_eraser:
                break;
            case R.id.btn_crop:
                break;
        }
    }

    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);
        Log.d("MY","Chay vao loadTextures");
        // Load input bitmap
        String su= getIntent().getStringExtra("pic");
        Uri uri= Uri.parse(su);
        Bitmap bitmap=null;
        try {
            bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();
        mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Set texture parameters
        GLToolbox.initTexParams();
        bitmap.recycle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        Log.d("MY","Chay vao onSurfaceChanged ");
        if (mTexRenderer != null) {
            mTexRenderer.updateViewSize(i, i1);
        }
    }
    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!mInitialized) {
            Log.d("MY","Chay vao onDrawFrame-1");
            // Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();
            mTexRenderer.init();
            loadTextures();
            mInitialized = true;
        }
        if (mCurrentEffect != 0) {
            Log.d("MY","Chay vao onDrawFrame-2");
            // if an effect is chosen initialize it and apply it to the texture
            initEffect();
            applyEffect();
        }
        renderResult();
    }

    public void renderResult() {

        if (mCurrentEffect != 0) {
            Log.d("MY","Chay vao renderResult-1");
            // if no effect is chosen, just render the original bitmap
            mTexRenderer.renderTexture(mTextures[1]);
        } else {
            Log.d("MY","Chay vao renderResult-2");
            saveFrame=true;
            // render the result of applyEffect()
            mTexRenderer.renderTexture(mTextures[0]);
        }
    }



    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }
    public void initEffect() {
        Log.d("MY","Chay vao day khoi tao Effect");
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        /**
         * Initialize the correct effect based on the selected menu/action item
         */
        switch (mCurrentEffect) {

            case 0:
                break;

            case 1:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", 0.5f);
                break;

            case 2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", .1f);
                mEffect.setParameter("white", .7f);
                break;

            case 3:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", 2.0f);
                break;

            case 4:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", 1.4f);
                break;

            case 5:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
                break;

            case 6:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
                break;

            case 7:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
                mEffect.setParameter("first_color", Color.YELLOW);
                mEffect.setParameter("second_color", Color.DKGRAY);
                break;

            case 8:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", .8f);
                break;

            case 9:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", .5f);
                break;

            case 10:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);
                break;

            case 11:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);
                break;

            case 12:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", 1.0f);
                break;

            case 13:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
                break;

            case 14:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
                break;

            case 15:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
                break;

            case 16:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_POSTERIZE);
                break;

            case 17:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", 180);
                break;

            case 18:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", .5f);
                break;

            case 19:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
                break;

            case 20:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
                break;

            case 21:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", .9f);
                break;

            case 22:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
                mEffect.setParameter("tint", Color.MAGENTA);
                break;

            case 23:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", .5f);
                break;

            default:
                break;

        }
    }

    public void applyEffect() {
        Log.d("MY","Chay vao day ap dụng Effect");
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);

    }

    @Override
    public void sendata(int x) {
        mCurrentEffect=x;
        Log.d("MY","ĐÃ nhận vị trí "+String.valueOf(mCurrentEffect));
        mEffectView.requestRender();
        Log.d("MY","Xet mCurrentEffect.requestRender ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        menu.add("Save Image")
                .setOnMenuItemClickListener(this.SaveImageClickListener)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_1:
                Toast.makeText(getBaseContext(),"Setting",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    MenuItem.OnMenuItemClickListener SaveImageClickListener = new MenuItem.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Bitmap bitmap;
            OutputStream output;

            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ef6);

            File filepath = Environment.getExternalStorageDirectory();

            File dir = new File(filepath.getAbsolutePath() + "/Save Image Example");
            dir.mkdirs();

            File file = new File(dir, "myimage1.png");

            Toast.makeText(Edittor.this, "Ảnh đã được lưu", Toast.LENGTH_SHORT).show();

            try {
                output = new FileOutputStream(file);

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                output.flush();
                output.close();

            }catch(Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    };

    @Override
    public void guidata(int x) {
        index_sticker=x;
        Log.d("MY","Nhận sticker "+index_sticker);
        StickerImageView iv_sticker = new StickerImageView(getBaseContext());
        iv_sticker.setImageDrawable(getResources().getDrawable(itemData[index_sticker]));
        canvas.addView(iv_sticker);
    }
}
