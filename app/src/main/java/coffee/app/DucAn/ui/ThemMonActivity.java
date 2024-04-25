package coffee.app.DucAn.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import coffee.app.DucAn.R;
import coffee.app.DucAn.adapter.ThucUongOderThemAdapter;
import coffee.app.DucAn.dao.HangHoaDAO;
import coffee.app.DucAn.dao.HoaDonChiTietDAO;
import coffee.app.DucAn.dao.LoaiHangDAO;
import coffee.app.DucAn.interfaces.ItemOderOnClick;
import coffee.app.DucAn.model.HangHoa;
import coffee.app.DucAn.model.HoaDonChiTiet;
import coffee.app.DucAn.utils.MyToast;

public class ThemMonActivity extends AppCompatActivity {
    Toolbar toolbar;
    LoaiHangDAO loaiHangDAO;
    HangHoaDAO hangHoaDAO;
    RecyclerView recyclerViewThucUongOder;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_them_mon);
        initToolBar();
        initView();

        loaiHangDAO = new LoaiHangDAO(this);
        hangHoaDAO = new HangHoaDAO(this);
        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);


        loadData();
    }

    private void loadData() {
        // Hiên thị danh sách Loại thức uống
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewThucUongOder.setLayoutManager(linearLayoutManager);

        ThucUongOderThemAdapter thucUongAdapter = new ThucUongOderThemAdapter(hangHoaDAO.getByTrangThai(String.valueOf(HangHoa.STATUS_STILL)), new ItemOderOnClick() {
            @Override
            public void itemOclick(View view, HangHoa hangHoa) {
                Intent intent = getIntent();
                String maHoaDon = intent.getStringExtra(MangVeActivity.MA_HOA_DON);
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setMaHoaDon(Integer.parseInt(maHoaDon));
                hoaDonChiTiet.setMaHangHoa(hangHoa.getMaHangHoa());
                hoaDonChiTiet.setSoLuong(1);
                hoaDonChiTiet.setGiaTien(hangHoa.getGiaTien() * hoaDonChiTiet.getSoLuong());
                Calendar calendar = Calendar.getInstance();
                hoaDonChiTiet.setNgayXuatHoaDon(calendar.getTime());
                if(hoaDonChiTietDAO.insertHoaDonChiTiet(hoaDonChiTiet)){
                    MyToast.successful(ThemMonActivity.this, "Thêm thành công "+hangHoa.getTenHangHoa());
                }
            }
        });
        recyclerViewThucUongOder.setAdapter(thucUongAdapter);
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbarThemMon);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        recyclerViewThucUongOder = findViewById(R.id.recyclerViewThucUongOder);
    }

}