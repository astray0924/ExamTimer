package com.example.examtimer;

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
	private TextView tvTime;
	private CheckBox chk_extra_time;
	private Button btnStart;
	private Button btnReset;

	// attributes
	private static final int DEFAULT_TIME = 6000 * 10 * 10;
	private static final int EXTRA_TIME = 6000;
	private static final int FIRST_ALARM_TIME = 6000 * 2;
	private boolean isExtraMode = false;

	// Timer modes
	private enum TimerMode {
		DEFAULT, EXTRA
	};

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
		tvTime = (TextView) findViewById(R.id.time);
		chk_extra_time = (CheckBox) findViewById(R.id.extra_time);
		btnStart = (Button) findViewById(R.id.start);
		btnReset = (Button) findViewById(R.id.reset);

		btnStart.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		chk_extra_time.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class ExtraTimer extends CountDownTimer {
		private int seconds_left = 0;

		public ExtraTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			setTimerText(0);

			Toast.makeText(MainActivity.this, "시험 시작!", Toast.LENGTH_SHORT)
					.show();

			setTimerMode(TimerMode.DEFAULT);
			timer.start();

		}

		@Override
		public void onTick(long millisUntilFinished) {
			setTimerText(millisUntilFinished);
			// if (Math.round((float) millisUntilFinished / 1000.0f) !=
			// seconds_left) {
			// seconds_left = Math.round((float) millisUntilFinished / 1000.0f);
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

			notifySecondAlarm();

			Toast.makeText(MainActivity.this, "Finished!", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			setTimerText(millisUntilFinished);

			if (millisUntilFinished < FIRST_ALARM_TIME && !firstAlarmHandled) {
				notifyFirstAlarm();
				firstAlarmHandled = true;
			}
		}

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
			btnStart.setVisibility(View.GONE);
			btnReset.setVisibility(View.VISIBLE);

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
			btnStart.setVisibility(View.VISIBLE);
			btnReset.setVisibility(View.GONE);

			setExtraCheckBoxEnable(true);
			break;
		}

	}

	private void setTimerText(long milliseconds) {
		int minutes = milliToMinute(milliseconds);
		int seconds = milliToSec(milliseconds);
		String timeString = String.format("%02d:%02d", minutes, seconds);
		tvTime.setText(timeString);
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
