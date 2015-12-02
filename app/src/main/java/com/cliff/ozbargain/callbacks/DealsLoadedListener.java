package com.cliff.ozbargain.callbacks;

import com.cliff.ozbargain.model.Deal;

import java.util.ArrayList;

/**
 * Created by Clifford on 1/12/2015.
 */
public interface DealsLoadedListener {
    public void onDealsLoaded(ArrayList<Deal> deals);
}
