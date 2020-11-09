package com.example.statebartest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextView;
    private ImageView mImageView;
    private Button mButton;
    private RecyclerView mRecyclerView;
    private boolean mIsShow = true;
    private static final int REQUEST_IMAGE = 0x31;
    private static final String TAG = "MainActivity";
    private ArrayList<String> mPath = new ArrayList<>();
    RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImmersionBar.with(this).fullScreen(true).statusBarDarkFont(true).init();
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview:
                if (mIsShow) {
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                    mIsShow = false;
                } else {
                    ImmersionBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
                    mIsShow = true;
                }
                break;
            case R.id.image_view:
                if (mPath != null && mPath.size() != 0) {
                    MultiImageSelector.create(this)
                            .origin(mPath)
                            .start(this, REQUEST_IMAGE);
                } else {
                    MultiImageSelector.create(this)
                            .start(this, REQUEST_IMAGE);
                }

                break;
            case R.id.button:
                if (mPath == null || mPath.size() == 0) {
                    Toast.makeText(this, "No Picture！Please choice again", Toast.LENGTH_SHORT).show();
                    break;
                }
                upload();
        }

    }

    private void initView() {
        mTextView = findViewById(R.id.textview);
        mTextView.setOnClickListener(this);
        mImageView = findViewById(R.id.image_view);
        mImageView.setOnClickListener(this);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerViewAdapter = new RecyclerViewAdapter(mPath, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                mPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mRecyclerViewAdapter.setPath(mPath);
                // 处理你自己的逻辑 ....
                for (String s :
                        mPath) {
                    Log.d(TAG, "onActivityResult: " + s);
                }


            }
        }
    }

    public void upload() {
         Map<String, RequestBody> params;
        UploadHelper helper = UploadHelper.getInstance();
        for (String s : mPath) {
            File file=new File(s);
            helper.addParameter("file"+mPath.indexOf(s),file);
        }
        params=helper.builder();
        RetrofitManager.getInstance().upload(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Toast.makeText(MainActivity.this,"开始上传",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(String s) {
                        mRecyclerViewAdapter.setPath(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError: "+ e);
                        mRecyclerViewAdapter.setPath(null);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}