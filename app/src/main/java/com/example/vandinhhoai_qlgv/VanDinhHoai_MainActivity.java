package com.example.vandinhhoai_qlgv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ListView;

import com.example.vandinhhoai_qlgv.Adapters.AdapterGiaoVien;
import com.example.vandinhhoai_qlgv.Classes.GiaoVien;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;
import java.util.ArrayList;

public class VanDinhHoai_MainActivity extends AppCompatActivity {

    ListView lstGiaoVien;
    EditText edtSearch;
    AdapterGiaoVien adapter;
    ArrayList<GiaoVien> arrGiaoVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstGiaoVien = findViewById(R.id.listViewGiaoVien);
        edtSearch = findViewById(R.id.editText_search);

        arrGiaoVien = new ArrayList<>();
        adapter = new AdapterGiaoVien(VanDinhHoai_MainActivity.this, arrGiaoVien);
        lstGiaoVien.setAdapter(adapter);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJSON().execute("http://vandinhhoai-001-site1.ktempurl.com/giao-vien/json.php");
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = removeAccents(s.toString()).toLowerCase().trim();
                ArrayList<GiaoVien> filteredTeachers = new ArrayList<>();

                for (GiaoVien teacher : arrGiaoVien) {
                    String teacherName = removeAccents(teacher.getTenGiaoVien()).toLowerCase();
                    String teacherAddress = removeAccents(teacher.getDiaChi()).toLowerCase();

                    // Lọc dữ liệu theo tên hoặc địa chỉ
                    if (teacherName.contains(searchText) || teacherAddress.contains(searchText)) {
                        filteredTeachers.add(teacher);
                    }
                }

                // Tạo adapter mới với dữ liệu đã lọc và cập nhật ListView
                AdapterGiaoVien adapter = new AdapterGiaoVien(VanDinhHoai_MainActivity.this, filteredTeachers);
                lstGiaoVien.setAdapter(adapter);
            }
        });
    }
    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    private String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // Add username and password for basic authentication
            String username = "11168477";
            String password = "60-dayfreetrial";
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", basicAuth);

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    class docJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String idGV = String.valueOf(jsonObject.getInt("id"));
                    String tenGV = jsonObject.getString("Tengv");
                    int gioiTinh = jsonObject.getInt("gioiTinh");
                    String diaChi = jsonObject.getString("diaChi");
                    String soDienThoai = jsonObject.getString("Sodt");
                    arrGiaoVien.add(new GiaoVien(idGV, tenGV, gioiTinh, diaChi, soDienThoai));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
