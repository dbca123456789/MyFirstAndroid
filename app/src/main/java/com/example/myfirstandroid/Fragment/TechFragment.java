package com.example.myfirstandroid.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfirstandroid.Activity.ShowNewsActivity;
import com.example.myfirstandroid.Adapter.NewsAdapter;
import com.example.myfirstandroid.Bean.News;
import com.example.myfirstandroid.LoadListView;
import com.example.myfirstandroid.R;
import com.example.myfirstandroid.Utils.HttpUtils;
import com.example.myfirstandroid.Utils.MyBitmapUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TechFragment extends Fragment implements LoadListView.ILoadListener,
        LoadListView.RLoadListener, NewsAdapter.CallBack{

    private static final String TAG = "TechFragment";
    final String url = "http://api.tianapi.com/keji/index?key=5f925e02ce15a227b7a631f55f43eaca&num=30";

    private View view;
    private LoadListView mListView;
    private List<News> newsList;

    private NewsAdapter adapter;
    private MyBitmapUtils myBitmapUtils;

    public TechFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news, container, false);
        myBitmapUtils = new MyBitmapUtils(getContext());
        setupViews();
        boolean check = HttpUtils.isNetworkAvalible(getContext());
        Log.i(TAG, "Check Network!!!!!!" + check);
        if (!check){
            // 没有网络弹出设置网络提醒对话框 Dailog
            HttpUtils.checkNetwork(getActivity());
            Toast.makeText(getContext(), "当前没有可以使用的网络，请检查网络设置！", Toast.LENGTH_SHORT);
        }else {
            initNews();
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ShowNewsActivity.class);
                // ListView条目的position是从0开始计算的：0,1,2...这样
                // 但是添加了一个Header之后，position变成了从1开始，于是需要手动修正一下这个postion的值。
                intent.putExtra("title", newsList.get(position - mListView.getHeaderViewsCount()).getNews_title());
                intent.putExtra("url", newsList.get(position - mListView.getHeaderViewsCount()).getNews_url());
                intent.putExtra("date", newsList.get(position - mListView.getHeaderViewsCount()).getDate());
                intent.putExtra("author", newsList.get(position - mListView.getHeaderViewsCount()).getAuthor_name());
                intent.putExtra("pic_url", newsList.get(position - mListView.getHeaderViewsCount()).getNews_picurl());
                startActivity(intent);
                // 添加Activity过场动画
                getActivity().overridePendingTransition(R.anim.anim_scale, R.anim.anim_alpha);
            }
        });

        return view;

    }

    private void initNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                Log.i(TAG, "jsonData is: "+ jsonData);
                if (jsonData != null){
                    parseJSONWithGSON(jsonData);
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject json_news = jsonArray.getJSONObject(i);
                String imgUrl = json_news.getString("picUrl");
                /**
                 * 采取三级缓存策略加载图片
                 */
                Bitmap bitmap = myBitmapUtils.getBitmap(imgUrl);
                /**
                 * 不采取缓存策略
                 * 根据图片url从网络获取
                 */
                //Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);

                String title = json_news.getString("title");
                String date = json_news.getString("ctime");
                String author_name = json_news.getString("description");
                String url = json_news.getString("url");
                Log.i(TAG, "url:*-*-*-*-*-*-*" + imgUrl);
                News news = new News(bitmap, title, url, imgUrl, date, author_name);
                newsList.add(news);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (JSONException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void setupViews() {
        mListView = view.findViewById(R.id.lv_main);
        //上拉加载接口
        mListView.setInterface(this);
        mListView.setReflashInterface(this);
        newsList = new ArrayList<>();
        adapter = new NewsAdapter(getContext(), R.layout.news_item, newsList, this);
        mListView.setAdapter(adapter);
    }

    @Override
    public void click(View view) {
        Toast.makeText(getContext(), "该新闻已删除", Toast.LENGTH_SHORT).show();
        newsList.remove(Integer.parseInt(view.getTag().toString()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoad() {
        // 添加延时效果模拟数据加载
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initNewDatas(); // 得到新数据
                mListView.loadCompleted();
            }
        }, 2000);
    }

    private void initNewDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData =  HttpUtils.requestHttp(url);
                if (jsonData != null){
                    parseJSONWithGSON_Load(jsonData);
                }else {
                    Log.i(TAG, "initNewDatas--> jsonData==NULL");
                    return;
                }
            }
        }).start();
    }

    private void parseJSONWithGSON_Load(String jsonData) {
        try {

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");

            JSONObject json_news = jsonArray.getJSONObject(new Random().nextInt(30) + 1);
            String imgUrl = json_news.getString("picUrl");
            /**
             * 采取三级缓存策略加载图片
             */
            Bitmap bitmap = myBitmapUtils.getBitmap(imgUrl);
            /**
             * 不采取缓存策略
             * 根据图片url从网络获取
             */
            //Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);

            String title = json_news.getString("title");
            String date = json_news.getString("ctime");
            String author_name = json_news.getString("description");
            String url = json_news.getString("url");
            Log.i(TAG, "url:*-*-*-*-*-*-*" + imgUrl);
            News news = new News(bitmap, title, url, imgUrl, date, author_name);
            newsList.add(news);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initRefreshDatas();
                mListView.reflashComplete();
            }
        }, 2000);
    }

    private void initRefreshDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonData = HttpUtils.requestHttp(url);
                parseJSONWithGSON_Refresh(jsonData);
            }
        }).start();

    }

    private void parseJSONWithGSON_Refresh(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("newslist");

            JSONObject json_news = jsonArray.getJSONObject(new Random().nextInt(30)+1);
            String imgUrl = json_news.getString("picUrl");
            Log.i(TAG, "url:*-*-*-*-*-*-*" + imgUrl);
            Bitmap bitmap = HttpUtils.decodeUriAsBitmapFromNet(imgUrl);
            String title = json_news.getString("title");
            String date = json_news.getString("ctime");
            String author_name = json_news.getString("description");
            String url = json_news.getString("url");
            News news = new News(bitmap, title, url, imgUrl, date, author_name);
            newsList.add(news);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
