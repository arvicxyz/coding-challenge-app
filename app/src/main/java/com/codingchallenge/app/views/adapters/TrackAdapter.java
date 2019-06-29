package com.codingchallenge.app.views.adapters;

import android.content.Context;
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
            TrackModel notification = _tracksList.get(position);
            holder.setData(notification);
            holder.setListeners();
        } else {
            // TODO: Set empty notifications view
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
                Glide.with(_context)
                        .load(_track.getTrackArtworkUrl())
                        .encodeQuality(100)
                        .into(_trackArtwork);
            }

            // Name
            _trackName.setText(_track.getTrackName());

            // Genre
            _trackGenre.setText(_track.getTrackGenre());

            // Price
            _trackPrice.setText(MessageFormat.format("$ {0}", _track.getTrackPrice()));
        }

        public void setListeners() {
            _itemView.setOnClickListener(v -> {
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
