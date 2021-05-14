package com.example.forecastinator;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>
{
    Context ctx = new MainActivity();
    private ArrayList<ForecastItem> forecastItems;

    private OnItemClickListener fListener;


     public interface OnItemClickListener
     {
         void onItemClick(int position);
     }

     public void setOnItemCLickListener(OnItemClickListener listener)
     {

         fListener = listener;
     }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder
    {

        NetworkImageView ivIcon;
        TextView tvMaxTemp;
        TextView tvMinTemp;
        TextView tvStatus;
        TextView tvDate;
        Context ctx;

        public ForecastViewHolder(@NonNull View itemView, final OnItemClickListener listener)
        {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvMaxTemp = itemView.findViewById(R.id.tvMaxTemp);
            tvMinTemp = itemView.findViewById(R.id.tvMinTemp);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public ForecastAdapter(ArrayList<ForecastItem> forecasts)
    {
        forecastItems = forecasts;
    }


    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        ForecastViewHolder fvh = new ForecastViewHolder(v, fListener);

        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position)
    {
        ImageLoader imageLoader;
        ForecastItem currentItem = forecastItems.get(position);

        String url = currentItem.getfImageSource();
        imageLoader = CustomVolleyRequest.getInstance(ctx).getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(holder.ivIcon,R.drawable.ic_sun, android.R.drawable.ic_dialog_alert));

        holder.ivIcon.setImageUrl(url, imageLoader);

        holder.tvMaxTemp.setText(currentItem.getfMaxTemp());
        holder.tvMinTemp.setText(currentItem.getfMinTemp());
        holder.tvStatus.setText(currentItem.getfStatus());
        holder.tvDate.setText(currentItem.getfDate());
    }

    @Override
    public int getItemCount()
    {
        return forecastItems.size();
    }


}
