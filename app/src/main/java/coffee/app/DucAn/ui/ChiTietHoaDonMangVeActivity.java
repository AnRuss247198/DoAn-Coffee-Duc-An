package coffee.app.DucAn.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import coffee.app.DucAn.R;
import coffee.app.DucAn.adapter.ChiTietHoaDonAdapter;
import coffee.app.DucAn.dao.HangHoaDAO;
import coffee.app.DucAn.dao.HoaDonChiTietDAO;
import coffee.app.DucAn.dao.HoaDonMangVeDao;
import coffee.app.DucAn.dao.NguoiDungDAO;
import coffee.app.DucAn.model.HangHoa;
import coffee.app.DucAn.model.HoaDonChiTiet;
import coffee.app.DucAn.model.HoaDonMangVe;
import coffee.app.DucAn.utils.MyToast;
import coffee.app.DucAn.utils.XDate;

public class ChiTietHoaDonMangVeActivity extends AppCompatActivity {
    RecyclerView recyclerViewThucUong;
    HoaDonChiTietDAO hoaDonChiTietDAO;
    HangHoaDAO hangHoaDAO;
    HoaDonMangVeDao hoaDonDAO;
    NguoiDungDAO nguoiDungDAO;
    TextView tvMaBan, tvGioVao, tvGioRa,tvGhiChu;
    ImageView ivBack;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don_mang_ve);
        initView();
        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);
        hangHoaDAO = new HangHoaDAO(this);
        hoaDonDAO = new HoaDonMangVeDao(this);
        nguoiDungDAO = new NguoiDungDAO(this);
        loadData();
        fillActivity();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComfirmDeleteHDCT();
            }
        });

    }

    private void showComfirmDeleteHDCT() {
        // Xoá hoá đơn chi tiết và hoá đơn (Theo mã hoá đơn)
        AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietHoaDonMangVeActivity.this, R.style.AlertDialogTheme)
                .setMessage("Bạn có muốn xoá hoá đơn HD0775098507" + getMaHoaDon() + "?")
                .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (hoaDonDAO.deleteHoaDonMangVe(getMaHoaDon()) && hoaDonChiTietDAO.deleteHoaDonChiTietByMaHoaDon(getMaHoaDon())) {
                            MyToast.successful(ChiTietHoaDonMangVeActivity.this, "Xoá thành công");
                            onBackPressed();
                        } else {
                            MyToast.error(ChiTietHoaDonMangVeActivity.this, "Xoá không thành côn");
                        }
                    }
                })
                .setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void fillActivity() {
        HoaDonMangVe hoaDon = hoaDonDAO.getByMaHoaDon(getMaHoaDon());
        Log.e("HoaDon>>>>>", hoaDon.getMaKhachHang());
        String ten = nguoiDungDAO.getByMaNguoiDung(hoaDon.getMaKhachHang()).getHoVaTen();
        tvMaBan.setText("" + ten);
        tvGioVao.setText(XDate.toStringDateTime(hoaDon.getGioVao()));
        tvGioRa.setText(XDate.toStringDateTime(hoaDon.getGioRa()));
        tvGhiChu.setText("Ghi chú: "+hoaDon.getGhiChu());
    }

    private String getMaHoaDon() {
        Intent intent = getIntent();
        return intent.getStringExtra(HoaDonActivity.MA_HOA_DON);
    }

    private void loadData() {
        String maHoaDon = getMaHoaDon();
        ArrayList<HoaDonChiTiet> listHDCT = hoaDonChiTietDAO.getByMaHoaDon(maHoaDon);
        ArrayList<HangHoa> list = new ArrayList<>();
        for (int i = 0; i < listHDCT.size(); i++) {
            list.add(hangHoaDAO.getByMaHangHoa(String.valueOf(listHDCT.get(i).getMaHangHoa())));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewThucUong.setLayoutManager(linearLayoutManager);
        ChiTietHoaDonAdapter adapter = new ChiTietHoaDonAdapter(this, list, listHDCT);
        recyclerViewThucUong.setAdapter(adapter);
    }

    private void initView() {
        recyclerViewThucUong = findViewById(R.id.recyclerViewThucUong);
        tvMaBan = findViewById(R.id.tvMaBan);
        tvGioVao = findViewById(R.id.tvGioVao);
        tvGioRa = findViewById(R.id.tvGioRa);
        ivBack = findViewById(R.id.ivBack);
        btnDelete = findViewById(R.id.btnDelete);
        tvGhiChu = findViewById(R.id.tvGhiChu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
    }
}