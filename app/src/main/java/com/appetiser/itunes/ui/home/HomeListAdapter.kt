package com.appetiser.itunes.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appetiser.itunes.R
import com.appetiser.itunes.data.Track
import com.appetiser.itunes.databinding.ListItemTrackBinding
import com.squareup.picasso.Picasso

class HomeListAdapter(
    private val _onItemClick: ((track: Track) -> Unit)? = null
) : PagedListAdapter<Track, HomeListAdapter.HomeListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track) =
                oldItem.trackId == newItem.trackId

            override fun areContentsTheSame(oldItem: Track, newItem: Track) =
                oldItem.isSelected == newItem.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_track, parent, false)
        return HomeListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        val track = getItem(position) ?: return
        val listItem = ListItemTrackBinding.bind(holder.itemView)

        Picasso.get()
            .load(track.artworkUrl100)
            .placeholder(R.drawable.twotone_broken_image_24)
            .error(R.drawable.twotone_broken_image_24)
            .into(listItem.imgArtWork);
        listItem.txtName.text = track.trackName ?: track.trackCensoredName
        listItem.txtGenre.text = track.primaryGenreName
        listItem.txtPrice.text = "Price: ${track.currency} ${track.trackPrice}"
        listItem.txtRentalPrice.text =
            if (track.trackRentalPrice > 0)
                "Rental Price: ${track.currency} ${track.trackRentalPrice}"
            else ""

        if (track.isSelected) {
            // View more details
            listItem.txtArtist.text = "By: ${track.artistName}"
            listItem.txtDescription.text =
                if (track.longDescription != null && track.longDescription.isNotBlank())
                    track.longDescription
                else track.shortDescription
            listItem.txtDescription.visibility =
                if (listItem.txtDescription.text.isNotBlank())
                    View.VISIBLE
                else View.GONE
            listItem.containerDetails.visibility = View.VISIBLE
        } else {
            // Normal view
            listItem.txtArtist.text = ""
            listItem.txtDescription.text = ""
            listItem.containerDetails.visibility = View.GONE
        }

        holder.itemView.tag = track

        if (!holder.itemView.hasOnClickListeners()) {
            holder.itemView.setOnClickListener {
                _onItemClick?.invoke(
                    // need to be cloned so that "oldItem" in DIFF_CALLBACK will not be affected
                    // in case there are modifications
                    (it.tag as Track).clone()
                )
            }
        }
    }

    class HomeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}