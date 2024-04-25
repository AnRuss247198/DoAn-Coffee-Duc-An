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

public class HoaDonActivity extends AppCompatActivity {
    public static final String MA_HOA_DON = "maHoaDon";
    Toolbar toolbar;
    RecyclerView recyclerViewHoaDon;
    HoaDonDAO hoaDonDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        initToolBar();

        initView();
        hoaDonDAO = new HoaDonDAO(this);
        loadData();

    }

    private void loadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewHoaDon.setLayoutManager(linearLayoutManager);
        List<HoaDon> list = hoaDonDAO.getByTrangThai(HoaDon.DA_THANH_TOAN);
        HoaDonMangVeDao hoaDonMangVeDao = new HoaDonMangVeDao(this);
        List<HoaDonMangVe> listHoaDonMangVe = hoaDonMangVeDao.getByTrangThai(HoaDonMangVe.DA_DUYET);
        for (HoaDonMangVe hoaDonMangVe : listHoaDonMangVe) {
            //int maHoaDon, Date gioVao, Date gioRa, int trangThai, String maKhachHang)
            list.add(new HoaDon(hoaDonMangVe.getMaHoaDon(), hoaDonMangVe.getGioVao(), hoaDonMangVe.getGioRa(), hoaDonMangVe.getTrangThai(), hoaDonMangVe.getMaKhachHang(),-1));
        }
        HoaDonAdapter adapter = new HoaDonAdapter(HoaDonActivity.this, list, new ItemHoaDonOnClick() {
            @Override
            public void itemOclick(View view, HoaDon hoaDon) {
                Intent intent = new Intent(HoaDonActivity.this, ChiTietHoaDonActivity.class);
                intent.putExtra(MA_HOA_DON, String.valueOf(hoaDon.getMaHoaDon()));
                intent.putExtra("maKhachHang", hoaDon.getMaKhachHang());
                Log.e("maKhachHang", hoaDon.getMaKhachHang());
                intent.putExtra("maBan", hoaDon.getMaBan());
                Log.e("maBan", hoaDon.getMaBan()+"");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
            }
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