package com.sahil.bakingtut.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.sahil.bakingtut.Constants;
import com.sahil.bakingtut.MainActivity;
import com.sahil.bakingtut.R;

public class BakingAppWidget extends AppWidgetProvider
{


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BAKING_APP_SHARED_PREFERENCES_KEY,0);
        String text = sharedPreferences.getString(Constants.SP_TEXT_INGRED_KEY,context.getString(R.string.data_not_found_message));
        String recipe_name = sharedPreferences.getString(Constants.RECIPE_NAME_KEY,context.getString(R.string.recipe_name_not_found_message));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.app_widget_title, recipe_name+"\n"+text);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        views.setOnClickPendingIntent(R.id.widget_host,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}