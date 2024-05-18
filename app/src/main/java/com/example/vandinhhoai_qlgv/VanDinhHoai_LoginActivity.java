package com.example.vandinhhoai_qlgv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vandinhhoai_qlgv.Adapters.AdapterGiaoVien;
import com.example.vandinhhoai_qlgv.Classes.GiaoVien;
import com.example.vandinhhoai_qlgv.Classes.TaiKhoan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class VanDinhHoai_LoginActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;
    ArrayList<TaiKhoan> arrTaiKhoan = new ArrayList<TaiKhoan>();
    TaiKhoan taiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_dinh_hoai_login);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new docJSON().execute("http://vandinhhoai-001-site1.ktempurl.com/accounts/json.php");
            }
        });

        Controller();
    }

    private void Controller() {
        edtUser = (EditText) findViewById(R.id.username);
        edtPass = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.loginButton);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                boolean isLogin = false;
                for (TaiKhoan taiKhoan: arrTaiKhoan) {
                    if (user.equals(taiKhoan.getUserName()) && pass.equals(taiKhoan.getPassWord())) {
                        isLogin = true;
                        break;
                    }
                }

                if (isLogin) {
                    Intent intent = new Intent(VanDinhHoai_LoginActivity.this, VanDinhHoai_MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(VanDinhHoai_LoginActivity.this, "Đăng nhạp thành công!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(VanDinhHoai_LoginActivity.this, "Tài khoản hoặc mật khẩu của bạn không đúng!", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                JSONArray mang = new JSONArray(s);
                for(int i=0;i<mang.length();i++){
                    JSONObject account = mang.getJSONObject(i);
                    String id = account.getString("id");
                    String user = account.getString("user");
                    String pass = account.getString("pass");

                    arrTaiKhoan.add(new TaiKhoan(id, user, pass));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(VanDinhHoai_LoginActivity.this,"Lỗi JSON" + s, Toast.LENGTH_LONG).show();
            }

        }
    }
}