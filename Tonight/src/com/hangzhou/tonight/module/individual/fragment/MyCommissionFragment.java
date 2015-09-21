package com.hangzhou.tonight.module.individual.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hangzhou.tonight.R;
import com.hangzhou.tonight.module.base.fragment.BFragment;
import com.hangzhou.tonight.module.individual.fragment.MyOrderFragment.OrderModel;
import com.hangzhou.tonight.module.individual.fragment.MyOrderFragment.ModelAdapter.ViewHolder;

/**
 * 我的佣金
 * @author hank
 *
 */
public class MyCommissionFragment extends BFragment {

	ListView listView;
	ModelAdapter dapater = null;
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_individual_mycommission, container, false);
	}
	
	@Override protected void doView() {
		listView = (ListView) findViewById(R.id.mycommission_listview);
	}

	@Override protected void doListeners() {

	}

	@Override protected void doHandler() {
		dapater = new ModelAdapter();
		listView.setAdapter(dapater);
		doPostData();
	}

	public void doPostData(){
		String[] strs = new String[]{"张三","汉庭酒店XXX分店","1000","5"};
		for(int i=0,len = 6;i<len;i++){
			EntityModel model = new EntityModel();
			model.name  = strs[0];
			model.place = strs[1];
			model.pay   = strs[2];
			model.money = strs[3];
			mModeList.add(model);
		}
	}
	
	List<EntityModel> mModeList = new ArrayList<EntityModel>();
	class ModelAdapter extends BaseAdapter {

		@Override public int getCount() {
			return mModeList.size();
		}

		@Override public EntityModel getItem(int position) {
			return mModeList.get(position);
		}

		@Override public long getItemId(int position) {
			return position;
		}

		@Override public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity().getApplicationContext(),R.layout.item_fragment_individual_mycommission, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			EntityModel item = mModeList.get(position);
			holder.tv_name.setText(item.name);
			holder.tv_place.setText(item.place);
			holder.tv_pay.setText(String.format(getResources().getString(R.string.mycommission_pay), item.pay));
			holder.tv_money.setText(item.money);
			return convertView;
		}

		class ViewHolder {
			TextView tv_name,tv_place,tv_pay,tv_money;
			public ViewHolder(View view) {
				tv_name   = (TextView) view.findViewById(R.id.mycommission_name);
				tv_place  = (TextView) view.findViewById(R.id.mycommission_place);
				tv_pay    = (TextView) view.findViewById(R.id.mycommission_pay);
				tv_money  = (TextView) view.findViewById(R.id.mycommission_money);
				view.setTag(this);
			}
		}
	}
	
	public static class EntityModel{
		String name,place,pay,money;

		public String getName() {
			return name;
		}

		public String getPlace() {
			return place;
		}

		public String getPay() {
			return pay;
		}

		public String getMoney() {
			return money;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPlace(String place) {
			this.place = place;
		}

		public void setPay(String pay) {
			this.pay = pay;
		}

		public void setMoney(String money) {
			this.money = money;
		}
		
	}
}
