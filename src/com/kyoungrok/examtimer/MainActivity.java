package com.kyoungrok.examtimer;

import com.example.examtimer.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {
	private CountDownTimer timer;
	private TextView tv_time;
	private CheckBox chk_extra_time;
	private Button btn_start;
	private Button btn_reset;

	// attributes
	private static final int DEFAULT_TIME = 6000 * 10 * 10; // 10 minutes
	private static final int EXTRA_TIME = 6000 * 10 * 1; // 1 minutes
	private static final int FIRST_ALARM_TIME = 6000 * 10 * 2; // 2 minutes
	private boolean isExtraMode = false;

	// Timer modes
	private enum TimerMode {
		DEFAULT, EXTRA
	};

	private class ExtraTimer extends CountDownTimer {
		private int seconds_left = 0;
	
		public ExtraTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
	
		@Override
		public void onFinish() {
			setTimerText(0);
	
			// exam starts!!
			notifyExamStart();
	
			setTimerMode(TimerMode.DEFAULT);
			timer.start();
	
		}
	
		@Override
		public void onTick(long millisUntilFinished) {
			setTimerText(millisUntilFinished);
			// if (Math.round((float) millisUntilFinished / 1000.0f) !=
			// seconds_left) {
			// seconds_left = Math
			// .round((float) millisUntilFinished / 1000.0f);
			// setTimerText(seconds_left);
			// }
		}
	
	}

	private class DefaultTimer extends CountDownTimer {
		private boolean firstAlarmHandled = false;
	
		public DefaultTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
	
		}
	
		@Override
		public void onFinish() {
			setTimerText(0);
	
			// second alarm
			notifySecondAlarm();
	
			Toast.makeText(MainActivity.this, "Finished!", Toast.LENGTH_SHORT)
					.show();
		}
	
		@Override
		public void onTick(long millisUntilFinished) {
			setTimerText(millisUntilFinished);
	
			if (millisUntilFinished < FIRST_ALARM_TIME && !firstAlarmHandled) {
				// first alarm
				notifyFirstAlarm();
				firstAlarmHandled = true;
			}
		}
	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// UI 요소 초기화
		initUI();

		// 타이버 초기화
		setTimerMode(TimerMode.DEFAULT);
	}

	private void initUI() {
		tv_time = (TextView) findViewById(R.id.time);
		chk_extra_time = (CheckBox) findViewById(R.id.extra_time);
		btn_start = (Button) findViewById(R.id.start);
		btn_reset = (Button) findViewById(R.id.reset);

		btn_start.setOnClickListener(this);
		btn_reset.setOnClickListener(this);
		chk_extra_time.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void setTimerMode(TimerMode mode) {
		switch (mode) {
		case DEFAULT:
			timer = new DefaultTimer(DEFAULT_TIME, 100);
			setTimerText(DEFAULT_TIME);
			break;
		case EXTRA:
			timer = new ExtraTimer(EXTRA_TIME, 100);
			setTimerText(EXTRA_TIME);
			break;
		}
	}

	private void notifyExamStart() {
		Toast.makeText(MainActivity.this, "시험 시작!", Toast.LENGTH_SHORT).show();
	}

	private void notifyFirstAlarm() {
		Toast.makeText(MainActivity.this, "첫번째 알람!", Toast.LENGTH_SHORT).show();
	}

	private void notifySecondAlarm() {
		Toast.makeText(MainActivity.this, "두번째 알람!", Toast.LENGTH_SHORT).show();
	}

	private int milliToMinute(long milliseconds) {
		return (int) ((milliseconds / (1000 * 60)) % 60);
	}

	private int milliToSec(long milliseconds) {
		return (int) (milliseconds / 1000) % 60;
	}

	private void setExtraCheckBoxEnable(boolean enable) {
		chk_extra_time.setEnabled(enable);
	}

	private void setTimerText(long milliseconds) {
		int minutes = milliToMinute(milliseconds);
		int seconds = milliToSec(milliseconds);
		String timeString = String.format("%02d:%02d", minutes, seconds);
		tv_time.setText(timeString);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.start:
			// 설정된 모드에 따라서 타이머 초기화
			if (isExtraMode) {
				setTimerMode(TimerMode.EXTRA);
			} else {
				setTimerMode(TimerMode.DEFAULT);
			}

			timer.start();

			// toggle the buttons' visibility
			btn_start.setVisibility(View.GONE);
			btn_reset.setVisibility(View.VISIBLE);

			// 상태 변화 반영
			setExtraCheckBoxEnable(false);
			break;
		case R.id.reset:
			if (timer != null) {
				timer.cancel();
			}

			// 선택된 모드에 따라서 타이머 초기화
			if (isExtraMode) {
				setTimerMode(TimerMode.EXTRA);
			} else {
				setTimerMode(TimerMode.DEFAULT);
			}

			// toggle the buttons' visibility
			btn_start.setVisibility(View.VISIBLE);
			btn_reset.setVisibility(View.GONE);

			setExtraCheckBoxEnable(true);
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isExtraMode = isChecked;

		if (isExtraMode) {
			setTimerMode(TimerMode.EXTRA);
		} else {
			setTimerMode(TimerMode.DEFAULT);
		}
	}

}
