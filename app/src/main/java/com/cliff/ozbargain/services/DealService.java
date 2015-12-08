package com.cliff.ozbargain.services;

import com.cliff.ozbargain.callbacks.DealsLoadedListener;
import com.cliff.ozbargain.core.OZBApplication;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.ui.R;
import com.cliff.ozbargain.util.L;

import java.util.ArrayList;

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

        L.m(this, "Job started");

        new DealsTask(this).execute(OZBApplication.getAppContext().getResources().getString(R.string.all_deals_url));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        L.m(this, "Job stopped");
        return false;
    }


    @Override
    public void onDealsLoaded(ArrayList<Deal> deals) {
        OZBApplication.getWritableDatabase().insertDeals(deals, false);
        jobFinished(parameters,false);
    }
}
