package coffee.app.DucAn.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import coffee.app.DucAn.MainActivity;
import coffee.app.DucAn.R;
import coffee.app.DucAn.adapter.PhotoAdapter;
import coffee.app.DucAn.adapter.ThucUongHomeFragmentAdapter;
import coffee.app.DucAn.dao.HangHoaDAO;
import coffee.app.DucAn.dao.HoaDonMangVeDao;
import coffee.app.DucAn.dao.NguoiDungDAO;
import coffee.app.DucAn.model.HangHoa;
import coffee.app.DucAn.model.HoaDonMangVe;
import coffee.app.DucAn.model.NguoiDung;
import coffee.app.DucAn.model.Photo;
import coffee.app.DucAn.ui.DoanhThuActivity;
import coffee.app.DucAn.ui.DuyetActivity;
import coffee.app.DucAn.ui.HoaDonActivity;
import coffee.app.DucAn.ui.HoaDonNguoiDungActivity;
import coffee.app.DucAn.ui.LoaiThucUongActivity;
import coffee.app.DucAn.ui.MangVeActivity;
import coffee.app.DucAn.ui.NhanVienActivity;
import coffee.app.DucAn.ui.QuanLyBanActivity;
import coffee.app.DucAn.ui.ThucUongActivity;
import coffee.app.DucAn.utils.MyToast;
import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment implements View.OnClickListener {
    TextView tvHi;
    CircleImageView civHinhAnh;
    ViewPager2 vpSlideImage;
    CircleIndicator3 indicator3;
    CardView cvBan, cvBan2, cvLoai, cvThucUong, cvNhanVien, cvHoaDon, cvDoanhThu, cvDuyet, cvMangVe, cvHoaDon2;
    MainActivity mainActivity;
    NguoiDungDAO nguoiDungDAO;
    HangHoaDAO hangHoaDAO;
    RecyclerView recyclerViewThucUong;
    Handler handler;
    Runnable runnable;
    LinearLayout linearLayoutDm, linearLayoutDm2, linearLayoutDm3, linearLayoutDm4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initOnClickCard();
        loadSlideImage();

        mainActivity = ((MainActivity) getActivity());
        nguoiDungDAO = new NguoiDungDAO(getContext());
        hangHoaDAO = new HangHoaDAO(getContext());

        welcomeUser();
        loadListThucUong();
        autoRunSildeImage();
        return view;
    }

    private void autoRunSildeImage() {
        // Tự động chuyển ảnh trong SlideImage
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (vpSlideImage.getCurrentItem() == getListImage().size() - 1) {
                    vpSlideImage.setCurrentItem(0, false);
                } else {
                    vpSlideImage.setCurrentItem(vpSlideImage.getCurrentItem() + 1);
                }
            }
        };
        handler.postDelayed(runnable, 2000);

        // Sự kiện Slide Image chuyển ảnh
        vpSlideImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 2000);
            }
        });
    }

    private void loadListThucUong() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerViewThucUong.setLayoutManager(linearLayoutManager);

        // Lấy danh sách thức uống hiển thị trên recyclerView
        ArrayList<HangHoa> listHangHoa = hangHoaDAO.getAll();
        ThucUongHomeFragmentAdapter adapter = new ThucUongHomeFragmentAdapter(listHangHoa);
        recyclerViewThucUong.setAdapter(adapter);
    }

    private void initView(View view) {
        vpSlideImage = view.findViewById(R.id.vpSlideImage);
        indicator3 = view.findViewById(R.id.circleIndicator3);
        cvBan = view.findViewById(R.id.cardBan);
        cvLoai = view.findViewById(R.id.cardLoaiThucUong);
        cvThucUong = view.findViewById(R.id.cardThucUong);
        cvNhanVien = view.findViewById(R.id.cardNhanVien);
        cvHoaDon = view.findViewById(R.id.cardHoaDon);
        cvDoanhThu = view.findViewById(R.id.cardDoanhThu);
        cvDuyet = view.findViewById(R.id.cardDuyet);
        cvMangVe = view.findViewById(R.id.cardMangVe);
        cvBan2 = view.findViewById(R.id.cardBan2);
        cvHoaDon2 = view.findViewById(R.id.cardHoaDon2);
        tvHi = view.findViewById(R.id.tvHi);
        civHinhAnh = view.findViewById(R.id.hinhAnh);
        linearLayoutDm = view.findViewById(R.id.linearLayoutDm1);
        linearLayoutDm2 = view.findViewById(R.id.linearLayoutDm2);
        linearLayoutDm3 = view.findViewById(R.id.linearLayoutDm3);
        linearLayoutDm4 = view.findViewById(R.id.linearLayoutDm4);
        recyclerViewThucUong = view.findViewById(R.id.recyclerViewThucUong);
    }

    private void initOnClickCard() {
        cvBan.setOnClickListener(this);
        cvLoai.setOnClickListener(this);
        cvThucUong.setOnClickListener(this);
        cvNhanVien.setOnClickListener(this);
        cvHoaDon.setOnClickListener(this);
        cvDoanhThu.setOnClickListener(this);
        cvDuyet.setOnClickListener(this);
        cvMangVe.setOnClickListener(this);
        cvBan2.setOnClickListener(this);
        cvHoaDon2.setOnClickListener(this);
    }

    private void loadSlideImage() {
        // Hiển thị Slide image
        PhotoAdapter adapter = new PhotoAdapter(getListImage());

        vpSlideImage.setAdapter(adapter);
        vpSlideImage.setOffscreenPageLimit(2);
        indicator3.setViewPager(vpSlideImage);
    }

    @NonNull
    private ArrayList<Photo> getListImage() {
        ArrayList<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.slide_image1));
        list.add(new Photo(R.drawable.slide_image2));
        list.add(new Photo(R.drawable.slide_image3));
        list.add(new Photo(R.drawable.silde_image4));
        list.add(new Photo(R.drawable.slide_image5));

        return list;
    }

    @SuppressLint("SetTextI18n")
    private void welcomeUser() {
        NguoiDung nguoiDung = getNguoiDung();
        Bitmap bitmap = BitmapFactory.decodeByteArray(nguoiDung.getHinhAnh(),
                0,
                nguoiDung.getHinhAnh().length);

        // Gán dữ liệu cho view
        tvHi.setText("Hello, " + nguoiDung.getHoVaTen());
        civHinhAnh.setImageBitmap(bitmap);
        if (nguoiDung.getChucVu().equals("Admin") || nguoiDung.getChucVu().equals("NhanVien")) {
            linearLayoutDm.setVisibility(View.VISIBLE);
            linearLayoutDm2.setVisibility(View.VISIBLE);
            linearLayoutDm3.setVisibility(View.GONE);
            linearLayoutDm4.setVisibility(View.GONE);
        } else {
            linearLayoutDm.setVisibility(View.GONE);
            linearLayoutDm2.setVisibility(View.GONE);
            linearLayoutDm3.setVisibility(View.VISIBLE);
            linearLayoutDm4.setVisibility(View.VISIBLE);
        }
    }

    private NguoiDung getNguoiDung() {
        // Lấy mã người dùng từ MainActivity thông qua hàm getKeyUser
        String maNguoiDung = Objects.requireNonNull(mainActivity).getKeyUser();
        // Lây đối tượng người dùng theo mã
        return nguoiDungDAO.getByMaNguoiDung(maNguoiDung);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardBan:
            case R.id.cardBan2:
                // Mở màng hình quản lý bàn
                startActivity(new Intent(getContext(), QuanLyBanActivity.class));
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardLoaiThucUong:
                // Mở màng hình quản lý loại hàng
                startActivity(new Intent(getContext(), LoaiThucUongActivity.class));
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardThucUong:
                // Mở màng hình quản lý thức uống
                startActivity(new Intent(getContext(), ThucUongActivity.class));
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardNhanVien:
                if (getNguoiDung().isAdmin()) {
                    // Người dùng có chức vụ ="Admin" -> Mở màng hình quản lý nhân viên
                    startActivity(new Intent(getContext(), NhanVienActivity.class));
                    (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                } else {
                    // Người dung có chức vụ = "NhanVien"
                    MyToast.error(getContext(), "Chức năng dành cho Admin");
                }
                break;
            case R.id.cardHoaDon:
                // Mở màng hình quản lý hoá đơn
                startActivity(new Intent(getContext(), HoaDonActivity.class));
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardHoaDon2:
                // Mở màng hình quản lý hoá đơn
                Intent intent1 = new Intent(getContext(), HoaDonNguoiDungActivity.class);
                intent1.putExtra("maNguoiDung", getNguoiDung().getMaNguoiDung());
                startActivity(intent1);
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardDuyet:
                // Mở màng hình quản lý hoá đơn
                startActivity(new Intent(getContext(), DuyetActivity.class));
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardMangVe:
                Intent intent = new Intent(getContext(), MangVeActivity.class);
                intent.putExtra("maNguoiDung", getNguoiDung().getMaNguoiDung());
                Calendar c = Calendar.getInstance(); // lấy ngày thánh năm và giờ hiện tại
                HoaDonMangVeDao hoaDonMangVeDao = new HoaDonMangVeDao(getContext());
                if (!hoaDonMangVeDao.checkHoaDonChuaDuyet(getNguoiDung().getMaNguoiDung()) || !hoaDonMangVeDao.checkHoaDonChuaXacNhan(getNguoiDung().getMaNguoiDung())) {
                    HoaDonMangVe hoaDonMangVe = new HoaDonMangVe();
                    hoaDonMangVe.setMaKhachHang(getNguoiDung().getMaNguoiDung());
                    hoaDonMangVe.setGioVao(c.getTime());
                    hoaDonMangVe.setGioRa(c.getTime());
                    hoaDonMangVe.setTrangThai(HoaDonMangVe.CHUA_XAC_NHAN);
                    hoaDonMangVe.setGhiChu("");
                    hoaDonMangVeDao.insertHoaDonMangVe(hoaDonMangVe);
                }
                startActivity(intent);
                (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                break;
            case R.id.cardDoanhThu:
                if (getNguoiDung().isAdmin()) {
                    // Người dùng có chức vụ ="Admin" -> Mở màng hình quản lý doanh thu
                    startActivity(new Intent(getContext(), DoanhThuActivity.class));
                    (requireActivity()).overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
                } else {
                    // Người dùng có chức vụ = "NhanVien"
                    MyToast.error(getContext(), "Chức năng dành cho Admin");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        welcomeUser();
        loadListThucUong();
    }
}