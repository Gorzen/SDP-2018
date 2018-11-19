package ch.epfl.sweng.studyup;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     *
     * @return Usable Context
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     *
     * @return Usable Resources outside a given Context
     */
    public static Resources getRes() {
        return mContext.getResources();
    }

    /**
     *
     * @param id The ID of the String
     * @return Usable String outside a given Context
     */
    public static String getStr(int id) {
        return getRes().getString(id);
    }
}
