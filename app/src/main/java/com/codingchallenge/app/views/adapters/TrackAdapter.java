package com.codingchallenge.app.views.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingchallenge.app.R;
import com.codingchallenge.app.models.TrackModel;
import com.codingchallenge.app.models.constants.ArtworkDimensions;
import com.codingchallenge.app.utils.ImageUtil;

import java.text.MessageFormat;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private final LayoutInflater _layoutInflater;
    private Context _context;
    private ItemClickListener _itemClickListener;

    private List<TrackModel> _tracksList;

    public TrackAdapter(@NonNull Context context) {
        _layoutInflater = LayoutInflater.from(context);
        _context = context;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = _layoutInflater.inflate(R.layout.row_track_item, parent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        if (_tracksList != null) {
            TrackModel track = _tracksList.get(position);
            holder.setData(track);
            holder.setListeners();
        }
    }

    @Override
    public int getItemCount() {
        if (_tracksList != null)
            return _tracksList.size();
        return 0;
    }

    public void setItemList(List<TrackModel> list) {
        _tracksList = list;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener clickListener) {
        _itemClickListener = clickListener;
    }

    // Adapter Getters and Setters

    private boolean isClickable = true;

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {

        private View _itemView;
        private TrackModel _track;

        private ImageView _trackArtwork;
        private TextView _trackName;
        private TextView _trackGenre;
        private TextView _trackPrice;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            _itemView = itemView;
            _trackArtwork = itemView.findViewById(R.id.trackArtwork);
            _trackName = itemView.findViewById(R.id.trackName);
            _trackGenre = itemView.findViewById(R.id.trackGenre);
            _trackPrice = itemView.findViewById(R.id.trackPrice);
        }

        public void setData(TrackModel track) {

            _track = track;

            // Artwork
            if (!TextUtils.isEmpty(_track.getTrackArtworkUrl())) {
                String artworkUrl = ImageUtil.getHighDefArtworkUrl(_track.getTrackArtworkUrl(), ArtworkDimensions.HI_DEF_MEDIUM);
                Glide.with(_context)
                        .load(artworkUrl)
                        .placeholder(R.drawable.img_default)
                        .encodeFormat(Bitmap.CompressFormat.WEBP)
                        .encodeQuality(100)
                        .centerCrop()
                        .into(_trackArtwork);
            }

            // Name
            String trackName = _track.getTrackName();
            if (!TextUtils.isEmpty(trackName)) {
                _trackName.setText(trackName);
            } else {
                _trackName.setText(_context.getString(R.string.not_available_text));
            }

            // Genre
            String trackGenre = _track.getTrackGenre();
            if (!TextUtils.isEmpty(trackGenre)) {
                _trackGenre.setText(trackGenre);
                _trackGenre.setVisibility(View.VISIBLE);
            } else {
                _trackGenre.setVisibility(View.GONE);
            }

            // Price
            float trackPrice = _track.getTrackPrice();
            if (trackPrice > 0) {
                _trackPrice.setText(MessageFormat.format("$ {0}", trackPrice));
            } else {
                _trackPrice.setText(_context.getString(R.string.free_text));
            }
        }

        public void setListeners() {
            _itemView.setOnClickListener(v -> {
                if (!isClickable)
                    return;
                if (_itemClickListener != null) {
                    _itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
