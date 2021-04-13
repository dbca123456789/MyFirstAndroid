package com.example.myfirstandroid.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfirstandroid.Bean.News;
import com.example.myfirstandroid.R;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> implements View.OnClickListener {
    private int resourceId;
    private CallBack mCallBack;

    private static final String TAG = "NewsAdapter";

    public NewsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<News> objects, CallBack callback) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.mCallBack = callback;
    }

    public interface CallBack{
        public void click(View view);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.newsImg = view.findViewById(R.id.news_item_img);
            viewHolder.newsTitle = view.findViewById(R.id.news_item_title);
            viewHolder.newsAuthor = view.findViewById(R.id.news_item_author);
            viewHolder.newsDate = view.findViewById(R.id.news_item_date);
            viewHolder.newsDelete = view.findViewById(R.id.delete_item);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

//        viewHolder.newsImg.setImageBitmap(news.getNews_img());

        if (news.getNews_img()!=null){
//            Log.i(TAG, "getView--> news.getNews_img != null");
            viewHolder.newsImg.setImageBitmap(news.getNews_img());
        }else {
            Log.i(TAG, "getView--> news.getNews_img = null");
        }
        viewHolder.newsTitle.setText(news.getNews_title());
        viewHolder.newsDate.setText(news.getDate());
        viewHolder.newsAuthor.setText(news.getAuthor_name());

        // 通常我们是用findViewById()方法来取得我们要使用的View控件，
        // 还可以用View中的setTag(Onbect)给View添加一个额外的数据，再用getTag()获取到对应的View。
        // setTag()和getTag()方法常用在处理多个相同的View时
        // 比如我们有要对几个Button添加点击事件，我们除了可以用view.getId()来区分Button，也可以给Button设置Tag来进行区分

        viewHolder.newsDelete.setTag(position);
        viewHolder.newsDelete.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        mCallBack.click(v);
    }

    static class ViewHolder{
        ImageView newsImg;
        TextView newsTitle;
        TextView newsAuthor;
        TextView newsDate;
        ImageView newsDelete;
    }

}
