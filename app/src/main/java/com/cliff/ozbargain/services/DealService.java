package com.cliff.ozbargain.services;

import android.os.AsyncTask;
import android.widget.Toast;

import com.cliff.ozbargain.callbacks.DealsLoadedListener;
import com.cliff.ozbargain.core.OZBApplication;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.util.XmlService;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by Clifford on 27/11/2015.
 */
public class DealService extends JobService  implements DealsLoadedListener {
    private JobParameters parameters;
    @Override
    public boolean onStartJob(JobParameters params) {
        this.parameters=params;

        Toast.makeText(this, "Job started",Toast.LENGTH_SHORT).show();

        new DealsTask(this).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Toast.makeText(this, "Job stopped",Toast.LENGTH_SHORT).show();
        return false;
    }


    @Override
    public void onDealsLoaded(ArrayList<Deal> deals) {
        jobFinished(parameters,false);
    }
}
