package com.example.vocabularybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WordItemFragment.OnFragmentInteractionListener, WordDetailFragment.OnFragmentInteractionListener {
    private static final String TAG = "tag";
    WordsDBHelper helper;
    public static List<Activity> activityList = new LinkedList();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityList.add(this);
        helper = new WordsDBHelper(this);
        helper.getReadableDatabase();
        helper.close();
    }
    private boolean isLand(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true;
        }
        return false;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus,menu);
        Log.v(TAG, "menu");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.v(TAG, "menu-opened");
        switch(item.getItemId()){
            case R.id.all:{
                RefreshWordItemFragment();
                break;
            }
            case R.id.search:{
                SearchDialog();
                break;
            }
            case R.id.add:{
                InsertDialog();
                break;
            }
            case R.id.exit:{
                exit();
            }
        }
        return false;
    }
    public void exit(){
        for(Activity act:activityList){
            act.finish();
        }
        System.exit(0);
    }

    public void onWordDetailClick(Uri uri) {

    }
    public void onWordItemClick(final String id) {
        if(isLand()) {
            ChangeWordDetailFragment(id);
        }else {
            Intent intent = new Intent(MainActivity.this, WordDetailActivity.class);
            intent.putExtra(WordDetailFragment.ARG_ID, id);
            startActivity(intent);
        }

        View mLongPressView = new LongPressView(this);
        mLongPressView.setOnLongClickListener(new View.OnLongClickListener() { //长按单词修改或删除
            public boolean onLongClick(View v) {
                onDeleteDialog(id);
                return true;
            }
        });
    }

    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
        RefreshWordItemFragment();
    }
    public void onUpdateDialog(String strId) {
        WordsDB wordsDB=WordsDB.getWordsDB();
        if (wordsDB != null && strId != null) {
            Words.WordDescription item = wordsDB.getSingleWord(strId);
            if (item != null) {
                UpdateDialog(strId, item.word, item.meaning, item.sample);
            }
        }
        RefreshWordItemFragment();
    }
    private void ChangeWordDetailFragment(String id){
        Bundle arguments = new Bundle();
        arguments.putString(WordDetailFragment.ARG_ID, id);
        Log.v(TAG, id);
        WordDetailFragment fragment = new WordDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();
    }
    private void InsertDialog() {
        Log.v(TAG, "insert");
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);
        Log.v(TAG, "getLayout");
        new AlertDialog.Builder(this).setTitle("新增单词").setView(linearLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) linearLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning = ((EditText) linearLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample = ((EditText) linearLayout.findViewById(R.id.txtSample)).getText().toString();
                        Log.v(TAG,strWord+":"+strMeaning+":"+strSample);
                        //InsertUserSql(strWord, strMeaning, strSample);
                        WordsDB wordsDB=WordsDB.getWordsDB();
                        Log.v(TAG, "getDB");
                        wordsDB.Insert(strWord, strMeaning, strSample);
                        Log.v(TAG, "have-inserted");
                        RefreshWordItemFragment();
                    }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    private void DeleteDialog(final String strId) {
        Log.v(TAG, "delete");
        new AlertDialog.Builder(this).setTitle("删除单词")
                .setMessage("是否真的删除单词?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WordsDB wordsDB=WordsDB.getWordsDB();
                        wordsDB.DeleteUseSql(strId);
                        RefreshWordItemFragment();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
        }).create().show();
    }
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final LinearLayout LinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText) LinearLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText) LinearLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText) LinearLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this).setTitle("修改单词").setView(LinearLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String strNewWord = ((EditText) LinearLayout.findViewById(R.id.txtWord)).getText().toString();
                String strNewMeaning = ((EditText) LinearLayout.findViewById(R.id.txtMeaning)).getText().toString();
                String strNewSample = ((EditText) LinearLayout.findViewById(R.id.txtSample)).getText().toString();
                WordsDB wordsDB=WordsDB.getWordsDB();
                wordsDB.UpdateUseSql(strId, strWord, strNewMeaning, strNewSample);
                RefreshWordItemFragment();
                }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }).create().show();
    }
    private void SearchDialog() {
        final LinearLayout  LinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this).setTitle("查找单词").setView(LinearLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
            String txtSearchWord = ((EditText) LinearLayout.findViewById(R.id.txtSearchWord)).getText().toString();
        RefreshWordItemFragment(txtSearchWord);
    }
    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {

            }
            }).create().show();
    }
    public void RefreshWordItemFragment() {
        WordItemFragment wordItemFragment = (WordItemFragment) getSupportFragmentManager().findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList();
    }
    public void RefreshWordItemFragment(String strWord) {
        WordItemFragment wordItemFragment = (WordItemFragment) getSupportFragmentManager().findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList(strWord);
    }
}
