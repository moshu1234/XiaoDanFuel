package andrewlt.fuel;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import andrewlt.fuel.DaoDataBase.MyFuelDetail;
import andrewlt.fuel.MyView.WidgetTopBar;
import andrewlt.fuel.Utils.MyDateTime;
import me.fuel.greendao.FuelData;
import me.fuel.greendao.FuelDataDao;

public class AddRecordActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Intent mIntentMain;
    private FuelDataDao mFuelDataDao;
    private FuelData mFuelData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mFuelData = (FuelData) bundle.getSerializable("data");
        ActionBar actionBar = getSupportActionBar();//高版本可以换成 ActionBar actionBar = getActionBar();
        actionBar.hide();
        initTopbar();
        initRadioButton();
        initRecord();
        mIntentMain = new Intent(this,MainActivity.class);
        mIntentMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mFuelDataDao = new MyFuelDetail().getInstance().getFuelDetailDao();
    }
    public void initRecord(){
        EditText editText;
        Spinner spinner = (Spinner) findViewById(R.id.record_fuelType);
        List<String> type_list = new ArrayList<>();
        type_list.add("柴油");
        type_list.add("#90");
        type_list.add("#92");
        type_list.add("#93");
        type_list.add("#95");
        type_list.add("#97");
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_list);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        if(mFuelData.getId() == -1) {
            MyDateTime myDateTime = new MyDateTime();
            editText = (EditText) findViewById(R.id.record_date);
            editText.setText(myDateTime.getLastDate(0));
            editText = (EditText) findViewById(R.id.record_time);
            editText.setText(myDateTime.getCurrentTime());

            spinner.setSelection(2);
        }else {
            editText = (EditText) findViewById(R.id.record_date);
            editText.setText(mFuelData.getYear()+"/"+mFuelData.getMonth()+"/"+mFuelData.getDay());
            editText = (EditText) findViewById(R.id.record_time);
            editText.setText(mFuelData.getTime());
            editText = (EditText) findViewById(R.id.record_miles);
            editText.setText(mFuelData.getCurrentMiles().toString());
            editText = (EditText) findViewById(R.id.record_unit);
            editText.setText(mFuelData.getUnitPrice().toString());
            editText = (EditText) findViewById(R.id.record_money);
            editText.setText(mFuelData.getTotalPrice().toString());
            editText = (EditText) findViewById(R.id.record_fuelCount);
            editText.setText(mFuelData.getCurrentFuelCapacity().toString());
            CheckBox checkBox = (CheckBox) findViewById(R.id.record_full);
            checkBox.setChecked(mFuelData.getIsFull());
            checkBox = (CheckBox) findViewById(R.id.record_light);
            checkBox.setChecked(mFuelData.getIsEmpty());
            checkBox = (CheckBox) findViewById(R.id.record_noRecord);
            checkBox.setChecked(mFuelData.getLastTimeNoRecord());
            Log.e("==","qulity="+mFuelData.getFuelQuality());
            switch (mFuelData.getFuelQuality()){
                case "柴油":
                    spinner.setSelection(0);
                    break;
                case "#90":
                    spinner.setSelection(1);
                    break;
                case "#92":
                    spinner.setSelection(2);
                    break;
                case "#93":
                    spinner.setSelection(3);
                    break;
                case "#95":
                    spinner.setSelection(4);
                    break;
                case "#97":
                    spinner.setSelection(5);
                    break;
            }
        }
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            view.setText("你的血型是："+m[arg2]);
            //TODO here need to save gender and age
//            myToast("spinner clicked");
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    public void initRadioButton(){
        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.radio_button1);
        radioButton.setChecked(true);
        ((RadioButton) findViewById(R.id.radio_button0)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button1)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button2)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button3)).setOnCheckedChangeListener(this);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.radio_button0:
//                    Log.e("======",buttonView.getId() + "is checked 1 ");
                    startActivity(mIntentMain);
                    break;
                case R.id.radio_button1:
//                    Log.e("======",buttonView.getId() + "is checked 2 ");

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
                saveRecord();
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
    public void saveRecord(){

        MyFuelDetail myFuelDetail = new MyFuelDetail().getInstance();

        FuelData fuelData = mFuelData;
        EditText editText = (EditText)findViewById(R.id.record_date);
        if(!editText.getText().toString().equals("")) {
            String[] s =  editText.getText().toString().split("/");
            Log.e("====","year:"+s[0]);
            Log.e("====","m:"+s[1]);
            Log.e("====","d:"+s[2]);
            fuelData.setYear(Integer.valueOf(s[0]));
            fuelData.setMonth(Integer.valueOf(s[1]));
            fuelData.setDay(Integer.valueOf(s[2]));
        }

        editText = (EditText)findViewById(R.id.record_time);
        fuelData.setTime(editText.getText().toString());
        Log.e("======","time="+editText.getText().toString());

        editText = (EditText)findViewById(R.id.record_miles);
        if(!editText.getText().toString().equals("")) {
            fuelData.setCurrentMiles(Integer.valueOf(editText.getText().toString()));
        }

        String unit, money, count;
        editText = (EditText)findViewById(R.id.record_unit);
        unit = editText.getText().toString();
        editText = (EditText)findViewById(R.id.record_money);
        money = editText.getText().toString();
        editText = (EditText)findViewById(R.id.record_fuelCount);
        count = editText.getText().toString();
        if(unit=="" || money==""){
            Toast.makeText(this, "请输入单价和总价", Toast.LENGTH_SHORT).show();
            return;
        }
        fuelData.setUnitPrice(Float.valueOf(unit));
        fuelData.setTotalPrice(Float.valueOf(money));
        if(count.equals("")){
            fuelData.setCurrentFuelCapacity(Float.valueOf(money)/Float.valueOf(unit));
        }
        else {
            fuelData.setCurrentFuelCapacity(Float.valueOf(count));
        }

        CheckBox checkBox;
        checkBox = (CheckBox)findViewById(R.id.record_full);
        fuelData.setIsFull(checkBox.isChecked());
        checkBox = (CheckBox)findViewById(R.id.record_light);
        fuelData.setIsEmpty(checkBox.isChecked());
        checkBox = (CheckBox)findViewById(R.id.record_noRecord);
        fuelData.setLastTimeNoRecord(checkBox.isChecked());

        Spinner spinner = (Spinner)findViewById(R.id.record_fuelType);
        String id = spinner.getSelectedItem().toString();
        Log.e("===","spinner="+id);
        fuelData.setFuelQuality(id);
        fuelData.setAverageFuel(0.00f);

        if(fuelData.getId() == -1) {
            myFuelDetail.addFuelDetail(fuelData);
        }else {
            myFuelDetail.updateFuelDetail(fuelData);
        }

        startActivity(mIntentMain);
    }
}
