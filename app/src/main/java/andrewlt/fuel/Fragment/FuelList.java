package andrewlt.fuel.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import andrewlt.fuel.AddRecordActivity;
import andrewlt.fuel.DaoDataBase.MyFuelDetail;
import andrewlt.fuel.R;
import me.fuel.greendao.FuelData;
import me.fuel.greendao.FuelDataDao;

/**
 * Created by liut1 on 7/31/16.
 */
public class FuelList extends Fragment {
    private Handler handler;
    MyFuelDetail mMyFuelDetail;
    FuelDataDao mFuelDataDao;
    private View view;
    private FuelListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<FuelData> mData = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fuel_list, container, false);
        mMyFuelDetail = new MyFuelDetail().getInstance();
        mFuelDataDao = mMyFuelDetail.getFuelDetailDao();
        mData = mFuelDataDao.queryBuilder().list();
        initCyclerView();
        return view;
    }

    private void initCyclerView(){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.FuelList);
        mAdapter = new FuelListAdapter(mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FuelListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, FuelData data) {
                Intent intent = new Intent(getContext(), AddRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) data);
                intent.putExtras(bundle);
//                startActivity(intent);
                Toast.makeText(getContext(), "onItemClick "+data.getCurrentFuelCapacity(), Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnItemLongClickListener(new FuelListAdapter.MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final FuelData data) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_select);
                Button bt = (Button)dialog.findViewById(R.id.dialog_ok);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMyFuelDetail.delFuelDetail(data);
                        setDataChanged();
                        dialog.dismiss();
                    }
                });
                bt = (Button)dialog.findViewById(R.id.dialog_cancel);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Toast.makeText(getContext(), "onItemLongClick "+data.getTime(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void setHandler(Handler handler){
        this.handler = handler;
    }
    public void setDataChanged(){
        mData.clear();
        mData.addAll(mFuelDataDao.queryBuilder().list());
        Log.e("aaa","size="+mData.size());
        mAdapter.notifyDataSetChanged();
    }
}
