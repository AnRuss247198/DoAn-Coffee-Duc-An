package coffee.app.DucAn.interfaces;

import android.view.View;

import coffee.app.DucAn.model.HoaDonMangVe;

public interface ItemHoaDonMangVeOnClick {
    void itemOclick(View view, HoaDonMangVe hoaDonMangVe);

    void itemDuyet(View view, HoaDonMangVe hoaDonMangVe);

    void itemHuy(View view, HoaDonMangVe hoaDonMangVe);
}
