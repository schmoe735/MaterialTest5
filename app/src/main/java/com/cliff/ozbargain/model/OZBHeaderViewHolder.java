package com.cliff.ozbargain.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cliff.ozbargain.ui.R;

/**
 * Created by Clifford on 2/12/2015.
 */
public class OZBHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView menuHeaderTitle;
    public OZBHeaderViewHolder(View itemView) {
        super(itemView);
        menuHeaderTitle = (TextView) itemView.findViewById(R.id.menu_title);
    }
    public TextView getMenuHeaderTitle() {
        return menuHeaderTitle;
    }

    public void setMenuHeaderTitle(TextView menuHeaderTitle) {
        this.menuHeaderTitle = menuHeaderTitle;
    }
}