package com.guoliang.customview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


import java.io.IOException;

/**
 * Author:     ZhangGuoLiang
 * CreateDate: 2019/12/13 11:30
 * 视频播放器（带控制条）
 */
public class VideoPlayerView extends RelativeLayout {
    private SurfaceTexture surfaceTexture;
    public MediaPlayer mediaPlayer;

    private final int PLAYER_NORMAL = 1; // 普通播放器
    private final int PLAYER_FULL_SCREEN = 2; // 全屏播放器
    private int mPlayerState = PLAYER_NORMAL;
    private boolean isShowControlView = true;

    ViewGroup.LayoutParams containerLayoutParams;

    private int touchTime = 0;  //悬浮窗未触动5秒隐藏
    private Handler mHandler = new Handler();

    private Runnable runnableMedia = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int position = mediaPlayer.getCurrentPosition();
                seekBarVideo.setProgress(position);
                tvAlterationTime.setText(DateUtils.formatElapsedTime(position / 1000));
            }
            if (isShowControlView) {
                touchTime++;
                if (touchTime == 60) {
                    isShowControlView = false;
                    isShowVideoControl();
                }
            }
            mHandler.postDelayed(this, 50);
        }
    };
    private CheckBox cbVideoPlay;
    private TextureView videoTextureView;
    private LinearLayout llTitleView;
    private ImageView ivBack;
    private TextView tvTitle;
    private ConstraintLayout clControl;
    private TextView tvAlterationTime;
    private SeekBar seekBarVideo;
    private TextView tvFixationTime;
    private ImageView ivZoom;
    private Context mContext;
    private ViewGroup parentView;
    private String mVideoPath;
    private boolean is_control;
    private boolean is_Title;
    private boolean control_follow_video;

    public VideoPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayerView(@NonNull final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoPlayerView);
        is_control = typedArray.getBoolean(R.styleable.VideoPlayerView_is_control, true);
        is_Title = typedArray.getBoolean(R.styleable.VideoPlayerView_is_Title, true);
        control_follow_video = typedArray.getBoolean(R.styleable.VideoPlayerView_control_follow_video, false);
        typedArray.recycle();

        View.inflate(context, R.layout.view_video_player, this);
        videoTextureView = findViewById(R.id.video_textureView);
        llTitleView = findViewById(R.id.ll_title_view);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        cbVideoPlay = findViewById(R.id.cb_video_play);
        clControl = findViewById(R.id.cl_control);
        tvAlterationTime = findViewById(R.id.tv_alteration_time);
        seekBarVideo = findViewById(R.id.seek_bar_video);
        tvFixationTime = findViewById(R.id.tv_fixation_time);
        ivZoom = findViewById(R.id.iv_zoom);

        clControl.setVisibility(is_control ? VISIBLE : INVISIBLE);
        llTitleView.setVisibility(is_Title ? VISIBLE : INVISIBLE);

        videoTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            // SurfaceTexture缓冲大小变化
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                //适配低端手机跳转选择图片时回到app页面会导致surface失效所有要重新setSurface
                if (surfaceTexture == null || surfaceTexture != surface) {
                    surfaceTexture = surface;
                    if (mediaPlayer == null) {
                        if (!mVideoPath.isEmpty()) {
                            setVideoPath(mVideoPath);
                        }
                    } else {
                        mediaPlayer.setSurface(new Surface(surfaceTexture));
                    }
                } else {
                    videoTextureView.setSurfaceTexture(surfaceTexture);
                }
            }

            // SurfaceTexture通过updateImage更新
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            // SurfaceTexture即将被销毁
            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return surfaceTexture == null;
            }

            // SurfaceTexture准备就绪
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //当进度条停止拖动的时候，把媒体播放器的进度跳转到进度条对应的进度
                if (mediaPlayer != null) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mediaPlayer.seekTo(seekBar.getProgress(), MediaPlayer.SEEK_CLOSEST);
                    } else {
                        mediaPlayer.seekTo(seekBar.getProgress());
                    }
                    tvAlterationTime.setText(DateUtils.formatElapsedTime(seekBar.getProgress() / 1000));
                }
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowControlView = !isShowControlView;
                isShowVideoControl();
            }
        });
        cbVideoPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                }
            }
        });
        ivZoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exitFullScreen()) {
                    enterFullScreen();
                }
            }
        });
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exitFullScreen()) {
                    Activity activity = (Activity) context;
                    activity.finish();
                }
            }
        });
    }

    public void isVideoPlay(boolean isPlay) {
        cbVideoPlay.setChecked(isPlay);
    }

    public void setVideoPath(final String videoPath) {
        mVideoPath = videoPath;
        if (surfaceTexture == null) return;
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
            //预加载监听
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    tvFixationTime.setText(DateUtils.formatElapsedTime(mediaPlayer.getDuration() / 1000));
                    seekBarVideo.setMax(mediaPlayer.getDuration());
                    refreshTextureView();
                    //设置渲染画板
                    mediaPlayer.setSurface(new Surface(surfaceTexture));
                    mHandler.post(runnableMedia);
                    mediaPlayer.start();
                    if (onVideoPlayerViewListener!=null) {
                        onVideoPlayerViewListener.preloadCompleted();
                    }
                }
            });
            //播放完成监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (what == 100) {
                        setVideoPath(videoPath);
                    }
                    return false;
                }
            });
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();

            tvTitle.setText(videoPath.substring(videoPath.lastIndexOf("/") + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放全屏
     */
    private void enterFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) return;
        // 隐藏ActionBar、状态栏，并横屏
        NiceUtil.hideActionBar(mContext);
        NiceUtil.scanForActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        containerLayoutParams = getLayoutParams();
        parentView = (ViewGroup) getParent();
        parentView.removeView(this);
        ViewGroup contentView = NiceUtil.scanForActivity(mContext).findViewById(android.R.id.content);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        contentView.addView(this, params);
        post(new Runnable() {
            @Override
            public void run() {
                refreshTextureView();
                setBackgroundColor(Color.parseColor("#000000"));
                if (control_follow_video) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) clControl.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
                    //播放器控制器大小变化
                    clControl.setLayoutParams(layoutParams);
                }
                mPlayerState = PLAYER_FULL_SCREEN;
            }
        });
    }

    /**
     * 退出全屏
     */
    private boolean exitFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) {
            NiceUtil.showActionBar(mContext);
            NiceUtil.scanForActivity(mContext).setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewGroup contentView = NiceUtil.scanForActivity(mContext).findViewById(android.R.id.content);
            contentView.removeView(this);
            parentView.addView(this, containerLayoutParams);
            post(new Runnable() {
                @Override
                public void run() {
                    refreshTextureView();
                    setBackgroundColor(Color.parseColor("#00000000"));
                    if (control_follow_video) {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) clControl.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, videoTextureView.getId());
                        layoutParams.addRule(RelativeLayout.ALIGN_START, videoTextureView.getId());
                        layoutParams.addRule(RelativeLayout.ALIGN_END, videoTextureView.getId());
                        //播放器控制器大小变化
                        clControl.setLayoutParams(layoutParams);
                    }
                    mPlayerState = PLAYER_NORMAL;
                }
            });
            return true;
        }
        return false;
    }


    private void refreshTextureView() {
        float videoScale = Math.min((float) getWidth() / mediaPlayer.getVideoWidth(), (float) getHeight() / mediaPlayer.getVideoHeight());
        float videoWidth = mediaPlayer.getVideoWidth() * videoScale;
        float videoHeight = mediaPlayer.getVideoHeight() * videoScale;
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) videoTextureView.getLayoutParams();
        layoutParams.width = (int) videoWidth;
        layoutParams.height = (int) videoHeight;
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        videoTextureView.setLayoutParams(layoutParams);
    }

    private void isShowVideoControl() {
        if (isShowControlView) {
            cbVideoPlay.setVisibility(View.VISIBLE);
            clControl.setVisibility(is_control? View.VISIBLE: View.INVISIBLE);
            llTitleView.setVisibility(is_control? View.VISIBLE: View.INVISIBLE);
        } else {
            cbVideoPlay.setVisibility(View.INVISIBLE);
            clControl.setVisibility(View.INVISIBLE);
            llTitleView.setVisibility(View.INVISIBLE);
        }
        touchTime = 0;
    }

    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            mHandler.removeCallbacks(runnableMedia);
        }
    }

    public void pause() {
        cbVideoPlay.setChecked(false);
    }

    public boolean onBackPressedVideo() {
        if (mPlayerState == PLAYER_FULL_SCREEN) {
            exitFullScreen();
            return true;
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (((Activity) mContext).isFinishing()) {
            stopMediaPlayer();
        }
    }

    public void setOnVideoPlayerViewListener(OnVideoPlayerViewListener onVideoPlayerViewListener) {
        this.onVideoPlayerViewListener = onVideoPlayerViewListener;
    }

    private OnVideoPlayerViewListener onVideoPlayerViewListener;
    public interface OnVideoPlayerViewListener{
        void preloadCompleted();
    }
}
