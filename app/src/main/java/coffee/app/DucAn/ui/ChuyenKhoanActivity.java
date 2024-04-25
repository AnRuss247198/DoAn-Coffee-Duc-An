package coffee.app.DucAn.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import coffee.app.DucAn.R;
import coffee.app.DucAn.dao.BanDAO;
import coffee.app.DucAn.dao.HoaDonChiTietDAO;
import coffee.app.DucAn.dao.HoaDonDAO;
import coffee.app.DucAn.dao.NguoiDungDAO;
import coffee.app.DucAn.dao.ThongBaoDAO;
import coffee.app.DucAn.model.Ban;
import coffee.app.DucAn.model.HoaDon;
import coffee.app.DucAn.model.NguoiDung;
import coffee.app.DucAn.model.ThongBao;
import coffee.app.DucAn.notification.MyNotification;
import coffee.app.DucAn.utils.Loading;
import coffee.app.DucAn.utils.MyToast;
import vn.momo.momo_partner.AppMoMoLib;

import static coffee.app.DucAn.ui.OderActivity.maBan;
import static coffee.app.DucAn.ui.SignInActivity._maNguoiDung;

public class ChuyenKhoanActivity extends AppCompatActivity {
    private static final String EXTRA_HOADON = "hoaDon";
    private static final String EXTRA_BAN = "ban";

    private TextView tvTienMat;
    private TextView tvMoMo;
    private TextView tvchuyenkhoan;
    private HoaDonDAO hoaDonDAO;
    private HoaDonChiTietDAO hoaDonChiTietDAO;
    private NguoiDungDAO nguoiDungDAO;
    private BanDAO banDAO;
    HoaDon _hoaDon;
    Ban _ban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chuyenkhoan);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

        tvTienMat = findViewById(R.id.tvTienMat);
        tvMoMo = findViewById(R.id.tvMoMo);
        tvchuyenkhoan = findViewById(R.id.tvchuyenkhoan);
        hoaDonDAO = new HoaDonDAO(this);
        hoaDonChiTietDAO = new HoaDonChiTietDAO(this);
        nguoiDungDAO = new NguoiDungDAO(this);
        banDAO = new BanDAO(this);

        NguoiDung nguoiDung = nguoiDungDAO.getByMaNguoiDung(_maNguoiDung);
        if (!nguoiDung.getChucVu().equals(NguoiDung.POSITION_CUSTOMER)) {
            tvMoMo.setVisibility(View.GONE);
        }

        // Check for data passed from the previous activity
        Intent intent = getIntent();
        if (intent != null) {
            _hoaDon = (HoaDon) intent.getSerializableExtra(EXTRA_HOADON);
            _ban = (Ban) intent.getSerializableExtra(EXTRA_BAN);
            if (_hoaDon != null && _ban != null) {
                setupPaymentMethods(_hoaDon, _ban);
            }
        }
    }

    private void setupPaymentMethods(HoaDon hoaDon, Ban ban) {
        tvchuyenkhoan.setText(hoaDonChiTietDAO.getGiaTien(hoaDon.getMaHoaDon()) + "");
        tvTienMat.setOnClickListener(v -> handleCashPayment(hoaDon, ban));
        tvMoMo.setOnClickListener(v -> handleMoMoPayment(hoaDon, ban));
    }

    private void handleCashPayment(HoaDon hoaDon, Ban ban) {
        Calendar calendar = Calendar.getInstance();
        hoaDon.setTrangThai2(1);
        if (banDAO.updateBan(ban) && hoaDonDAO.updateHoaDon(hoaDon)) {
            MyToast.successful(ChuyenKhoanActivity.this, "Thanh Toán thành công");
            MyNotification.getNotification(ChuyenKhoanActivity.this, "Thanh toán thành công hoá đơn HD0775098507" + hoaDon.getMaHoaDon());
            addNewNotification(hoaDon, calendar);
        }
        Intent intent = new Intent(ChuyenKhoanActivity.this, QuanLyBanActivity.class);
        maBan = "";
        startActivity(intent);
        finish();
    }

    private void handleMoMoPayment(HoaDon hoaDon, Ban ban) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = createMoMoPaymentParams(hoaDon);
        AppMoMoLib.getInstance().requestMoMoCallBack(ChuyenKhoanActivity.this, eventValue);
    }

    private void addNewNotification(HoaDon hoaDon, Calendar calendar) {
        ThongBao thongBao = new ThongBao();
        thongBao.setNoiDung("Thanh toán thành công hoá đơn HD0775098507" + hoaDon.getMaHoaDon());
        thongBao.setTrangThai(ThongBao.STATUS_CHUA_XEM);
        thongBao.setNgayThongBao(calendar.getTime());
        ThongBaoDAO thongBaoDAO = new ThongBaoDAO(ChuyenKhoanActivity.this);
        thongBaoDAO.insertThongBao(thongBao);
    }

    private Map<String, Object> createMoMoPaymentParams(HoaDon hoaDon) {
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put("merchantname", "Afforda Company - Nguyen Van Chinh");
        eventValue.put("merchantcode", "MOMO1NRV20220112");
        eventValue.put("amount", hoaDonChiTietDAO.getGiaTien(hoaDon.getMaHoaDon()));
        eventValue.put("orderId", "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("orderLabel", "Đức An Coffee");
        eventValue.put("merchantnamelabel", "Dịch vụ");
        eventValue.put("fee", 0);
        eventValue.put("description", "Thanh toán hóa đơn Đức An Coffee");
        eventValue.put("requestId", "MOMO1NRV20220112_merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", "MOMO1NRV20220112");
        eventValue.put("extra", "");
        eventValue.put("requestType", "captureMoMoWallet");
        eventValue.put("signature", "");
        eventValue.put("partnerName", "Viettel Post");
        return eventValue;
    }

    // onActivityResult method to handle MoMo payment callback
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                int status = data.getIntExtra("status", -1);
                Log.e("messageAPI", "status: " + status);
                switch (status) {
                    case 0:
                        handleMoMoPaymentSuccess(data);
                        break;
                    case 1:
                        handleMoMoPaymentFailure(data);
                        break;
                    case 2:
                        handleMoMoPaymentNotSupported();
                        break;
                    default:
                        Log.e("messageAPI", "Không nhận được token");
                        break;
                }
            } else {
                Log.e("messageAPI", "Không nhận được data");
            }
        } else {
            Log.e("messageAPI", "Không nhận được requestCode");
        }
    }

    private void handleMoMoPaymentSuccess(Intent data) {
        String token = data.getStringExtra("data");
        String phoneNumber = data.getStringExtra("phonenumber");
        String env = data.getStringExtra("env");
        if (env == null) {
            env = "app";
        }

        if (token != null && !token.equals("")) {
            // Send phoneNumber & token to your server side to process payment with MoMo server
            // If Momo topup success, continue to process your order
            Log.e("messageAPI", "receive token");
            // send token to your server
            //thanh toán hoá đơn
            Calendar calendar = Calendar.getInstance();
            _hoaDon.setTrangThai2(0);
            if (banDAO.updateBan(_ban) && hoaDonDAO.updateHoaDon(_hoaDon)) {
                MyToast.successful(ChuyenKhoanActivity.this, "Thanh Toán thành công");
                MyNotification.getNotification(ChuyenKhoanActivity.this, "Thanh toán thành công hoá đơn HD0775098507" + _hoaDon.getMaHoaDon());
                addNewNotification(_hoaDon, calendar);
            }
            Loading loading = new Loading(ChuyenKhoanActivity.this);
            loading.startLoading();
            new Handler().postDelayed(() -> {
                loading.stopLoading();
                Intent intent = new Intent(ChuyenKhoanActivity.this, QuanLyBanActivity.class);
                maBan = "";
                startActivity(intent);
            }, 2000);
        } else {
            Log.e("messageAPI", "not receive token");
        }
    }

    private void handleMoMoPaymentFailure(Intent data) {
        String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
        Log.e("messageAPI", "message: " + message);
    }

    private void handleMoMoPaymentNotSupported() {
        Log.e("messageAPI", "message: " + "This function is not supported in this version of MoMo app. Please update to latest version.");
    }
}
