package coffee.app.DucAn.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import coffee.app.DucAn.R;
import coffee.app.DucAn.adapter.TopSanPhamAdapter;
import coffee.app.DucAn.model.HoaDonChiTiet;

public class TopSanPhamActivity extends AppCompatActivity {

     RecyclerView recyclerView;
    List<HoaDonChiTiet> hoaDonChiTietList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_san_pham);

        initView();
        hoaDonChiTietList = (List<HoaDonChiTiet>) getIntent().getSerializableExtra("list");
        TopSanPhamAdapter topSanPhamAdapter = new TopSanPhamAdapter(this, hoaDonChiTietList);
        recyclerView.setAdapter(topSanPhamAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerViewTopSanPham);
    }
}