package com.cliff.ozbargain.tools;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cliff.ozbargain.model.MenuItem;
import com.cliff.ozbargain.model.OZBHeaderViewHolder;
import com.cliff.ozbargain.model.OZBItemViewHolder;
import com.cliff.ozbargain.ui.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Clifford on 20/11/2015.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private LayoutInflater mInflater;
    private List<MenuItem> menuItems= Collections.emptyList();
    private Context mContext;

    public MenuAdapter(Context context,List<MenuItem> menu) {
        this.mInflater = LayoutInflater.from(context);
        this.menuItems =menu;
        this.mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("MenuAdapter","onCreateViewHolder called "+ viewType);
        if (viewType==TYPE_HEADER){
            View view = mInflater.inflate(R.layout.ozb_menu_header, parent, false);
            RecyclerView.ViewHolder holder = new OZBHeaderViewHolder(view);
            return holder;
        }else {
            View view = mInflater.inflate(R.layout.ozb_menu_row, parent, false);
            RecyclerView.ViewHolder holder = new OZBItemViewHolder(view);
            return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position==0?TYPE_HEADER:TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return menuItems.size()+1;
    }

    public void delete(int position){
        menuItems.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("MenuAdapter", "onBindView called " + position);
        if (holder instanceof  OZBItemViewHolder) {
            OZBItemViewHolder itemViewHolder = (OZBItemViewHolder) holder;
            MenuItem currentItem = menuItems.get(position-1);
            itemViewHolder.getMenuTitle().setText(currentItem.getTitle());
            itemViewHolder.getMenuIcon().setImageResource(currentItem.getIconId());
        }
    }





}
