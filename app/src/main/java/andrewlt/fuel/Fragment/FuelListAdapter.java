package andrewlt.fuel.Fragment;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import andrewlt.fuel.R;
import andrewlt.fuel.Utils.MyFormat;
import me.fuel.greendao.FuelData;

/**
 * Created by liut1 on 7/31/16.
 */
public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.MyHeadViewHolder> {
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    public interface MyItemClickListener {
        public void onItemClick(View view, FuelData data);
    }
    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, FuelData data);
    }
    private List<FuelData> mData;
    public FuelListAdapter(List<FuelData>data){
        mData = data;
    }
    @Override
    public MyHeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHeadViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fuel_list_item_detail, parent, false);
        holder = new MyHeadViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHeadViewHolder holder, int position) {
        MyFormat myFormat = new MyFormat();
        holder.date.setText(mData.get(position).getYear()+"-"+mData.get(position).getMonth()+"-"+mData.get(position).getDay());
        holder.miles.setText(mData.get(position).getCurrentMiles()+"公里");
        holder.averageFuel.setText(myFormat.decimalKeep(mData.get(position).getAverageFuel(),2)+"升/百公里");

        if(position+1 < (mData.size())){
            Log.e("===","posio="+position+"   size="+mData.size());
            int milesDiff = mData.get(position+1).getCurrentMiles()-mData.get(position).getCurrentMiles();
            holder.mileAdded.setText(milesDiff+"公里");

            holder.priceMiles.setText(myFormat.decimalKeep(mData.get(position).getTotalPrice()/milesDiff,2)+"元/公里");
        }
        else {
            holder.mileAdded.setText("?");
            holder.priceMiles.setText("?");
        }
        if(position > 0){
            holder.fuelAverageDiff.setText("+"+myFormat.decimalKeep(mData.get(position).getAverageFuel()-mData.get(position-1).getAverageFuel(),2)+"升/百公里");
        }
        else {
            holder.fuelAverageDiff.setText("?");
        }
        holder.unitPrice.setText(myFormat.decimalKeep(mData.get(position).getUnitPrice(),2)+"元/升");
        holder.totalPrice.setText(myFormat.decimalKeep(mData.get(position).getTotalPrice(),2)+"元");
        holder.fuelCount.setText(myFormat.decimalKeep(mData.get(position).getCurrentFuelCapacity(),2)+"升");
        if(!TextUtils.isEmpty(mData.get(position).getNotes())){
            holder.note.setText("备注："+mData.get(position).getNotes());
        }
        if(position%2 == 0){
            holder.linearLayout.setBackgroundColor(0xFFFFFF);
        }
        holder.itemView.setTag(mData.get(position));
//        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }
    public class MyHeadViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener,View.OnLongClickListener{
        MyItemClickListener mlistener;
        MyItemLongClickListener mLonglistener;
        TextView date, miles,averageFuel,priceMiles,mileAdded,fuelAverageDiff,unitPrice,totalPrice,fuelCount,note;
        LinearLayout linearLayout,expand;
        public MyHeadViewHolder(View view) {
            super(view);
            mlistener = mItemClickListener;
            mLonglistener = mItemLongClickListener;

            date=(TextView)view.findViewById(R.id.fuel_list_date);
            miles=(TextView)view.findViewById(R.id.fuel_list_mils);
            averageFuel=(TextView)view.findViewById(R.id.fuel_list_fuelAverage);
            priceMiles=(TextView)view.findViewById(R.id.fuel_list_priceMiles);
            mileAdded=(TextView)view.findViewById(R.id.fuel_list_mileAdded);
            fuelAverageDiff=(TextView)view.findViewById(R.id.fuel_list_fuelAverageDiff);
            unitPrice=(TextView)view.findViewById(R.id.fuel_list_unitPrice);
            totalPrice=(TextView)view.findViewById(R.id.fuel_list_totalPrice);
            fuelCount=(TextView)view.findViewById(R.id.fuel_list_fuelCount);
            note=(TextView)view.findViewById(R.id.fuel_list_note);

            linearLayout=(LinearLayout)view.findViewById(R.id.fuel_list);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(this);
            expand = (LinearLayout)view.findViewById(R.id.expand);
            expand.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if(mlistener != null){
                bind();
                mlistener.onItemClick(v,mData.get(getAdapterPosition()));
            }
        }
        @Override
        public boolean onLongClick(View v){
            Log.e("===","wo caa   a a");
            if(mLonglistener != null){
                mLonglistener.onItemLongClick(v,mData.get(getAdapterPosition()));
            }
            return true;
        }

        public void bind(){
            Log.e("==","=="+expand.getVisibility());
            if(expand.getVisibility() == View.VISIBLE){
                expand.setVisibility(View.GONE);
            }
            else {
                expand.setVisibility(View.VISIBLE);
            }
        }
    }
}
