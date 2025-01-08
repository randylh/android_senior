package com.example.myapplication.ipc.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.db.book.DbOpenHelper;

public class BookProvider extends ContentProvider {

    private static final String TAG = BookProvider.class.getSimpleName();

    private static final String AUTHORITY = "com.example.myapplication.ipc.provider.bp";

    private static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    private static final Uri USER_CONTEN_URI = Uri.parse("content://" + AUTHORITY +"/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;


    @Override
    public boolean onCreate() {
        // 创建，初始化工作
        Log.d(TAG, "onCreate current Thread= " + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return false;
    }

     private void initProviderData() {
        try {
            mDb = new DbOpenHelper(mContext).getWritableDatabase();
            mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
            mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
            mDb.execSQL("insert into book values(3,'Android');");
            mDb.execSQL("insert into book values(4,'iOS');");
            mDb.execSQL("insert into book values(5,'Html5');");
            mDb.execSQL("insert into user values(1,'jake',1);");
            mDb.execSQL("insert into user values(2,'jasmine',0);");
        }catch (Exception e) {

        }


     }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query current Thread= " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (null == tableName) {
            throw new IllegalArgumentException("Unsupported uri: " + uri);
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //  getType 用来返回一个 Uri，请求所对应的 MIME 类型（媒体类型），比如图片、视频等
        Log.d(TAG, "getType");
        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, "insert");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb.insert(table, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = mDb.update(table, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
