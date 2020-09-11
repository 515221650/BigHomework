package com.java.yangzhuoyi.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.yangzhuoyi.EventListActivity;
import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.adapter.EventAdapter;
import com.java.yangzhuoyi.common.DefineView;
import com.java.yangzhuoyi.util.Event;
import com.java.yangzhuoyi.util.NewsDigest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.spec.ECField;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EventFragment extends BaseFragment implements DefineView {
    private View mView;
    private ListView eventList;
    private List<Event> eventSourceList = new ArrayList<>();
    private EventAdapter eventAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null)
        {
            mView = inflater.inflate(R.layout.event_fragment, null);
            initView();
            initValidData();
            initListener();
            binData();
            FetchEvent process = new FetchEvent();
            process.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        return mView;
    }

    @Override
    public void initView() {
        eventList = mView.findViewById(R.id.event_list);
    }

    @Override
    public void initValidData() {
        eventAdapter = new EventAdapter(getActivity());
        eventList.setAdapter(eventAdapter);

    }

    @Override
    public void initListener() {
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EventListActivity.class);
//                intent.putExtra("eventList", eventSourceList.get(i));
                Event.setInstance(eventSourceList.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void binData() {

    }

    class RcvEvent
    {
        public String title;
        public String seg_title;
        public String time;
        public String id;
        public ArrayList<String> segs;
        public ArrayList<Double> wordBag;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSeg_title(String seg_title) {
            this.seg_title = seg_title;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class FetchEvent extends AsyncTask<Void, Void, Void> {
        final int SIZE = 10;
        ArrayList<RcvEvent> rcvEvents = new ArrayList<>();
        HashMap<String, Integer> segsHashMap = new HashMap<>();
        List<ArrayList<Double>> centerWordBag = new ArrayList<>(SIZE);
        ArrayList<ArrayList<RcvEvent>> FinalEventCluster;
        HashSet<String> StopWords = new HashSet<>();

        int NewsNum;

        @Override
        protected Void doInBackground(Void... voids) {
            StopWords.add("表明"); StopWords.add("表示");
            URL url = null;
            try {
                url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type=event&page=1&size=1000");
                StringBuilder data = new StringBuilder();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(20000);
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONObject JO = new JSONObject(data.toString());
                JSONArray JA2 = (JSONArray) JO.get("data");

                NewsNum = JA2.length();
                for (int i = 0; i < NewsNum; i++) {
                    RcvEvent event = new RcvEvent();
                    JSONObject JO2 = (JSONObject) JA2.get(i);
                    event.setId(JO2.optString("_id"));
                    event.setTitle(JO2.optString("title"));
                    event.setTime(JO2.optString("time"));
                    event.setSeg_title(JO2.optString("seg_text"));
                    event.segs = new ArrayList<>();
                    event.segs.addAll(Arrays.asList(event.seg_title.split("\\s+")));
                    rcvEvents.add(event);
                }


            for(int i=0; i<NewsNum; i++)
            {
                int segsLen = rcvEvents.get(i).segs.size();
                for(int j=0; j<segsLen; j++)
                {
                    String tmp = rcvEvents.get(i).segs.get(j);
                    if(tmp.length() > 1 && !StopWords.contains(tmp))
                    {
                        Integer v = segsHashMap.get(tmp);
                        if(v == null) v = 0;
                        segsHashMap.put(tmp, v+1);
                    }
                }
            }

            List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(segsHashMap.entrySet());
            Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            ArrayList<Map.Entry<String,Integer>> topSegs = new ArrayList<>();
            topSegs.addAll(list.subList(30, 330));
            Collections.sort(topSegs, new Comparator<Map.Entry<String,Integer>>() {
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o2.getKey().compareTo(o1.getKey());
                }
            });
            HashMap<String, Integer> topSegHashMap = new HashMap<>();
            for(int i=0; i<300; i++)
            {
                topSegHashMap.put(topSegs.get(i).getKey(), topSegs.get(i).getValue());
            }

            for(int i=0; i<NewsNum; i++)
            {
                HashMap<String, Integer> newsHashMap = new HashMap<>();
                for(int j=0; j<300; j++){
                    newsHashMap.put(topSegs.get(j).getKey(), 0);
                }
                for(String word: rcvEvents.get(i).segs)
                {
                    if(newsHashMap.get(word) != null)
                    {
                        newsHashMap.put(word, 1);
                    }
                }
                List<Map.Entry<String,Integer>> wordlist = new ArrayList<Map.Entry<String,Integer>>(newsHashMap.entrySet());
                Collections.sort(wordlist,new Comparator<Map.Entry<String,Integer>>() {
                    public int compare(Map.Entry<String, Integer> o1,
                                       Map.Entry<String, Integer> o2) {
                        return o2.getKey().compareTo(o1.getKey());
                    }
                });
                rcvEvents.get(i).wordBag = new ArrayList<>();
                for(Map.Entry<String,Integer> entry: wordlist)
                {
                    if(entry.getValue()!= 0) rcvEvents.get(i).wordBag.add(1.0);
                    else rcvEvents.get(i).wordBag.add(0.0);
                }
            }
            Log.d("event ", "start k-means");


            // Start K-means
            Collections.shuffle(rcvEvents);
            centerWordBag.clear();
            for(int i=0; i<SIZE; i++)
                centerWordBag.add(rcvEvents.get(i).wordBag);

            act_cluster(80);
            Log.d("event ", "finish k-means");

            for(int k=0; k<SIZE; k++)
            {
                ArrayList<RcvEvent> cluster = FinalEventCluster.get(k);
                if (cluster.size() > 100 || cluster.size() < 5) continue;
                ArrayList<Double> wordBagSum = new ArrayList<>();
                int clusterSize = cluster.size();
                for(int p=0; p<300; p++)
                {
                    Double tmp = 0.0;
                    for(int q=0; q<clusterSize; q++)
                    {
                        tmp += cluster.get(q).wordBag.get(p);
                    }
                    wordBagSum.add(tmp);
                }
                ArrayList<Map.Entry<String,Double>> topTags = new ArrayList<>();
                for(int p=0; p<300; p++)
                {
                    topTags.add(new AbstractMap.SimpleEntry<String, Double>(topSegs.get(p).getKey(), wordBagSum.get(p)));
                }
                Collections.sort(topTags,new Comparator<Map.Entry<String,Double>>() {
                    public int compare(Map.Entry<String, Double> o1,
                                       Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });

                Event clusterEvent = new Event();
                clusterEvent.events = new ArrayList<>();
                for(int pp=0; pp<3; pp++)
                {
                    clusterEvent.keywords[pp] = topTags.get(pp).getKey();
                    clusterEvent.hotnumber[pp] = Integer.toString(topTags.get(pp).getValue().intValue());
                }
                int eventClusterSize = cluster.size();
                for(int pp=0; pp<eventClusterSize; pp++)
                {
                    NewsDigest digest = new NewsDigest();
                    digest.setTitle(cluster.get(pp).title);
                    digest.setTime(cluster.get(pp).time);
                    digest.setId(cluster.get(pp).id);
                    clusterEvent.events.add(digest);
                }
//                Log.d("1","2333"+" "+eventClusterSize);
                eventSourceList.add(clusterEvent);
            }
            }
            catch (SocketTimeoutException e){
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            eventAdapter.eventList.clear();
            eventAdapter.eventList.addAll(eventSourceList);
            eventAdapter.notifyDataSetChanged();
        }


        double getDist(RcvEvent e1, ArrayList<Double> e2)
        {
            double dist = 0;
            for(int i=0; i<300; i++)
            {
                double tmp = e1.wordBag.get(i) - e2.get(i);
                dist += tmp * tmp;
            }
            return dist;
        }

        void act_cluster(int times)
        {
            for(int t=0; t<times; t++)
            {
                Collections.shuffle(centerWordBag);
                FinalEventCluster = new ArrayList<>();
                for(int i=0; i<SIZE; i++)
                    FinalEventCluster.add(new ArrayList<RcvEvent>());

                for(RcvEvent event: rcvEvents)
                {
                    double mindist = 10000;
                    int minIndex = 0;
                    for(int i=0; i<SIZE; i++)
                    {
                        double tmpdist = getDist(event, centerWordBag.get(i));
//                        if(t == 1)
//                        {
//                            Log.d("evnet", "tmpdist [" + i +"] "+tmpdist);
//                        }
                        if(tmpdist < mindist)
                        {
                            mindist = tmpdist;
                            minIndex = i;
                        }

                    }
//                    if(t == 1)
//                    {
//                        Log.d("event ", "choose cluster "+ minIndex);
//                    }
                    FinalEventCluster.get(minIndex).add(event);
                }
                centerWordBag.clear();
                for(int i=0; i<SIZE; i++)
                {
                    centerWordBag.add(getCenter(FinalEventCluster.get(i)));
                }
            }
        }

        ArrayList<Double> getCenter(ArrayList<RcvEvent> list)
        {
            int listsize = list.size();
            ArrayList<Double> res = new ArrayList<>();
            if(listsize < 5)
            {
                res = rcvEvents.get((int) (Math.random() * NewsNum)).wordBag;
            }
            for(int i=0; i<300; i++)
            {
                double tmp = 0;
                for(int j=0; j<listsize; j++)
                {
                    tmp += list.get(j).wordBag.get(i);
                }
                res.add(tmp/(double)listsize);
            }
            return res;
        }
    }
}
