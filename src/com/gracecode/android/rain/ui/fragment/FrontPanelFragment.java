package com.gracecode.android.rain.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gracecode.android.common.helper.DateHelper;
import com.gracecode.android.common.helper.UIHelper;
import com.gracecode.android.rain.R;
import com.gracecode.android.rain.RainApplication;
import com.gracecode.android.rain.helper.SendBroadcastHelper;
import com.gracecode.android.rain.helper.StopPlayTimeoutHelper;
import com.gracecode.android.rain.helper.TypefaceHelper;
import com.gracecode.android.rain.helper.WeatherIconHelper;
import com.gracecode.android.rain.receiver.PlayBroadcastReceiver;
import com.gracecode.android.rain.request.WeatherRequest;
import com.gracecode.android.rain.ui.widget.SimplePanel;
import org.json.JSONException;
import org.json.JSONObject;

public class FrontPanelFragment extends PlayerFragment
        implements SimplePanel.SimplePanelListener, View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private ToggleButton mToggleButton;
    private SimplePanel mFrontPanel;
    private ToggleButton mPlayButton;
//    private TextView mHeadsetNeeded;
    private TextView mCountDownTextView;
    private TextView mWeatherTextView;

    private int mFocusPlayTime = 0;
    static private final int MAX_FOCUS_PLAY_TIMES = 12;

    private RainApplication mRainApplication;
    private MenuItem mPlayMenuItem;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences mPreferences;

    private RequestQueue mRequestQueue;
    private WeatherIconHelper mWeatherIconHelper;

    /**
     * 标记第一个打开应用
     */
    private Runnable mShowMainIntroRunnable = new Runnable() {
        private static final String PREF_IS_FIRST_RUN = "PREF_IS_FIRST_RUN";

        private boolean isFirstRun() {
            return mPreferences.getBoolean(PREF_IS_FIRST_RUN, true);
        }

        private void markNotFirstRun() {
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(PREF_IS_FIRST_RUN, false);
            editor.apply();
        }

        @Override
        public void run() {
            try {
                // 如果是首次启动，则显示提示信息框
                if (isFirstRun()) {
                    // @Todo
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                markNotFirstRun(); // 标记下次不再启动
            }
        }
    };


    /**
     * 标记第一次打开滑动面板
     */
    private Runnable mOpenPanelRunnable = new Runnable() {
        private static final String PREF_IS_FIRST_OPEN_PANEL = "PREF_IS_FIRST_OPEN_PANEL";

        private boolean isFirstOpenPanel() {
            return mPreferences.getBoolean(PREF_IS_FIRST_OPEN_PANEL, true);
        }

        private void markPanelOpened() {
            mPreferences.edit().putBoolean(PREF_IS_FIRST_OPEN_PANEL, false).apply();
        }

        @Override
        public void run() {
            // 第一次打开面板的时候，显示功能介绍
            if (isFirstOpenPanel()) {
                try {
                    // @TODO
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    markPanelOpened();  // 设定下次不再打开
                }
            }
        }
    };


    /**
     * 注册对应的广播事件
     */
    private BroadcastReceiver mBroadcastReceiver = new PlayBroadcastReceiver() {
        @Override
        public void onPlay() {
            setPlaying();
        }

        @Override
        public void onStop() {
            setStopped();
        }

        @Override
        public void onSetVolume(int track, float volume) {

        }

        @Override
        public void onSetPresets(float[] presets) {

        }

//        @Override
//        public void onHeadsetPlugged() {
//            setAsNormal();
//        }
//
//        @Override
//        public void onHeadsetUnPlugged() {
//            setHeadsetNeeded();
//            setStopped();
//        }

        @Override
        public void onPlayStopTimeout(long timeout, long remain, boolean byUser) {
            if (!byUser && remain != StopPlayTimeoutHelper.NO_REMAIN) {
                String countdown = DateHelper.getCountDownString(remain);
                if (mCountDownTextView.getVisibility() != View.VISIBLE) {
                    mCountDownTextView.setVisibility(View.VISIBLE);
                }
                mCountDownTextView.setText(countdown);
            } else {
                mCountDownTextView.setVisibility(View.INVISIBLE);
            }
        }
    };


    public void setPlayMenuItem(MenuItem item) {
        this.mPlayMenuItem = item;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRainApplication = RainApplication.getInstance();
        mSharedPreferences = mRainApplication.getSharedPreferences();
        mRequestQueue = mRainApplication.getRequestQueue();
        mPreferences = getActivity().getSharedPreferences(FrontPanelFragment.class.getName(), Context.MODE_PRIVATE);
    }


    /**
     * 自定义字体样式
     */
    private void setCustomFonts() {
        UIHelper.setCustomTypeface((ViewGroup) getView(), TypefaceHelper.getTypefaceMusket2(getActivity()));

        mWeatherTextView.setTypeface(TypefaceHelper.getTypefaceWeather(getActivity()));

        if (mToggleButton != null) {
            mToggleButton.setTypeface(TypefaceHelper.getTypefaceElegant(getActivity()));
        }

//        if (mHeadsetNeeded != null) {
//            mHeadsetNeeded.setTypeface(TypefaceHelper.getTypefaceElegant(getActivity()));
//        }

        if (mCountDownTextView != null) {
            mCountDownTextView.setTypeface(TypefaceHelper.getTypefaceMusket2(getActivity()));
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        mToggleButton.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);

        if (mRainApplication.isMeizuDevice()) {
            mPlayButton.setVisibility(View.INVISIBLE);
        }

//        mHeadsetNeeded.setOnClickListener(this);

        // The Panel is closed when initial launched.
        onClosed();

        // 设置自定义的字体
        setCustomFonts();

        // 初始化界面
//        setHeadsetNeeded();
        setAsNormal();

        mRequestQueue.start();
        mRequestQueue.add(mWeatherRequest);
    }


    /**
     * 请求当日的天气信息
     */
    private WeatherRequest mWeatherRequest = new WeatherRequest(new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONObject today = response.getJSONObject("today");
                mWeatherIconHelper.setWeather(today.getString("condition"));
                mWeatherIconHelper.show();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mWeatherIconHelper.show();
            }
        }

    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mWeatherIconHelper.show();
        }
    });


    @Override
    public void onStop() {
        super.onStop();
        mRequestQueue.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShowMainIntroRunnable.run();
    }

    public void setFrontPanel(SimplePanel panel) {
        this.mFrontPanel = panel;
    }

//    public void setHeadsetNeeded() {
//        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.headset_needed);
//        if (animation != null) {
//            mHeadsetNeeded.startAnimation(animation);
//        }
//
//        mPlayButton.setVisibility(View.INVISIBLE);
//        mHeadsetNeeded.setVisibility(View.VISIBLE);
//    }


    public void setAsNormal() {
//        mHeadsetNeeded.clearAnimation();
//        mHeadsetNeeded.setVisibility(View.INVISIBLE);
        if (!mRainApplication.isMeizuDevice()) {
            mPlayButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_front_panel, null);
        mWeatherTextView = (TextView) view.findViewById(R.id.icon);
        mWeatherIconHelper = new WeatherIconHelper(mWeatherTextView);
//        mHeadsetNeeded = (TextView) view.findViewById(R.id.headset_needed);
        mCountDownTextView = (TextView) view.findViewById(R.id.countdown);
        mPlayButton = (ToggleButton) view.findViewById(R.id.toggle_play);
        mToggleButton = (ToggleButton) view.findViewById(R.id.toggle_panel);
        return view;
    }


    @Override
    public void onOpened() {
        if (mToggleButton != null) {
            mToggleButton.setChecked(true);
        }

        mOpenPanelRunnable.run();
    }


    @Override
    public void onClosed() {
        if (mToggleButton != null) {
            mToggleButton.setChecked(false);
        }
    }


    @Override
    BroadcastReceiver getBroadcastReceiver() {
        return mBroadcastReceiver;
    }

    @Override
    public void setPlaying() {
        super.setPlaying();
        mPlayButton.setChecked(true);
        if (mPlayMenuItem != null) {
            mPlayMenuItem.setIcon(android.R.drawable.ic_media_pause);
        }
    }


    @Override
    public void setStopped() {
        super.setStopped();
        mPlayButton.setChecked(false);
        mCountDownTextView.setVisibility(View.INVISIBLE);
        if (mPlayMenuItem != null) {
            mPlayMenuItem.setIcon(android.R.drawable.ic_media_play);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggle_panel:
                if (mFrontPanel.isOpened()) {
                    mFrontPanel.close();
                } else {
                    mFrontPanel.open();
                }
                break;

            case R.id.toggle_play:
                togglePlay();
                break;

//            case R.id.headset_needed:
//                try {
//                    String message = getString(R.string.headset_needed);
//
//                    // 这里有个小的彩蛋，多点击耳机图标多次就可以解锁直接使用耳机外放播放
//                    if (mFocusPlayTime >= MAX_FOCUS_PLAY_TIMES) {
//                        markAsPlayWithoutHeadset();
//                        message = getString(R.string.play_wihout_headset);
//                    }
//
//                    UIHelper.showShortToast(getActivity(), message);
//                } finally {
//                    mFocusPlayTime++;
//                    setStopped();
//                }
//
//                break;
        }
    }

//    private void markAsPlayWithoutHeadset() {
//        mSharedPreferences.edit().putBoolean(PlayService.PREF_FOCUS_PLAY_WITHOUT_HEADSET, true).apply();
//    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        mPlayMenuItem = menuItem;
        togglePlay();
        return true;
    }

    public void togglePlay() {
        if (isPlaying()) {
            SendBroadcastHelper.sendStopBroadcast(getActivity());
        } else {
            SendBroadcastHelper.sendPlayBroadcast(getActivity());
        }
    }
}
