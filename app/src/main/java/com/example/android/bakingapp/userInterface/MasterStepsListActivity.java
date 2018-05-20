package com.example.android.bakingapp.userInterface;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Recipe;

import static com.example.android.bakingapp.userInterface.RecipesList.EXTRA_RECIPE;

public class MasterStepsListActivity extends AppCompatActivity implements RecipeDetailsAdapter.OnClickListener{

    public static final String EXTRA_STEPS = "steps";
    public static final String EXTRA_STEP_INDEX = "step_index";
    public static final String INSTANCE_STEP_INDEX = "instance_step_index";

    int stepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_steps_list);

        if(getIntent().getExtras()!=null) {
            Recipe mRecipe = getIntent().getExtras().getParcelable(EXTRA_RECIPE);
            if(getSupportActionBar()!=null && mRecipe!=null)
                getSupportActionBar().setTitle(mRecipe.getName());
        }

        if(savedInstanceState !=null && savedInstanceState.containsKey(INSTANCE_STEP_INDEX))
        {
            stepIndex = savedInstanceState.getInt(INSTANCE_STEP_INDEX);
            onItemClick(stepIndex);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(INSTANCE_STEP_INDEX,stepIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(INSTANCE_STEP_INDEX,stepIndex);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onItemClick(int position) {

        stepIndex = position;

        if(getIntent().getExtras()!=null) {
            Recipe mRecipe = getIntent().getExtras().getParcelable(EXTRA_RECIPE);

            // If device is tablet, set isTwoPanel to true before continuing
            if(getResources().getBoolean(R.bool.isTablet))
            {
                StepDetailsFragment detailsFragment = new StepDetailsFragment();
                detailsFragment.isTwoPanel=true;
                if(mRecipe!=null)
                    detailsFragment.mStepsList = mRecipe.getStepList();
                detailsFragment.indexStep = position;

                if(findViewById(R.id.frame_player)==null) {
                    getFragmentManager()
                            .beginTransaction()
                            .add(R.id.frame_details,detailsFragment)
                            .commit();
                }else
                {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_details,detailsFragment)
                            .commit();
                }
            }else
            {
                Intent intent = new Intent(this,StepDetailsActivity.class);

                if(mRecipe!=null) {
                    intent.putExtra(EXTRA_STEPS, mRecipe.getStepList());
                    intent.putExtra(EXTRA_STEP_INDEX, position);
                }

                this.startActivity(intent);
            }
        }
    }
}
