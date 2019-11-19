package com.example.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.helpmequickly_my.R;
import com.example.task.ImLoader;
import com.example.task.Task;
import com.example.task.TaskAdapter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private List<Task> mTasks = new ArrayList<>();
    private List banners = new ArrayList();
    private TextView type1;
    private TextView type2;
    private TextView type3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        final RecyclerView recyclerView = rootView.findViewById(R.id.list_task);
        initTask(rootView);
        Banner banner = (Banner) rootView.findViewById(R.id.banner);
        banner.setImageLoader(new ImLoader());
        banner.setImages(banners);
        banner.start();
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        final TaskAdapter adapter = new TaskAdapter(mTasks);
        recyclerView.setAdapter(adapter);
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.t1);
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.t2);
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelect(R.id.t3);
            }
        });
        return rootView;

    }

    private void initTask(View view){
        type1 = (TextView) view.findViewById(R.id.t1);
        type2 = (TextView) view.findViewById(R.id.t2);
        type3 = (TextView) view.findViewById(R.id.t3);
        type1.setSelected(true);
        for(int i = 0; i < 10; i++) {
            Task task= new Task("4号楼小刘","  [加急]下雨啦，快去帮我收衣服！！！！","快点","今天18：15","剩余00：15");
            mTasks .add(task);
            if (i < 5){
                banners.add(R.drawable.home);
            }
        }

    }

    private void changeSelect(int resId){
        type1.setSelected(false);
        type2.setSelected(false);
        type3.setSelected(false);
        switch (resId) {
            case R.id.t1:
                type1.setSelected(true);
                break;
            case R.id.t2:
                type2.setSelected(true);
                break;
            case R.id.t3:
                type3.setSelected(true);
                break;
        }
    }
}
