package com.example.hoangbao.apptracnghiem.uis.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hoangbao.apptracnghiem.R;
import com.example.hoangbao.apptracnghiem.adapter.DatumAdapter;
import com.example.hoangbao.apptracnghiem.adapter.LoaiMenuAdapter;
import com.example.hoangbao.apptracnghiem.model.Datum;
import com.example.hoangbao.apptracnghiem.model.ExamListRespone;
import com.example.hoangbao.apptracnghiem.model.LoaiMenu;
import com.example.hoangbao.apptracnghiem.rest.RestClient;
import com.example.hoangbao.apptracnghiem.uis.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachMonThiFragment extends Fragment {
    public static String mamon;
    String url="http://14.160.93.98:8672/danhsachmonthi.php";
    FragmentManager fragmentManager;
    View view;
    ArrayList<ExamListRespone>examListResponeArrayList;
    ArrayList<Datum> datumArrayList;
    ExamListRespone examListRespone;
    ArrayList<LoaiMenu> loaiMenuArrayList;
    DrawerLayout drawerLayoutdanhsachmonthi;
    ImageButton imgbtnDanhSachMonThi;
    ListView lvMenuDanhSachMonThi;
    ListView lvdanhsachmonthi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_danh_sach_mon_thi, container, false);
        anhXa();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        loaiMenuArrayList.add(new LoaiMenu(R.drawable.next, "Đăng Xuất"));
        final LoaiMenuAdapter loaiMenuAdapter = new LoaiMenuAdapter(getActivity(), R.layout.dong_menu, loaiMenuArrayList);
        lvMenuDanhSachMonThi.setAdapter(loaiMenuAdapter);
        retrofit2.Call<ExamListRespone> callExamList=RestClient.getAPIs().getExamList();
        callExamList.enqueue(new Callback<ExamListRespone>() {
            @Override
            public void onResponse(Call<ExamListRespone> call, Response<ExamListRespone> response) {
                Log.d("kiemtra",response.toString());
                ExamListRespone examListRespone=response.body();
                examListResponeArrayList.add(examListRespone);
                mamon=examListRespone.getData().get(0).getMamon();
                datumArrayList= (ArrayList<Datum>) examListResponeArrayList.get(0).getData();
                DatumAdapter datumAdapter=new DatumAdapter(getActivity(),R.layout.dong_datum,datumArrayList);
                lvdanhsachmonthi.setAdapter(datumAdapter);
            }

            @Override
            public void onFailure(Call<ExamListRespone> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi danhsachmonthifragment", Toast.LENGTH_SHORT).show();
            }
        });
        batSuKien();
        super.onActivityCreated(savedInstanceState);
    }

    private void batSuKien() {
        imgbtnDanhSachMonThi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutdanhsachmonthi.openDrawer(GravityCompat.START);
            }
        });
        lvdanhsachmonthi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LamBaiThiFragment lamBaiThiFragment = new LamBaiThiFragment();
                fragmentTransaction.replace(R.id.framelayout, lamBaiThiFragment);
                fragmentTransaction.commit();
            }
        });
        lvMenuDanhSachMonThi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn thoát chương trình không?");
                builder.setCancelable(true);
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    private void anhXa() {
        datumArrayList=new ArrayList<>();
        fragmentManager = getActivity().getSupportFragmentManager();
        examListResponeArrayList = new ArrayList<>();
        loaiMenuArrayList = new ArrayList<>();
        drawerLayoutdanhsachmonthi = view.findViewById(R.id.drawerlayout_danhsachmonthi);
        lvMenuDanhSachMonThi = view.findViewById(R.id.lv_menudanhsachmonthi);
        imgbtnDanhSachMonThi = view.findViewById(R.id.imgbtn_danhsachmonthi);
        lvdanhsachmonthi = view.findViewById(R.id.lv_danhsachmonthi);
    }
}
