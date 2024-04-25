package coffee.app.DucAn.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import coffee.app.DucAn.R;
import coffee.app.DucAn.interfaces.ItemOderOnClick;
import coffee.app.DucAn.model.HangHoa;

public class ThucUongOderThemAdapter extends RecyclerView.Adapter<ThucUongOderThemAdapter.ThucUongViewHolder> {
    ArrayList<HangHoa> list;
    ItemOderOnClick itemOderOnClick;

    public ThucUongOderThemAdapter(ArrayList<HangHoa> list , ItemOderOnClick itemOderOnClick){
        this.list = list;
        this.itemOderOnClick = itemOderOnClick;
    }

    @NonNull
    @Override
    public ThucUongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_thuc_uong_oder, parent, false);
        return new ThucUongViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ThucUongViewHolder holder, int position) {
        HangHoa hangHoa = list.get(position);
        if (hangHoa == null) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(hangHoa.getHinhAnh(),
                0,
                hangHoa.getHinhAnh().length);
        holder.ivHinhAnh.setImageBitmap(bitmap);
        holder.tvTenHangHoa.setText(hangHoa.getTenHangHoa());
        holder.tvGiaTien.setText(hangHoa.getGiaTien() + "VND");
        holder.tvMaHangHoa.setText("Mã sản phẩm: "+String.valueOf(hangHoa.getMaHangHoa()));
        holder.ivThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOderOnClick.itemOclick(view, hangHoa);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ThucUongViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHinhAnh, ivThem;
        TextView tvTenHangHoa, tvGiaTien, tvMaHangHoa;

        public ThucUongViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHinhAnh = itemView.findViewById(R.id.ivHinhAnh);
            ivThem = itemView.findViewById(R.id.ivThem);
            tvTenHangHoa = itemView.findViewById(R.id.tvTenHangHoa);
            tvGiaTien = itemView.findViewById(R.id.tvGiaTien);
            tvMaHangHoa = itemView.findViewById(R.id.tvMaSanPham);
        }
    }
}
