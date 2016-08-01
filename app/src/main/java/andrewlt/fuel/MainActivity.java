package andrewlt.fuel;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import andrewlt.fuel.DaoDataBase.MyFuelDetail;
import andrewlt.fuel.Fragment.FuelCurve;
import andrewlt.fuel.Fragment.FuelList;
import andrewlt.fuel.MyView.CurveView;
import andrewlt.fuel.MyView.WidgetTopBar;
import me.fuel.greendao.FuelData;
import me.fuel.greendao.FuelDataDao;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    private CurveView curveView;
    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mFragmentAdapter;
    private FuelCurve fuelCurve;
    private FuelList fuelList;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private Intent mIntentAddRecord;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();//高版本可以换成 ActionBar actionBar = getActionBar();
        actionBar.hide();
        initDao();
        initFragments();
        initRadioButton();
        initTopbar();
        this.mIntentAddRecord = new Intent(this, AddRecordActivity.class);
    }
    private void initDao(){
        MyFuelDetail myFuelDetail = new MyFuelDetail().getInstance();
        myFuelDetail.initGreenDao(this);
        FuelDataDao mFuelDataDao = myFuelDetail.getFuelDetailDao();
        FuelData fuelData = new FuelData();
        float j = (float) 2.9;
        fuelData.setAverageFuel(j);
        fuelData.setYear(2016);
        fuelData.setMonth(12);
        fuelData.setDay(1);
        myFuelDetail.calAverageFuel();
//        myFuelDetail.getFuelDetailDao().deleteAll();
    }

    private void initFragments(){
        mViewPager = (ViewPager)findViewById(R.id.main_viewPager);
        fuelCurve = new FuelCurve();
        mFragments.add(fuelCurve);
        mTitles.add("近一年油耗");
        fuelList = new FuelList();
        fuelList.setHandler(mainHandler);
        mFragments.add(fuelList);
        mTitles.add("油耗记录");
        mFragmentAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            private int[] imageResId = {
                    R.drawable.tab_bar_home_normal,
                    R.drawable.add_data
            };
            @Override
            public Fragment getItem(int position) {
                Log.e("====","current="+position);
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
//                Drawable image = getResources().getDrawable(imageResId[position]);
//                image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//                SpannableString sb = new SpannableString(" "+mTitles.get(position));
//                ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//                sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                return sb;
            }
        };
        mViewPager.setAdapter(mFragmentAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tableLayout);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for(int i=0;i<mTitles.size();i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void initRadioButton(){
        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.radio_button0);
        radioButton.setChecked(true);
        ((RadioButton) findViewById(R.id.radio_button0)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button1)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button2)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button3)).setOnCheckedChangeListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
// TODO Auto-generated method stub
        super.onNewIntent(intent);

        fuelCurve.setCurveChartData();
        fuelCurve.setCurveDetail();
        fuelList.setDataChanged();
        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.radio_button0);
        radioButton.setChecked(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_button0:
//                    Log.e("======",buttonView.getId() + "is checked 1 ");
                    break;
                case R.id.radio_button1:
//                    Log.e("======",buttonView.getId() + "is checked 2 ");
                    FuelData fuelData = new FuelData();
                    fuelData.setId((long) -1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) fuelData);
                    mIntentAddRecord.putExtras(bundle);
                    startActivity(mIntentAddRecord);
                    break;
                case R.id.radio_button2:
//                    Log.e("======",buttonView.getId() + "is checked 3 ");
//                    startActivity(mFavoriteIntent);
                    break;
                case R.id.radio_button3:
//                    Log.e("======",buttonView.getId() + "is checked 4 ");
                    break;
            }
        }
    }

    private void initTopbar(){
        WidgetTopBar wtbOne = (WidgetTopBar) findViewById(R.id.wtb_curve);
        wtbOne.getLeftBtnImage().setOnClickListener(this);
        wtbOne.getRightBtnImage().setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_left_top_bar: {
                Toast.makeText(this, "第二个标题 左边按钮", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.ib_right_top_bar: {
                Toast.makeText(this, "第一个标题 右边按钮", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_left_top_bar: {
                Toast.makeText(this, "第一个标题 左边按钮", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.btn_right_top_bar: {
                Toast.makeText(this, "第二个标题 右边按钮", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
    public Handler mainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                fuelCurve.setCurveChartData();
                fuelCurve.setCurveDetail();
            }
            return true;
        }
    });
}
