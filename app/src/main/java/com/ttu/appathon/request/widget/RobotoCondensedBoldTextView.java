package com.ttu.appathon.request.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

// Adding custom font style
public class RobotoCondensedBoldTextView
  extends TextView
{
  public RobotoCondensedBoldTextView(Context paramContext)
  {
    super(paramContext);

    if (!isInEditMode()) {}
    try
    {
      setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "fonts/Roboto-Light.ttf"));
      return;
    }
    catch (Exception localException)
    {
              localException.printStackTrace();
    }
  }
  
  public RobotoCondensedBoldTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);

    if (!isInEditMode()) {}
    try
    {
      setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "fonts/roboto.ttf"));
      return;
    }
    catch (Exception localException)
    {

      localException.printStackTrace();
    }
  }
  
  public RobotoCondensedBoldTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (!isInEditMode()) {}
    try
    {
      setTypeface(Typeface.createFromAsset(paramContext.getAssets(), "fonts/roboto.ttf"));
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}

/* Location:           C:\Users\request\Desktop\APK tool\New folder\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar * Qualified Name:     com.bt.bms.widget.RobotoCondensedBoldTextView * JD-Core Version:    0.7.0.1 */