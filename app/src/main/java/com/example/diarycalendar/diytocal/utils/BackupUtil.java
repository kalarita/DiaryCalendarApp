package com.example.diarycalendar.diytocal.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BackupUtil {
    public static void rewrite(SQLiteDatabase db, String contentType, String date, Context context)
    {
        Cursor cursor = getCursor(db,contentType,date);
        FileOutputStream outputStream;
        BufferedOutputStream bos;
        try
        {
            File src = getPath(contentType,context,date);
            //打开文件

            if(!src.exists() && !src.isFile())
            {
                src.createNewFile();
            }

            outputStream = new FileOutputStream(src);
            bos = new BufferedOutputStream(outputStream);
            while(cursor.moveToNext())
            {
                bos.write(getItem(cursor,contentType,date).getBytes(),0,getItem(cursor,contentType,date).getBytes().length);
                bos.flush();
            }
            bos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static Cursor getCursor(SQLiteDatabase db, String contentType, String date)
    {
        if(contentType.equals("Diary"))
        {
            return db.query("Diary", new String[]{"id","date", "title","content"},"date = ?",new String[]{date},null,null,null);
        }
        else
        {
            return  db.query("Event",new String[]{"id","date", "event"},"date = ?",new String[]{date},null,null,null);
        }
    }
    public static File getPath(String contentType, Context context, String date)
    {
        File parent = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File dire = new File(parent, contentType);
        if (!dire.exists() && !dire.isDirectory()) {
            dire.mkdir();
        }
        return new File(dire,date+".txt");
    }
    public static String getDest(String contentType, String date)
    {
        File dest = new File("/"+ contentType+"/"+date+".txt");
        return dest.toString();
    }
    public static String getItem(Cursor cursor, String contentType,String date)
    {
        StringBuilder sb = new StringBuilder("");
        sb.append(cursor.getInt(cursor.getColumnIndex("id")));
        sb.append(",");
        if(contentType.equals("Diary"))
        {
            sb.append(cursor.getString(cursor.getColumnIndex("title")));
            sb.append(",");
            sb.append(cursor.getString(cursor.getColumnIndex("content")));
            sb.append(",");
        }
        else
        {
            sb.append(cursor.getString(cursor.getColumnIndex("event")));
        }
        sb.append(date);
        sb.append("\n");
        return sb.toString();

    }
    public static void close(Cursor cursor)
    {
        try {
            if(cursor != null)
            {
                cursor.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static FileInputStream getSrc(String contentType, Context context, String date) throws FileNotFoundException {
        File src = getPath(contentType,context,date);
        return new FileInputStream(src);
    }
}
