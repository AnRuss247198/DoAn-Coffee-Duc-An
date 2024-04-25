package coffee.app.DucAn.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import coffee.app.DucAn.database.CoffeeDB;
import coffee.app.DucAn.utils.XDate;
import coffee.app.DucAn.model.HoaDon;

public class HoaDonDAO {
    CoffeeDB coffeeDB;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public HoaDonDAO(Context context) {
        this.coffeeDB = new CoffeeDB(context);
    }

    @SuppressLint("Range")
    public ArrayList<HoaDon> get(String sql, String... choose) {
        ArrayList<HoaDon> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = coffeeDB.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, choose);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                HoaDon hoaDon = new HoaDon();
                hoaDon.setMaHoaDon(cursor.getInt(cursor.getColumnIndex("maHoaDon")));
                hoaDon.setMaBan(cursor.getInt(cursor.getColumnIndex("maBan")));
                try {
                    hoaDon.setGioVao(XDate.toDateTime(cursor.getString(cursor.getColumnIndex("gioVao"))));
                    hoaDon.setGioRa(XDate.toDateTime(cursor.getString(cursor.getColumnIndex("gioRa"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                hoaDon.setTrangThai(cursor.getInt(cursor.getColumnIndex("trangThai")));
                hoaDon.setMaKhachHang(cursor.getString(cursor.getColumnIndex("maKhachHang")));
                hoaDon.setTrangThai2(cursor.getInt(cursor.getColumnIndex("trangthai2")));
                hoaDon.setGhiChu(cursor.getString(cursor.getColumnIndex("ghiChu")));
                list.add(hoaDon);
                Log.i("TAG", hoaDon.toString());
            } while (cursor.moveToNext());
        }

        return list;
    }

    public ArrayList<HoaDon> getAll() {
        String sql = "SELECT * FROM HOADON";
        return get(sql);
    }

    public boolean insertHoaDon(HoaDon hoaDon) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maBan", hoaDon.getMaBan());
        values.put("gioVao", XDate.toStringDateTime(hoaDon.getGioVao()));
        values.put("gioRa", XDate.toStringDateTime(hoaDon.getGioRa()));
        values.put("trangThai", hoaDon.getTrangThai());
        values.put("maKhachHang", hoaDon.getMaKhachHang());
        values.put("trangthai2", 0);
        values.put("ghiChu", hoaDon.getGhiChu());
        long check = sqLiteDatabase.insert("HOADON", null, values);

        return check != -1;
    }

    public boolean updateHoaDon(HoaDon hoaDon) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maBan", hoaDon.getMaBan());
        values.put("gioVao", XDate.toStringDateTime(hoaDon.getGioVao()));
        values.put("gioRa", XDate.toStringDateTime(hoaDon.getGioRa()));
        values.put("trangThai", hoaDon.getTrangThai());
        values.put("maKhachHang", hoaDon.getMaKhachHang());
        values.put("trangthai2", hoaDon.getTrangThai());
        values.put("ghiChu", hoaDon.getGhiChu());
        int check = sqLiteDatabase.update("HOADON", values, "maHoaDon=?", new String[]{String.valueOf(hoaDon.getMaHoaDon())});

        return check > 0;
    }

    public boolean deleteHoaDon(String maHoaDon) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getWritableDatabase();
        int check = sqLiteDatabase.delete("HOADON", "maHoaDon=?", new String[]{String.valueOf(maHoaDon)});

        return check > 0;
    }

    public HoaDon getByMaHoaDon(String maHoaDon) {
        String sql = "SELECT * FROM HOADON WHERE maHoaDon=?";
        ArrayList<HoaDon> list = get(sql, maHoaDon);

        return list.get(0);
    }

    public HoaDon getByMaHoaDonVaTrangThai(String maBan, int trangThai) {
        String sql = "SELECT * FROM HOADON WHERE maBan=? AND trangThai=?";
        ArrayList<HoaDon> list = get(sql, maBan, String.valueOf(trangThai));

        return list.get(0);
    }

    public ArrayList<HoaDon> getByTrangThai(int status) {
        String sql = "SELECT * FROM HOADON WHERE trangThai=?";

        return get(sql, String.valueOf(status));
    }

    //get all by maKhachHang
    public ArrayList<HoaDon> getByMaKhachHang(String maKhachHang) {
        //trạng thái đã thanh toán
        String sql = "SELECT * FROM HOADON WHERE maKhachHang=? AND trangThai=?";
        return get(sql, maKhachHang, String.valueOf(HoaDon.DA_THANH_TOAN));
    }
}
