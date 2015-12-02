package com.cliff.ozbargain.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliff.ozbargain.ui.R;

/**
 * Created by Clifford on 2/12/2015.
 */
public class OZBItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView menuTitle;
    private ImageView menuIcon;

    public OZBItemViewHolder(View itemView) {
        super(itemView);
        menuTitle= (TextView) itemView.findViewById(R.id.menu_title);
        menuIcon= (ImageView) itemView.findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(this);
        menuTitle.setOnClickListener(this);
    }
    public TextView getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(TextView menuTitle) {
        this.menuTitle = menuTitle;
    }

    public ImageView getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(ImageView menuIcon) {
        this.menuIcon = menuIcon;
    }


    @Override
    public void onClick(View v) {


    }
}