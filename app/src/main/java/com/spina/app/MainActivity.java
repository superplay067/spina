package com.spina.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Spinner modeSpinner;
    private EditText minutesInput;
    private View timerLayout;
    private Button startButton;
    private TextView statusText;

    private static final String[] MODES = {
            "Таймер",
            "Стандарт",
            "Гибрид"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationHelper.createChannel(this);

        modeSpinner = findViewById(R.id.modeSpinner);
        minutesInput = findViewById(R.id.minutesInput);
        timerLayout = findViewById(R.id.timerLayout);
        startButton = findViewById(R.id.startButton);
        statusText = findViewById(R.id.statusText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                MODES
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);

        modeSpinner.setSelection(SpinaPrefs.getMode(this));
        minutesInput.setText(String.valueOf(SpinaPrefs.getMinutes(this)));

        modeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                timerLayout.setVisibility(
                        position == SpinaPrefs.MODE_STANDARD ? View.GONE : View.VISIBLE
                );
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        startButton.setOnClickListener(v -> {
            if (SpinaPrefs.isActive(this)) {
                stopSpina();
            } else {
                startSpina();
            }
        });

        updateUi();
    }

    private void startSpina() {
        int mode = modeSpinner.getSelectedItemPosition();

        int minutes = 15;
        if (mode == SpinaPrefs.MODE_TIMER || mode == SpinaPrefs.MODE_HYBRID) {
            String raw = minutesInput.getText().toString().trim();
            try {
                minutes = Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                minutes = 0;
            }

            if (minutes < 1 || minutes > 1440) {
                minutesInput.setError("От 1 до 1440 минут");
                return;
            }
        }

        SpinaPrefs.setMode(this, mode);
        SpinaPrefs.setMinutes(this, minutes);
        SpinaPrefs.setActive(this, true);

        if (mode == SpinaPrefs.MODE_TIMER || mode == SpinaPrefs.MODE_HYBRID) {
            AlarmScheduler.schedule(this);
        }

        requestNotificationPermissionIfNeeded();
        updateUi();

        Toast.makeText(this, "Spina запущена", Toast.LENGTH_SHORT).show();
    }

    private void stopSpina() {
        SpinaPrefs.setActive(this, false);
        AlarmScheduler.cancel(this);
        updateUi();
        Toast.makeText(this, "Напоминания выключены", Toast.LENGTH_SHORT).show();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33 &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1001
            );
        }
    }

    private void updateUi() {
        boolean active = SpinaPrefs.isActive(this);
        startButton.setText(active ? "ОСТАНОВИТЬ" : "ЗАПУСТИТЬ");
        statusText.setText(active
                ? "Напоминания включены"
                : "Напоминания выключены");
    }
}
