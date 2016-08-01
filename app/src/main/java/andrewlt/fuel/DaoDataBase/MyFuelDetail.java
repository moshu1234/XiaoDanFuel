package andrewlt.fuel.DaoDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import me.fuel.greendao.DaoMaster;
import me.fuel.greendao.DaoSession;
import me.fuel.greendao.FuelData;
import me.fuel.greendao.FuelDataDao;

/**
 * Created by liut1 on 7/17/16.
 */
public class MyFuelDetail {
    private static MyFuelDetail instance = null;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private FuelDataDao dao = null;

    public MyFuelDetail getInstance(){
        if(instance == null){
            synchronized (MyFuelDetail.class){
                if(instance == null){
                    instance = new MyFuelDetail();
                }
            }
        }
        return instance;
    }

    public void initGreenDao(Context context){
        if(dao != null){
            return;
        }
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "t_fuelDetail", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        dao = daoSession.getFuelDataDao();
    }

    public FuelDataDao getFuelDetailDao() {
        return dao;
    }
    public void addFuelDetail(FuelData note){
        long id = dao.queryBuilder().list().size();
        note.setId(id);
        dao.insert(note);
    }
    public void updateFuelDetail(FuelData note){
        dao.update(note);
    }
    public void delFuelDetail(FuelData note){
        dao.delete(note);
    }
    public List<FuelData> searchFuelDetail(){
        return dao.queryBuilder().list();
    }
    public void searchFuelDetel(int id){

    }
    /*
    *1      2     3     4
    *
    */
    public void calAverageFuel(){
        FuelData data=null;
        Float count=0f;
        List<FuelData> mData = dao.queryBuilder().list();
        Log.e("=====","nima="+mData.size());
        for(int i=0;i<mData.size();i++){

            Log.e("=====","getUnitPrice="+mData.get(i).getUnitPrice());
            Log.e("=====","getTotalPrice="+mData.get(i).getTotalPrice());
            Log.e("=====","getCurrentFuelCapacity="+mData.get(i).getCurrentFuelCapacity());
            if(mData.get(i).getIsFull()){
                if(data != null && data.getIsFull()){
                    if(data.getAverageFuel() == 0) {
                        Float average = (mData.get(i).getCurrentFuelCapacity() + count)*100/(mData.get(i).getCurrentMiles() - data.getCurrentMiles());
                        data.setAverageFuel(average);
                        dao.update(data);
                    }
                }
                data = mData.get(i);
                count = 0f;

            }
            else if(mData.get(i).getIsEmpty()){
                if(data != null && data.getIsEmpty()){
                    if(data.getAverageFuel() == 0) {
                        Float average = (mData.get(i).getCurrentFuelCapacity() + count)*100/(mData.get(i).getCurrentMiles() - data.getCurrentMiles());
                        data.setAverageFuel(average);
                        dao.update(data);
                    }
                }
                data = mData.get(i);
                count = 0f;
            }
            else {
                count += mData.get(i).getCurrentFuelCapacity();
            }
        }
    }
}
