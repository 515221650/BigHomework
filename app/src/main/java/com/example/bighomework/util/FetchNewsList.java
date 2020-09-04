//package com.example.bighomework.util;
//
//import android.os.AsyncTask;
//import androidx.fragment.app.Fragment;
//
//import com.example.bighomework.adapter.NewsListAdapter;
//import com.example.bighomework.fragment.PageFragment;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.nio.channels.AsynchronousChannelGroup;
//
//public class FetchNewsList extends AsyncTask<Fragment, Void, Void> {
//    String newsClass = "";
//    NewsListAdapter newsListAdapter = null;
//
//    @Override
//    protected Void doInBackground(Fragment... fragments) {
//        newsClass = "a";
//        newsClass = newsClass + "b";
//        System.out.print(newsClass);
////        newsListAdapter = ((PageFragment) fragments[0]).newsListAdapter;
////        newsClass = ((PageFragment) fragments[0]).getMessage().toLowerCase();
////        try {
////            URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type="+newsClass+"&page=1&size=20");
////            StringBuilder data = new StringBuilder();
////            try {
////                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
////                InputStream inputStream = httpURLConnection.getInputStream();
////                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
////                String line = "";
////                while(line != null)
////                {
////                    line = bufferedReader.readLine();
////                    data.append(line);
////                }
////                JSONObject JO = new JSONObject(data.toString());
////                JSONArray JA2 = (JSONArray) JO.get("data");
////                int NewsNum = JA2.length();
////                for(int i=0; i<NewsNum; i++)
////                {
////                    NewsDigest newsDigest = new NewsDigest();
////                    JSONObject JO2 = (JSONObject) JA2.get(i);
////                    newsDigest.setId((String) JO2.get("_id"));
////                    newsDigest.setTitle((String) JO2.get("title"));
////                    newsDigest.setTime((String) JO2.get("time"));
////                    String _source = (String) JO2.get("source");
////                    if(_source.equals("null"))
////                        newsDigest.setSource("");
////                    else newsDigest.setSource(_source);
////                    newsDigest.setDigest(((String) JO2.get("content")).substring(0, 30));
////                    newsListAdapter.newsDigestArrayList.add(newsDigest);
////                }
////                newsListAdapter.notifyDataSetChanged();
////            } catch (IOException e) {
////                e.printStackTrace();
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        } catch (MalformedURLException e) {
////            e.printStackTrace();
////        }
//        return null;
//    }
//}
