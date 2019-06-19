package com.sahil.bakingtut;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.sahil.bakingtut.Model.Step;

import java.io.Serializable;
import java.util.List;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {List<Step> steps;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            Intent intent=getIntent();
            Bundle arguments =intent.getBundleExtra(Constants.BUNDLE_KEY);
            steps= (List<Step>) arguments.getSerializable(Constants.STEP_LIST_ACTIVITY_EXTRA_KEY);
            pos=arguments.getInt(Constants.POSITION_KEY,0);
            arguments.putSerializable(Constants.STEP_LIST_ACTIVITY_EXTRA_KEY,(Serializable) steps);
            arguments.putInt(Constants.POSITION_KEY,pos);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
            return true;
        }
        return true;
    }
}
