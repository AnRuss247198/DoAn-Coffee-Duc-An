package coffee.app.DucAn.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import coffee.app.DucAn.R;
import coffee.app.DucAn.adapter.HoaDonAdapter;
import coffee.app.DucAn.dao.HoaDonDAO;
import coffee.app.DucAn.dao.HoaDonMangVeDao;
import coffee.app.DucAn.interfaces.ItemHoaDonOnClick;
import coffee.app.DucAn.model.HoaDon;
import coffee.app.DucAn.model.HoaDonMangVe;

public class HoaDonNguoiDungActivity extends AppCompatActivity {
    public static final String MA_HOA_DON = "maHoaDon";
    Toolbar toolbar;
    RecyclerView recyclerViewHoaDon;
    HoaDonMangVeDao hoaDonDAO;
    HoaDonDAO   hoaDonDao;
    String maKhachHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_nguoi_dung);

        initToolBar();
        maKhachHang = getIntent().getStringExtra("maNguoiDung");
        initView();
        hoaDonDAO = new HoaDonMangVeDao(this);
        hoaDonDao = new HoaDonDAO(this);
        loadData();
    }
    private void loadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewHoaDon.setLayoutManager(linearLayoutManager);
        List<HoaDonMangVe> hoaDonList = hoaDonDAO.getAllByMaKhachHang(maKhachHang);
        List<HoaDon> hoaDonList2 = hoaDonDao.getByMaKhachHang(maKhachHang);
        for (HoaDonMangVe hoaDonMangVe : hoaDonList) {
            //int maHoaDon, Date gioVao, Date gioRa, int trangThai, String maKhachHang)
            hoaDonList2.add(new HoaDon(hoaDonMangVe.getMaHoaDon(), hoaDonMangVe.getGioVao(), hoaDonMangVe.getGioRa(), hoaDonMangVe.getTrangThai(), hoaDonMangVe.getMaKhachHang(),-1));
        }
        HoaDonAdapter adapter = new HoaDonAdapter( HoaDonNguoiDungActivity.this,hoaDonList2, new ItemHoaDonOnClick() {
            @Override
            public void itemOclick(View view, HoaDon hoaDon) {
                Intent intent = new Intent(HoaDonNguoiDungActivity.this, ChiTietHoaDonActivity.class);
                intent.putExtra(MA_HOA_DON, String.valueOf(hoaDon.getMaHoaDon()));
                intent.putExtra("maKhachHang", hoaDon.getMaKhachHang());
                Log.e("maKhachHang", hoaDon.getMaKhachHang());
                intent.putExtra("maBan", hoaDon.getMaBan());
                Log.e("maBan", hoaDon.getMaBan()+"");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
//            @Override
//            public void itemHuy(View view, HoaDonMangVe hoaDonMangVe) {
//                hoaDonMangVe.setTrangThai(HoaDonMangVe.HUY_HOA_DON);
//                hoaDonDAO.updateHoaDonMangVe(hoaDonMangVe);
//                loadData();
//            }
        });
        recyclerViewHoaDon.setAdapter(adapter);
    }

    private void initView() {
        recyclerViewHoaDon = findViewById(R.id.recyclerViewHoaDon);
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbarHoaDon);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}