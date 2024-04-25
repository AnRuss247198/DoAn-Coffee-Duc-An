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
import java.util.List;
import java.util.Random;

import coffee.app.DucAn.database.CoffeeDB;
import coffee.app.DucAn.model.HoaDonMangVe;
import coffee.app.DucAn.utils.XDate;

public class HoaDonMangVeDao {
    CoffeeDB coffeeDB;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public HoaDonMangVeDao(Context context) {
        this.coffeeDB = new CoffeeDB(context);
    }

    @SuppressLint("Range")
    public List<HoaDonMangVe> get(String sql, String... choose) {
        List<HoaDonMangVe> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = coffeeDB.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(sql, choose);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                HoaDonMangVe hoaDonMangVe = new HoaDonMangVe();
                hoaDonMangVe.setMaHoaDon(cursor.getInt(cursor.getColumnIndex("maHoaDon")));
                try {
                    hoaDonMangVe.setGioVao(XDate.toDateTime(cursor.getString(cursor.getColumnIndex("gioVao"))));
                    hoaDonMangVe.setGioRa(XDate.toDateTime(cursor.getString(cursor.getColumnIndex("gioRa"))));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                hoaDonMangVe.setTrangThai(cursor.getInt(cursor.getColumnIndex("trangThai")));
                hoaDonMangVe.setMaKhachHang(cursor.getString(cursor.getColumnIndex("maKhachHang")));
                hoaDonMangVe.setGhiChu(cursor.getString(cursor.getColumnIndex("ghiChu")));
                list.add(hoaDonMangVe);
                Log.i("TAG", hoaDonMangVe.toString());
            } while (cursor.moveToNext());
        }

        return list;
    }

    public List<HoaDonMangVe> getAll() {
        String sql = "SELECT * FROM HOADONMANGVE";
        return get(sql);
    }
    public boolean insertHoaDonMangVe(HoaDonMangVe hoaDonMangVe) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maHoaDon", new Random().nextInt(999999 - 9999) + 9999);
        values.put("maKhachHang", hoaDonMangVe.getMaKhachHang());
        values.put("gioVao", spf.format(hoaDonMangVe.getGioVao()));
        values.put("gioRa", spf.format(hoaDonMangVe.getGioRa()));
        values.put("trangThai", hoaDonMangVe.getTrangThai());
        values.put("ghiChu", hoaDonMangVe.getGhiChu());
        long check = sqLiteDatabase.insert("HOADONMANGVE", null, values);

        return check != -1;
    }

    //check hoa don chua duyet by manguoidung
    public boolean checkHoaDonChuaDuyet(String maKhachHang) {
        //or trangThai= CHUA_DUYET or trangThai= CHUA_XAC_NHAN
        String sql = "SELECT * FROM HOADONMANGVE WHERE maKhachHang=? AND trangThai=?";
        List<HoaDonMangVe> list = get(sql, maKhachHang, String.valueOf(HoaDonMangVe.CHUA_DUYET));
        return list.size() > 0;
    }

    //check hoa don chua duyet by manguoidung
    public boolean checkHoaDonChuaXacNhan(String maKhachHang) {
        //or trangThai= CHUA_DUYET or trangThai= CHUA_XAC_NHAN
        String sql = "SELECT * FROM HOADONMANGVE WHERE maKhachHang=? AND trangThai=?";
        List<HoaDonMangVe> list = get(sql, maKhachHang, String.valueOf(HoaDonMangVe.CHUA_XAC_NHAN));
        return list.size() > 0;
    }

    public boolean deleteHoaDonMangVe(String maHoaDon) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getWritableDatabase();
        int check = sqLiteDatabase.delete("HOADONMANGVE", "maHoaDon=?", new String[]{maHoaDon});

        return check > 0;
    }

    public boolean updateHoaDonMangVe(HoaDonMangVe hoaDonMangVe) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maKhachHang", hoaDonMangVe.getMaKhachHang());
        values.put("gioVao", spf.format(hoaDonMangVe.getGioVao()));
        values.put("gioRa", spf.format(hoaDonMangVe.getGioRa()));
        values.put("trangThai", hoaDonMangVe.getTrangThai());
        values.put("ghiChu", hoaDonMangVe.getGhiChu());
        int check = sqLiteDatabase.update("HOADONMANGVE", values, "maHoaDon=?", new String[]{String.valueOf(hoaDonMangVe.getMaHoaDon())});

        return check > 0;
    }

    public HoaDonMangVe getByMaHoaDon(String maHoaDon) {
        String sqlGetByMaLoai = "SELECT * FROM HOADONMANGVE WHERE maHoaDon=?";
        List<HoaDonMangVe> list = get(sqlGetByMaLoai, maHoaDon);

        return list.get(0);
    }

    //getAll hoa don by manguoidung
    public List<HoaDonMangVe> getAllByMaKhachHang(String maKhachHang) {
        //trạng thái đã duyệt và chưa duyệt
        String sql = "SELECT * FROM HOADONMANGVE WHERE maKhachHang=? AND trangThai=?";
        return get(sql, maKhachHang,String.valueOf(HoaDonMangVe.DA_DUYET));
    }

    //get one hoa don by manguoidung and trangthai 1
    public HoaDonMangVe getByMaHoaDonVaTrangThai(String maKhachHang) {
        //khôgn lấy trạng thái hủy hoá đơn và đã duyệt
        String sql = "SELECT * FROM HOADONMANGVE WHERE maKhachHang=? AND trangThai=?";
        List<HoaDonMangVe> list = get(sql, maKhachHang, String.valueOf(HoaDonMangVe.CHUA_DUYET));
        if(list.size()==0){
            list = get(sql, maKhachHang, String.valueOf(HoaDonMangVe.CHUA_XAC_NHAN));
        }
        return list.get(0);
    }

    public List<HoaDonMangVe> getByTrangThai(int ChuaDuyet) {
        String sql = "SELECT * FROM HOADONMANGVE WHERE trangThai=?";
        return get(sql, String.valueOf(ChuaDuyet));
    }
    @SuppressLint("Range")
    public int getGiaTien(int maHoaDon) {
        SQLiteDatabase sqLiteDatabase = coffeeDB.getReadableDatabase();
        String sql = "SELECT SUM(giaTien) as DoanhThu FROM HOADONCHITIET WHERE maHoaDon=?";
        ArrayList<Integer> list = new ArrayList<>();
        @SuppressLint("Recycle")
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{String.valueOf(maHoaDon)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                try {
                    list.add(cursor.getInt(cursor.getColumnIndex("DoanhThu")));
                } catch (Exception e) {
                    list.add(0);
                }
            } while (cursor.moveToNext());
        }
        return list.get(0);
    }
}
