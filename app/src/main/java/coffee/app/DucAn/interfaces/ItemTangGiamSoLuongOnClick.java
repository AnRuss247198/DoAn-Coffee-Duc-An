package coffee.app.DucAn.interfaces;

import android.view.View;

import coffee.app.DucAn.model.HangHoa;
import coffee.app.DucAn.model.HoaDonChiTiet;

public interface ItemTangGiamSoLuongOnClick {
    void itemOclick(View view, int indext, HoaDonChiTiet hoaDonChiTiet, HangHoa hangHoa);
    void itemOclickDeleteHDCT(View view, HoaDonChiTiet hoaDonChiTiet);
}
