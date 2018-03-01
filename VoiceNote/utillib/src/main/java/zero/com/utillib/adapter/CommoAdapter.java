package zero.com.utillib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommoAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mData;
	protected int mItemLayoutId;
	public CommoAdapter(Context context, List<T> data, int itemLayoutId) {
		this.mContext = context;
		this.mData = data;
		this.mItemLayoutId = itemLayoutId;
	}
	
	public void upDate(List<T> data){
		this.mData = data;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, converView, parent,mItemLayoutId, position);
		
		/*Deposit pk = mData.get(position);
		R.layout.outbound_pickprint_detail_deposit_itme
		((TextView)holder.getVIew(R.id.deposit_txt)).setText(pk.deposit);
		((TextView)holder.getVIew(R.id.number_txt)).setText(pk.number);*/
		convert(holder,getItem(position),position);
		return holder.getmConverView();
	}

	public abstract void convert(ViewHolder holder,T t,int position);
}
