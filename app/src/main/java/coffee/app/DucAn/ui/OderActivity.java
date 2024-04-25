package coffee.app.DucAn.ui;

import static coffee.app.DucAn.ui.SignInActivity._maNguoiDung;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;

import coffee.app.DucAn.R;
import coffee.app.DucAn.adapter.HoaDonChiTietMainAdapter;
import coffee.app.DucAn.dao.BanDAO;
import coffee.app.DucAn.dao.HangHoaDAO;
import coffee.app.DucAn.dao.HoaDonChiTietDAO;
import coffee.app.DucAn.dao.HoaDonDAO;
import coffee.app.DucAn.dao.NguoiDungDAO;
import coffee.app.DucAn.dao.ThongBaoDAO;
import coffee.app.DucAn.interfaces.ItemTangGiamSoLuongOnClick;
import coffee.app.DucAn.model.Ban;
import coffee.app.DucAn.model.HangHoa;
import coffee.app.DucAn.model.HoaDon;
import coffee.app.DucAn.model.HoaDonChiTiet;
import coffee.app.DucAn.model.ThongBao;
import coffee.app.DucAn.utils.MyToast;
import coffee.app.DucAn.utils.XDate;

public class OderActivity extends AppCompatActivity {
    public static final String MA_HOA_DON = "maHoaDon";
    HoaDonChiTietDAO hoaDonChiTietDAO;
    HangHoaDAO hangHoaDAO;
    HoaDonDAO hoaDonDAO;
    BanDAO banDAO;
    TextView tvMaBan, tvGioVao, tvThemMon, tvTamTinh, tvHoaDonCuoi,tvnguoi;
    RecyclerView recyclerViewThucUong;
    Button btnThanhToan;
    Toolbar toolbar;
    public static  String maBan = "";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder);
        initToolbar();
        initview();

        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);
        hangHoaDAO = new HangHoaDAO(this);
        hoaDonDAO = new HoaDonDAO(this);
        banDAO = new BanDAO(this);

        sharedPreferences = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        fillActivity();
        loadData();
        tvThemMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openThemMonActivity();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thanhToanHoaDon();
            }
        });

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarOder);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initview() {
        tvMaBan = findViewById(R.id.tvMaBan);
        tvGioVao = findViewById(R.id.tvGioVao);
        recyclerViewThucUong = findViewById(R.id.recyclerViewThucUong);
        tvThemMon = findViewById(R.id.tvThemMon);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        tvTamTinh = findViewById(R.id.tvTamTinh);
        tvHoaDonCuoi = findViewById(R.id.tvHoaDonCuoi);
        tvnguoi = findViewById(R.id.tvnguoidat);
    }

    private void openThemMonActivity() {
        // Mở màng hình thêm món
        HoaDon hoaDon = getHoaDon();
        Intent intent = new Intent(OderActivity.this, ThemMonActivity.class);
        intent.putExtra(MA_HOA_DON, String.valueOf(hoaDon.getMaHoaDon()));
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void thanhToanHoaDon() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_thanhtoan);

        TextView tvMahoaDon = dialog.findViewById(R.id.tvMaHoaDon);
        TextView tvMaBan = dialog.findViewById(R.id.tvMaBan);
        TextView tvGioVaoTT = dialog.findViewById(R.id.tvGioVao);
        TextView tvHoaDonCuoi = dialog.findViewById(R.id.tvHoaDonCUoi);
        TextView tvnguoi = dialog.findViewById(R.id.tvnguoidat);
        TextView tvTongTien = dialog.findViewById(R.id.tvTongTien);
        TextView tvCancle= dialog.findViewById(R.id.tvCancle);
        EditText edtGhiChu = dialog.findViewById(R.id.edtGhiChu);
        Button btnPay = dialog.findViewById(R.id.btnPay);

        tvMahoaDon.setText("HD0"+getHoaDon().getMaHoaDon());
        tvMaBan.setText("B0"+getHoaDon().getMaBan());
        tvGioVaoTT.setText(XDate.toStringDateTime(getHoaDon().getGioVao()));
        tvTongTien.setText(hoaDonChiTietDAO.getGiaTien(getHoaDon().getMaHoaDon()) + "VND");
        tvHoaDonCuoi.setText(hoaDonChiTietDAO.getGiaTien(getHoaDon().getMaHoaDon()) + "VND");
        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO(this);
        Log.e("nguoiAIII",_maNguoiDung);
        String ma = sharedPreferences.getString("maNguoiDung", "");
        String ten = nguoiDungDAO.getByMaNguoiDung(ma).getHoVaTen();
        tvnguoi.setText(ten);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thanh toán hoá đơn
                HoaDon hoaDon = getHoaDon();
                hoaDon.setTrangThai(HoaDon.DA_THANH_TOAN); // cập nhật lại trạng thái đã thanh toán
                Calendar calendar = Calendar.getInstance();
                hoaDon.setGioRa(calendar.getTime());// cập nhật lại giờ ra
                hoaDon.setGhiChu(edtGhiChu.getText().toString());
                _maNguoiDung = sharedPreferences.getString("maNguoiDung", "");
                hoaDon.setMaKhachHang(_maNguoiDung);
                Intent intent = getIntent();
                String maBan = intent.getStringExtra(QuanLyBanActivity.MA_BAN);
                Ban ban = banDAO.getByMaBan(maBan);
                ban.setTrangThai(Ban.CON_TRONG); // cập nhật lại trạng thái còn trống
                Intent intent1 = new Intent(OderActivity.this, ChuyenKhoanActivity.class);
                intent1.putExtra("ban",ban);
                intent1.putExtra("hoaDon",hoaDon);
                startActivity(intent1);
                finish();
//                if (banDAO.updateBan(ban) && hoaDonDAO.updateHoaDon(hoaDon)) {
//                    MyToast.successful(OderActivity.this, "Thanh Toán thành công");
//                    MyNotification.getNotification(OderActivity.this, "Thanh toán thành công hoá đơn HD0775098507" + hoaDon.getMaHoaDon());
//                    themThonBaoMoi(hoaDon, calendar);
//                }
//                onBackPressed();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void themThonBaoMoi(HoaDon hoaDon, Calendar calendar) {
        // Tạo thông báo thanh toán hoá đơn
        ThongBao thongBao = new ThongBao();
        thongBao.setNoiDung("Thanh toán thành công hoá đơn HD0775098507"+ hoaDon.getMaHoaDon());
        thongBao.setTrangThai(ThongBao.STATUS_CHUA_XEM);
        thongBao.setNgayThongBao(calendar.getTime());
        ThongBaoDAO thongBaoDAO = new ThongBaoDAO(OderActivity.this);
        thongBaoDAO.insertThongBao(thongBao);
    }

    private void loadData() {
        HoaDon hoaDon = getHoaDon();
        ArrayList<HoaDonChiTiet> listHDCT = hoaDonChiTietDAO.getByMaHoaDon(String.valueOf(hoaDon.getMaHoaDon())); // lấy All hoá đơn chi tiết theo mã hoá đơn
        ArrayList<HangHoa> list = new ArrayList<>();
        for (int i = 0; i < listHDCT.size(); i++) {
            list.add(hangHoaDAO.getByMaHangHoa(String.valueOf(listHDCT.get(i).getMaHangHoa())));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewThucUong.setLayoutManager(linearLayoutManager);
        HoaDonChiTietMainAdapter adapter = new HoaDonChiTietMainAdapter(this, list, listHDCT, new ItemTangGiamSoLuongOnClick() {
            @Override
            public void itemOclick(View view, int indext, HoaDonChiTiet hoaDonChiTiet, HangHoa hangHoa) {
                hoaDonChiTiet.setSoLuong(indext);
                hoaDonChiTiet.setGiaTien(indext * hangHoa.getGiaTien());
                hoaDonChiTietDAO.updateHoaDonChiTiet(hoaDonChiTiet);
                fillActivity();
            }

            @Override
            public void itemOclickDeleteHDCT(View view, HoaDonChiTiet hoaDonChiTiet) {
                // Xoá oder
                AlertDialog.Builder builder = new AlertDialog.Builder(OderActivity.this, R.style.AlertDialogTheme);
                builder.setMessage("Xoá oder ?");
                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(hoaDonChiTietDAO.deleteHoaDonChiTiet(String.valueOf(hoaDonChiTiet.getMaHDCT()))){
                            MyToast.successful(OderActivity.this, "Xoá oder thành công");
                            loadData();
                            fillActivity();
                        }else {
                            MyToast.error(OderActivity.this, "Xoá không thành công");
                        }
                    }
                });
                builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        recyclerViewThucUong.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    private void fillActivity() {
        HoaDon hoaDon = getHoaDon();
        tvMaBan.setText("Bàn BO" + hoaDon.getMaBan());
        tvGioVao.setText(XDate.toStringDateTime(hoaDon.getGioVao()));
        tvTamTinh.setText(hoaDonChiTietDAO.getGiaTien(hoaDon.getMaHoaDon()) + "VND");
        tvHoaDonCuoi.setText(hoaDonChiTietDAO.getGiaTien(hoaDon.getMaHoaDon()) + "VND");
    }

    private HoaDon getHoaDon() {
        Intent intent = getIntent();
        if(maBan.equals("")){
            maBan = intent.getStringExtra("maBan");
        }
        return hoaDonDAO.getByMaHoaDonVaTrangThai(maBan, HoaDon.CHUA_THANH_TOAN) != null ? hoaDonDAO.getByMaHoaDonVaTrangThai(maBan, HoaDon.CHUA_THANH_TOAN) : new HoaDon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        fillActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_in_left, R.anim.anim_out_right);
    }
}