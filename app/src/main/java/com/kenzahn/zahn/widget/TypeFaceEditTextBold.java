package com.kenzahn.zahn.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.kenzahn.zahn.R;
import com.kenzahn.zahn.utils.FontManager;

public class TypeFaceEditTextBold extends AppCompatEditText {
    public Context mContext;

    public TypeFaceEditTextBold(Context context) {
        super(context);
        this.mContext = context;
    }

    public TypeFaceEditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TypeFaceTextView, 0, 0);

        try {
            String typeface = ta.getString(R.styleable.TypeFaceTextView_TypeFace);
            String style = ta.getString(R.styleable.TypeFaceTextView_Style);
            if (typeface != null) {
                if (style != null && style.equals("bold")) {
                    FontManager.setFontTypeFace(this, FontManager.getTypeface(context, context.getString(R.string.font_bold)));
                } else if (style != null && style.equals("italic")) {
                    FontManager.setFontTypeFace(this, FontManager.getTypeface(context, typeface), Typeface.ITALIC);
                } else if (style != null && style.equals("bolditalic")) {
                    FontManager.setFontTypeFace(this, FontManager.getTypeface(context, typeface), Typeface.BOLD_ITALIC);
                } else {
                    FontManager.setFontTypeFace(this, FontManager.getTypeface(context, typeface));
                }
            } else {
                FontManager.setFontTypeFace(this, FontManager.getTypeface(context, context.getString(R.string.font_bold)));
            }
        } finally {
            ta.recycle();
        }
    }


}
