package mmsolutions.im;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomFontTextView extends TextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontTextView(Context context) {
        super(context);
        applyCustomFont(context, null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", 0);

//        TextView textView = (TextView) findViewById(R.id.shopTitle);
//        SpannableString content = new SpannableString("Content");
//        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
//        textView.setText(content);

        setTypeface(Typeface.createFromAsset(context.getAssets(), "7fonts.ru_eurof55.ttf"));
    }
}