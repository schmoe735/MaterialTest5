package com.cliff.ozbargain.tools;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.cliff.ozbargain.model.Deal;
import com.cliff.ozbargain.network.VolleyLoader;
import com.cliff.ozbargain.ui.R;
import com.cliff.ozbargain.util.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clifford on 26/11/2015.
 */
public class DealListAdapter extends RecyclerView.Adapter<DealListAdapter.DealItemViewHolder> {

    public static final String TAG = DealListAdapter.class.getSimpleName();
    private VolleyLoader volleyLoader;
    private ImageLoader imageLoader;

    private ArrayList<Deal> mDeals;
    private LayoutInflater layoutInflater;
    private Context mContext;
    public DealListAdapter(Context context) {
        this.mContext=context;
        layoutInflater=LayoutInflater.from(context);
        mDeals =new ArrayList<>();
        volleyLoader=VolleyLoader.getInstance();
        imageLoader=volleyLoader.getImageLoader();
    }
    public void setDeals(List<Deal> deals){
        this.mDeals.addAll(deals);
        notifyItemRangeChanged(0, deals.size());
    }



    @Override
    public DealItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.deal_item,parent,false);
        DealItemViewHolder viewHolder= new DealItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DealItemViewHolder holder, int position) {
        Deal currDeal = mDeals.get(position);
        holder.setDealTitle(currDeal.getTitle());
        holder.setDealDate(currDeal.getDate());
        holder.setDealRating(currDeal.getPosRating(), currDeal.getNegRating());
        holder.setDescription(currDeal.getDescription());
        if(currDeal.getImageUri()!=null){
            setImage(currDeal.getImageUri(), holder.getDealImage());
        }
    }

    private void setImage(String imageUri,final ImageView dealImage) {

        imageLoader.get(imageUri, new ImageLoader.ImageListener() {


            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                dealImage.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                L.d(TAG, "Error while loading images"+error.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    class DealItemViewHolder extends RecyclerView.ViewHolder{

        private ImageView dealImage;
        private TextView dealTitle;
        private TextView dealDate;
        private TextView posCounter;
        private TextView negCounter;
        private ImageView imagePreview;
        private TextView dealDesc;

        public DealItemViewHolder(final View itemView) {
            super(itemView);
            dealImage=((ImageView)itemView.findViewById(R.id.dealImage));
            dealTitle=((TextView)itemView.findViewById(R.id.dealTitle));
            dealDate=((TextView)itemView.findViewById(R.id.dealDate));
            posCounter=((TextView)itemView.findViewById(R.id.posCount));
            negCounter=((TextView)itemView.findViewById(R.id.negCount));
            imagePreview=(ImageView)(((FragmentActivity)mContext).findViewById(R.id.imagePreview));
            dealDesc=((TextView)itemView.findViewById(R.id.description));

            dealImage.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    ImageView thumbnailImg = (ImageView) v;

                    imagePreview.setImageDrawable(thumbnailImg.getDrawable());
                    imagePreview.setVisibility(View.VISIBLE);
                }
            });

        }

        public void setDealDate(String dealDate) {
            this.dealDate.setText(dealDate);
        }

        public void setDealTitle(String dealTitle) {
            if (dealTitle!=null){
                this.dealTitle.setText(dealTitle);
            }

        }
        public void setDescription(String desc){
            this.dealDesc.setText(Html.fromHtml(desc));
        }
        private ImageView getDealImage(){
            return this.dealImage;
        }


        public void setDealRating(int posRating, int negRating) {
            this.posCounter.setText(""+posRating);
            this.negCounter.setText(""+negRating);

        }

        public void setRating(float rating){

        }
    }

    public ArrayList<Deal> getDeals() {
        return mDeals;
    }
}
