package com.myapplock.application;

import android.app.Application;

import com.myapplock.models.AppItems;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

/**
 * Created by amjaiswal on 7/9/2015.
 */
public class MyAppLock extends Application{

    private ArrayList<AppItems> lockedAppList;

    private ArrayList<AppItems> unlockedAppList;

    public ArrayList<AppItems> getLockedAppList() {
        if(lockedAppList==null)
        {
            lockedAppList=new ArrayList<AppItems>();
        }
        return lockedAppList;
    }

    public void setLockedAppList(ArrayList<AppItems> lockedAppList) {
        this.lockedAppList = lockedAppList;
    }

    public ArrayList<AppItems> getUnlockedAppList() {
        if(unlockedAppList==null)
        {
            unlockedAppList=new ArrayList<AppItems>();
        }
        return unlockedAppList;
    }

    public void setUnlockedAppList(ArrayList<AppItems> unlockedAppList) {
        this.unlockedAppList = unlockedAppList;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initImageLoader();
    }

    public void initImageLoader() {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // .imageDownloader(DownloadModule.getCustomImageDownaloder(this))
                        // .discCacheFileNameGenerator(new FileNameGenerator() {
                        // @Override
                        // public String generate(String imageUri) {
                        // return Utils.getNostraImageFileName(imageUri);
                        // }
                        // })
                .memoryCache(new WeakMemoryCache());

        ImageLoader.getInstance().init(builder.build());
    }

}
