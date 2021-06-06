package com.apachat.fontawesome.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;

public class FontDrawable extends Drawable {
  private static final int SANS = 1;
  private static final int SERIF = 2;
  private static final int MONOSPACE = 3;
  private static final int[] themeAttributes = {
    android.R.attr.textAppearance
  };
  private static final int[] appearanceAttributes = {
    android.R.attr.textSize,
    android.R.attr.typeface,
    android.R.attr.textStyle,
    android.R.attr.textColor
  };
  private Resources mResources;
  private TextPaint mTextPaint;
  private StaticLayout mTextLayout;
  private Layout.Alignment mTextAlignment = Layout.Alignment.ALIGN_NORMAL;
  private Path mTextPath;
  private ColorStateList mTextColors;
  private Rect mTextBounds;
  private CharSequence mText = "";

  public FontDrawable(Context context, int faIconRes, boolean isSolid, boolean isBrand) {
    super();
    init(context, faIconRes, isSolid, isBrand);
  }

  private void init(Context context, int faIconRes, boolean isSolid, boolean isBrand) {
    mResources = context.getResources();
    mTextBounds = new Rect();
    mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.density = mResources.getDisplayMetrics().density;
    mTextPaint.setDither(true);

    int textSize = 15;
    ColorStateList textColor = null;
    int styleIndex = -1;
    int typefaceIndex = -1;

    TypedArray a = context.getTheme().obtainStyledAttributes(themeAttributes);
    int appearanceId = a.getResourceId(0, -1);
    a.recycle();

    TypedArray ap = null;
    if (appearanceId != -1) {
      ap = context.obtainStyledAttributes(appearanceId, appearanceAttributes);
    }
    if (ap != null) {
      for (int i = 0; i < ap.getIndexCount(); i++) {
        int attr = ap.getIndex(i);
        switch (attr) {
          case 0: //Text Size
            textSize = a.getDimensionPixelSize(attr, textSize);
            break;
          case 1: //Typeface
            typefaceIndex = a.getInt(attr, typefaceIndex);
            break;
          case 2: //Text Style
            styleIndex = a.getInt(attr, styleIndex);
            break;
          case 3: //Text Color
            textColor = a.getColorStateList(attr);
            break;
          default:
            break;
        }
      }

      ap.recycle();
    }

    setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF000000));
    setRawTextSize(textSize);

    Typeface tf;
    if (isSolid) {
      tf = FontCache.get(context, FontCache.FA_FONT_SOLID);
    } else if (isBrand) {
      tf = FontCache.get(context, FontCache.FA_FONT_BRANDS);
    } else {
      tf = FontCache.get(context, FontCache.FA_FONT_REGULAR);
    }
    setTypeface(tf, styleIndex);
    setText(context.getString(faIconRes));
  }

  public CharSequence getText() {
    return mText;
  }

  public void setText(CharSequence text) {
    if (text == null) text = "";
    mText = text;
    measureContent();
  }

  public float getTextSize() {
    return mTextPaint.getTextSize();
  }

  public void setTextSize(float size) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
  }

  public void setTextSize(int unit, float size) {
    float dimension = TypedValue.applyDimension(unit, size,
      mResources.getDisplayMetrics());
    setRawTextSize(dimension);
  }

  private void setRawTextSize(float size) {
    if (size != mTextPaint.getTextSize()) {
      mTextPaint.setTextSize(size);
      measureContent();
    }
  }

  public float getTextScaleX() {
    return mTextPaint.getTextScaleX();
  }

  public void setTextScaleX(float size) {
    if (size != mTextPaint.getTextScaleX()) {
      mTextPaint.setTextScaleX(size);
      measureContent();
    }
  }

  public Layout.Alignment getTextAlign() {
    return mTextAlignment;
  }

  public void setTextAlign(Layout.Alignment align) {
    if (mTextAlignment != align) {
      mTextAlignment = align;
      measureContent();
    }
  }

  public void setTypeface(Typeface tf, int style) {
    if (style > 0) {
      if (tf == null) {
        tf = Typeface.defaultFromStyle(style);
      } else {
        tf = Typeface.create(tf, style);
      }
      setTypeface(tf);
      int typefaceStyle = tf != null ? tf.getStyle() : 0;
      int need = style & ~typefaceStyle;
      mTextPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
      mTextPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
    } else {
      mTextPaint.setFakeBoldText(false);
      mTextPaint.setTextSkewX(0);
      setTypeface(tf);
    }
  }

  public Typeface getTypeface() {
    return mTextPaint.getTypeface();
  }

  public void setTypeface(Typeface tf) {
    if (mTextPaint.getTypeface() != tf) {
      mTextPaint.setTypeface(tf);
      measureContent();
    }
  }

  public void setTextColor(int color) {
    setTextColor(ColorStateList.valueOf(color));
  }

  public void setTextColor(ColorStateList colorStateList) {
    mTextColors = colorStateList;
    updateTextColors(getState());
  }

  public void setTextPath(Path path) {
    if (mTextPath != path) {
      mTextPath = path;
      measureContent();
    }
  }

  private void measureContent() {
    if (mTextPath != null) {
      mTextLayout = null;
      mTextBounds.setEmpty();
    } else {
      double desired = Math.ceil(Layout.getDesiredWidth(mText, mTextPaint));
      mTextLayout = new StaticLayout(mText, mTextPaint, (int) desired,
        mTextAlignment, 1.0f, 0.0f, false);
      mTextBounds.set(0, 0, mTextLayout.getWidth(), mTextLayout.getHeight());
    }
    invalidateSelf();
  }

  private boolean updateTextColors(int[] stateSet) {
    int newColor = mTextColors.getColorForState(stateSet, Color.WHITE);
    if (mTextPaint.getColor() != newColor) {
      mTextPaint.setColor(newColor);
      return true;
    }
    return false;
  }

  @Override
  protected void onBoundsChange(Rect bounds) {
    mTextBounds.set(bounds);
  }

  @Override
  public boolean isStateful() {
    return mTextColors.isStateful();
  }

  @Override
  protected boolean onStateChange(int[] state) {
    return updateTextColors(state);
  }

  @Override
  public int getIntrinsicHeight() {
    if (mTextBounds.isEmpty()) {
      return -1;
    } else {
      return (mTextBounds.bottom - mTextBounds.top);
    }
  }

  @Override
  public int getIntrinsicWidth() {
    if (mTextBounds.isEmpty()) {
      return -1;
    } else {
      return (mTextBounds.right - mTextBounds.left);
    }
  }

  @Override
  public void draw(Canvas canvas) {
    final Rect bounds = getBounds();
    final int count = canvas.save();
    canvas.translate(bounds.left, bounds.top);
    if (mTextPath == null) {
      mTextLayout.draw(canvas);
    } else {
      canvas.drawTextOnPath(mText.toString(), mTextPath, 0, 0, mTextPaint);
    }
    canvas.restoreToCount(count);
  }

  @Override
  public void setAlpha(int alpha) {
    if (mTextPaint.getAlpha() != alpha) {
      mTextPaint.setAlpha(alpha);
    }
  }

  @Override
  public int getOpacity() {
    return mTextPaint.getAlpha();
  }

  @Override
  public void setColorFilter(ColorFilter cf) {
    if (mTextPaint.getColorFilter() != cf) {
      mTextPaint.setColorFilter(cf);
    }
  }
}
