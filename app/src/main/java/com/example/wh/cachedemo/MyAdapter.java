package com.example.wh.cachedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by wh on 16-7-30.
 */
public class MyAdapter extends BaseAdapter implements LoadListener {
    private static final String TAG = "whwhwh--MyAdapter";
    private String[] imgUrl;
    private Context mContext;
    private ListView mListView;


    public MyAdapter(String[] str, Context context, ListView listView) {
        imgUrl = str;
        mContext = context;
        mListView = listView;
    }

    @Override
    public int getCount() {
        return imgUrl.length;
    }

    @Override
    public Object getItem(int position) {
        return imgUrl[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
        holder.imageView.setImageResource(R.drawable.empty_photo);
        holder.imageView.setTag(imgUrl[position]);
        setBitmap(convertView, imgUrl[position]);
        return convertView;
    }

    private void setBitmap(View convertView, String url) {
        // 本来用mListView.findViewWithTag(url); 但是总是找不到，不知道为什么？？？
        // 下面的onSuccess方法中却是成功的，这是一个问题，有时间可以查一下具体原因。
        ImageView imageView = (ImageView) convertView.findViewWithTag(url);
        Bitmap bitmap;
        bitmap = SoftReferenceCache.getInstance().get(url);
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i(TAG, "setBitmap: SoftReferenceCache");
            return;
        }
        bitmap = MemoryCache.getInstance().get(url);
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i(TAG, "setBitmap: MemoryCache");
            return;
        }
        bitmap = DiskCache.getInstance(mContext).get(url);
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.i(TAG, "setBitmap: DiskCache");
            return;
        }
        Log.i(TAG, "setBitmap: Net Load");
        DiskCache.getInstance(mContext).setListener(this).downLoad(url);
    }

    @Override
    public void onSuccess(String url, Bitmap bitmap) {
        ImageView imageView = (ImageView) mListView.findViewWithTag(url);
        Log.i(TAG, "onSuccess: imageView = " + imageView);
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onError(String error) {
        Log.i(TAG, "onError: " + error);
    }

    class ViewHolder {
        ImageView imageView;
    }
}
