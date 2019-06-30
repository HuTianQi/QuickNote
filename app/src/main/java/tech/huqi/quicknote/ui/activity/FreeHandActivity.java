package tech.huqi.quicknote.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import tech.huqi.quicknote.R;
import tech.huqi.quicknote.config.QuickNote;
import tech.huqi.quicknote.core.view.DrawingBoardView;
import tech.huqi.quicknote.util.AttachmentHelper;
import tech.huqi.quicknote.util.BitmapUtil;
import tech.huqi.quicknote.util.DialogFactory;

import static tech.huqi.quicknote.config.Constants.INTENT_IMAGE_PATH;

/**
 * Created by hzhuqi on 2019/4/17
 */
public class FreeHandActivity extends BaseActivity {
    private ImageView mIvPaint;
    private ImageView mIvEraser;
    private ImageView mIvRevocation;
    private ImageView mIvRevert;
    private ImageView mIvClear;
    private DrawingBoardView mDrawingBoardView;
    private Drawable mPaintActiveDrawable;
    private Drawable mPaintUnActiveDrawable;
    private Drawable mEraserActiveDrawable;
    private Drawable mEraserUnActiveDrawable;
    private Drawable mRevocationActiveDrawable;
    private Drawable mRevertActiveDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freehand);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_freehand);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_done_white_24dp);
        initView();
        initClickListener();
        initData();
    }


    private void initView() {
        mIvPaint = findViewById(R.id.freehand_iv_paint);
        mIvEraser = findViewById(R.id.freehand_iv_eraser);
        mIvRevocation = findViewById(R.id.freehand_iv_revocation);
        mIvRevert = findViewById(R.id.freehand_iv_revert);
        mIvClear = findViewById(R.id.freehand_iv_clear);
        mDrawingBoardView = findViewById(R.id.draw_board);
    }

    private void initClickListener() {
        mIvPaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvPaint.setImageDrawable(mPaintActiveDrawable);
                mIvEraser.setImageDrawable(mEraserUnActiveDrawable);
                mDrawingBoardView.setDrawMode(DrawingBoardView.DRAW_MODE_PAINT);
            }
        });
        mIvEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvEraser.setImageDrawable(mEraserActiveDrawable);
                mIvPaint.setImageDrawable(mPaintUnActiveDrawable);
                mDrawingBoardView.setDrawMode(DrawingBoardView.DRAW_MODE_ERASER);
            }
        });
        mIvRevocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.revocation();
            }
        });
        mIvRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.revert();
            }
        });
        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFactory.createAndShowDialog(FreeHandActivity.this, QuickNote.getString(R.string.alert),
                        QuickNote.getString(R.string.clear_freehand_tip), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDrawingBoardView.clearAll();
                            }
                        });
            }
        });
        mDrawingBoardView.setOnContentChangedListener(new DrawingBoardView.OnContentChangedListener() {
            @Override
            public void onContentChanged() {
                if (mDrawingBoardView.getRevocationCount() > 0) { // 可恢复
                    mIvRevert.setImageDrawable(mRevertActiveDrawable);
                } else {
                    mIvRevert.setImageDrawable(getResources().getDrawable(R.drawable.ic_revert_unactivated_24dp));
                }
                if (mDrawingBoardView.getActivePathsCount() > 0) { // 可撤销
                    mIvRevocation.setImageDrawable(mRevocationActiveDrawable);
                } else {
                    mIvRevocation.setImageDrawable(getResources().getDrawable(R.drawable.ic_revocation_unactivated_24dp));
                }
            }
        });
    }

    private void initData() {
        mPaintActiveDrawable = getResources().getDrawable(R.drawable.ic_brush_activated_24dp);
        mPaintUnActiveDrawable = getResources().getDrawable(R.drawable.ic_brush_unactivated_24dp);
        mEraserActiveDrawable = getResources().getDrawable(R.drawable.ic_eraser_activated_24dp);
        mEraserUnActiveDrawable = getResources().getDrawable(R.drawable.ic_eraser_unactivated_24dp);
        mRevocationActiveDrawable = getResources().getDrawable(R.drawable.ic_revocation_activated_24dp);
        mRevertActiveDrawable = getResources().getDrawable(R.drawable.ic_revert_activated_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = getIntent();
                Bitmap bitmap = mDrawingBoardView.getBitmap();
                File image = AttachmentHelper.createNewAttachmentFile(this, "images", ".jpg");
                BitmapUtil.saveBitmap(bitmap, image);
                intent.putExtra(INTENT_IMAGE_PATH, image.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}
