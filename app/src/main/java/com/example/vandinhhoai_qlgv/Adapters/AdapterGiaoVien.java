package com.example.vandinhhoai_qlgv.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vandinhhoai_qlgv.Classes.GiaoVien;
import com.example.vandinhhoai_qlgv.R;

import java.util.ArrayList;
import java.util.Locale;

public class AdapterGiaoVien extends BaseAdapter {
    private Context context;
    private ArrayList<GiaoVien> listGiaoVien;

    public AdapterGiaoVien(Context context, ArrayList<GiaoVien> listGiaoVien) {
        this.context = context;
        this.listGiaoVien = listGiaoVien;
    }

    @Override
    public int getCount() {
        return listGiaoVien.size(); // Sử dụng số lượng mục trong danh sách đã lọc
    }

    @Override
    public Object getItem(int position) {
        return listGiaoVien.get(position); // Lấy mục từ danh sách đã lọc
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.giao_vien_item, parent, false);
        }
        GiaoVien giaoVien = listGiaoVien.get(position); // Lấy mục từ danh sách đã lọc

        TextView txtTenGv = convertView.findViewById(R.id.textViewTen);
        TextView txtGioiTinh = convertView.findViewById(R.id.textViewGioiTinh);
        TextView txtDiaChi = convertView.findViewById(R.id.textViewDiaChi);
        TextView txtSoDienThoai= convertView.findViewById(R.id.textViewSoDienThoai);

        txtTenGv.setText(giaoVien.getTenGiaoVien());
        if (giaoVien.getGioiTinh() == 0){
            txtGioiTinh.setText("Nu");
        }else {
            txtGioiTinh.setText("Nam");
        }

        txtDiaChi.setText(giaoVien.getDiaChi());
        txtSoDienThoai.setText(giaoVien.getSoDienThoai());

        return convertView;
    }
}

