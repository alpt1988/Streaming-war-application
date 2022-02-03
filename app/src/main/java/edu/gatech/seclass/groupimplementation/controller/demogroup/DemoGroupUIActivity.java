package edu.gatech.seclass.groupimplementation.controller.demogroup;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import edu.gatech.seclass.groupimplementation.R;
import edu.gatech.seclass.groupimplementation.controller.event.EventUIActivity;
import edu.gatech.seclass.groupimplementation.model.db.GlobalRoomDatabase;
import edu.gatech.seclass.groupimplementation.model.demo.DemoGroup;
import edu.gatech.seclass.groupimplementation.model.event.Event;

public class DemoGroupUIActivity extends AppCompatActivity {
    private Button m_demoDetailsButton;
    private Button m_demoWatchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_event);

        m_demoDetailsButton = (Button) findViewById(R.id.demoinfo);
        m_demoDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DemoGroupUIActivity.this, DemoDetailsActivity.class));
            }
        });

        m_demoWatchButton = (Button) findViewById(R.id.watchevent);
        m_demoWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DemoGroupUIActivity.this, WatchEventActivity.class));
            }
        });
    }

}
