package com.kenzahn.zahn.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.expandview.ChildViewHolder;
import com.kenzahn.zahn.model.MenuSubItem;

public class MenuSubViewHolder extends ChildViewHolder {
    private final TextView mMoviesTextView;
    private final ImageView mIcon;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public MenuSubViewHolder(View itemView) {
        super(itemView);
        mMoviesTextView = itemView.findViewById(R.id.tv_movies);
        mIcon = itemView.findViewById(R.id.ivItemIcon) ;
    }
    public final void bind(MenuSubItem movies) {
        mMoviesTextView.setText(movies.getName());
        mIcon.setImageResource(movies.getmIcon());
    }
}
