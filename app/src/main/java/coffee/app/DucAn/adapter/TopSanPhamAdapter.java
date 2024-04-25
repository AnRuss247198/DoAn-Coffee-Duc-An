package coffee.app.DucAn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import coffee.app.DucAn.R;
import coffee.app.DucAn.dao.HangHoaDAO;
import coffee.app.DucAn.model.HoaDonChiTiet;

public class TopSanPhamAdapter extends RecyclerView.Adapter<TopSanPhamAdapter.ViewHolder>{

    List<HoaDonChiTiet> hoaDonChiTietList;
    Context context;
    HangHoaDAO hangHoaDAO;

    public TopSanPhamAdapter(Context context, List<HoaDonChiTiet> hoaDonChiTietList) {
        this.context = context;
        this.hoaDonChiTietList = hoaDonChiTietList;
        hangHoaDAO = new HangHoaDAO(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_thuc_uong_hoadonchitiet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietList.get(position);
        holder.tvTenHangHoa.setText(hangHoaDAO.getHangHoaById(hoaDonChiTiet.getMaHangHoa()).getTenHangHoa());
        holder.tvSoluong.setText("x"+hoaDonChiTiet.getSoLuong());
        holder.tvGiaTien.setText(hangHoaDAO.getHangHoaById(hoaDonChiTiet.getMaHangHoa()).getGiaTien()+" đ");
        Bitmap bitmap = BitmapFactory.decodeByteArray(hangHoaDAO.getHangHoaById(hoaDonChiTiet.getMaHangHoa()).getHinhAnh(),
                0,
                hangHoaDAO.getHangHoaById(hoaDonChiTiet.getMaHangHoa()).getHinhAnh().length);
        holder.ivHinhAnh.setImageBitmap(bitmap);
        holder.tvMaHangHoa.setText(String.valueOf(hoaDonChiTiet.getMaHangHoa()));
    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHinhAnh;
        TextView tvTenHangHoa, tvSoluong, tvGiaTien,tvMaHangHoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinhAnh = itemView.findViewById(R.id.ivHinhAnh);
            tvTenHangHoa = itemView.findViewById(R.id.tvTenHangHoa);
            tvSoluong = itemView.findViewById(R.id.tvSoluong);
            tvGiaTien = itemView.findViewById(R.id.tvGiaTien);
            tvMaHangHoa = itemView.findViewById(R.id.tvMasanpham);
        }
    }
}
