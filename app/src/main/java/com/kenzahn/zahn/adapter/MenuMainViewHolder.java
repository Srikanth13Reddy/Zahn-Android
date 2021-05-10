package com.kenzahn.zahn.adapter;

/**
 * Created by suresh on 10/7/2018.
 */

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.expandview.ParentViewHolder;
import com.kenzahn.zahn.model.MenuMainItem;

public class MenuMainViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    private final ImageView mArrowExpandImageView,ivItemIcon;
    public TextView ivItemTitle;

    public MenuMainViewHolder(View itemView) {
        super(itemView);
        ivItemTitle = (TextView) itemView.findViewById(R.id.ivItemTitle);

        mArrowExpandImageView = (ImageView) itemView.findViewById(R.id.iv_arrow_expand);
        ivItemIcon = (ImageView) itemView.findViewById(R.id.ivItemIcon);
    }

    public void bind(MenuMainItem movieCategory) {
        ivItemTitle.setText(movieCategory.getName());
        ivItemIcon.setImageResource(movieCategory.getIcon());
       /* mArrowExpandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapseView();
                } else {
                    expandView();
                }
            }
        });*/
        if(movieCategory.getChildItemList()==null){
            mArrowExpandImageView.setVisibility(View.GONE);
        }else{
            mArrowExpandImageView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);

        if (expanded) {
            mArrowExpandImageView.setRotation(ROTATED_POSITION);
        } else {
            mArrowExpandImageView.setRotation(INITIAL_POSITION);
        }

    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        mArrowExpandImageView.startAnimation(rotateAnimation);

    }
}