package cn.cloudfk.taoalbum.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.io.File;
import java.util.ArrayList;

import cn.cloudfk.taoalbum.R;
import cn.cloudfk.taoalbum.data.DataTool;
import cn.cloudfk.taoalbum.data.model.AlbumModel;
import cn.cloudfk.taoalbum.utils.HttpAssist;
import cn.cloudfk.taoalbum.utils.ToolsView;

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AlbumActivity";
    private Toolbar mToolbar;
    private TextView mTvMessage;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTvMessage = findViewById(R.id.tv_message);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView.addItemDecoration(divider);

        findViewById(R.id.btn_upload).setOnClickListener(this);

        mAdapter = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewAlbum(position);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Select picture, from album.
     */
    private void selectAlbum() {
        Album.album(this)
                .multipleChoice()
                .columnCount(2)
                .selectCount(6)
                .camera(true)
                .cameraVideoQuality(1)
                .cameraVideoLimitDuration(Integer.MAX_VALUE)
                .cameraVideoLimitBytes(Integer.MAX_VALUE)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .title(mToolbar.getTitle().toString())
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);
                        mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        ToolsView.ToastToCenter(AlbumActivity.this, R.string.canceled, Toast.LENGTH_SHORT);
                    }
                })
                .start();
    }

    private void uploadAlbum(){
        ToolsView.ToastToCenter(this,"call uploadAlbum",Toast.LENGTH_SHORT);
        if (mAlbumFiles!= null && mAlbumFiles.size()>0){
            for (AlbumFile file :mAlbumFiles){
                Log.i(TAG,"file.getPath()"+file.getPath());

                ToolsView.ToastToCenter(this,file.getBucketName(),Toast.LENGTH_LONG);
                final String fileNameAll = file.getPath();
                final String fileName = file.getPath() ;
                AlbumModel albumModel = DataTool.queryAlbumByFileName(fileName);
                boolean isUpload = true;
                if (albumModel==null){
                    albumModel = new AlbumModel();
                    albumModel.fileName = fileNameAll;
                    albumModel.state=0;
                    DataTool.insertAlbum(albumModel);
                }else if (albumModel.state !=null &&albumModel.state.intValue() ==1){
                    ToolsView.ToastToCenter(this,file.getBucketName()+",已经上传过了",Toast.LENGTH_SHORT);
                    Log.i(TAG,"文件已经上传过了:"+fileName);
                    isUpload = false;
                }
                File inFile = new File(file.getPath());
                if (isUpload && inFile != null &&inFile.exists()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run(){
                            HttpAssist.getInstance().uploadFile(new File(fileNameAll));
                            AlbumModel albumModel = new AlbumModel();
                            albumModel.fileName = fileName;
                            albumModel.state=1;
                            DataTool.updateAlbum(albumModel);
                        }

                    }).start();
                }else{
                    Log.i(TAG,"文件已经上传或不存在:"+file.getPath());
                    ToolsView.ToastToCenter(this, "文件已经上传或不存在。", Toast.LENGTH_SHORT);
                }
            }
        }
    }
    /**
     * Preview image, to album.
     */
    private void previewAlbum(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            ToolsView.ToastToCenter(this, R.string.no_selected, Toast.LENGTH_SHORT);
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title(mToolbar.getTitle().toString())
                                    .build()
                    )
                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mAdapter.notifyDataSetChanged(mAlbumFiles);
                            mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    })
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.menu_album: {
                selectAlbum();
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_upload:{
                uploadAlbum();
                break;
            }
        }

    }
}
