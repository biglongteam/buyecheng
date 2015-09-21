package com.hangzhou.tonight.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;

import com.hangzhou.tonight.LoginActivity;
import com.hangzhou.tonight.R;
import com.hangzhou.tonight.adapter.ActivesListAdapter;
import com.hangzhou.tonight.entity.ActivesEntity;
import com.hangzhou.tonight.entity.NearByPeople;
import com.hangzhou.tonight.maintabs.TabItemActivity;
import com.hangzhou.tonight.util.Base64Utils;
import com.hangzhou.tonight.util.HttpRequest;
import com.hangzhou.tonight.util.IntentJumpUtils;
import com.hangzhou.tonight.util.JsonResolveUtils;
import com.hangzhou.tonight.util.JsonUtils;
import com.hangzhou.tonight.util.PreferenceConstants;
import com.hangzhou.tonight.util.RC4Utils;
import com.hangzhou.tonight.util.ScreenUtils;
import com.hangzhou.tonight.view.HeaderLayout;
import com.hangzhou.tonight.view.HeaderLayout.HeaderStyle;
import com.hangzhou.tonight.view.HeaderLayout.SearchState;
import com.hangzhou.tonight.view.HeaderLayout.onMiddleImageButtonClickListener;
import com.hangzhou.tonight.view.HeaderLayout.onSearchListener;
import com.hangzhou.tonight.view.HeaderSpinner;
import com.hangzhou.tonight.view.HeaderSpinner.onSpinnerClickListener;
import com.hangzhou.tonight.view.XListView;
import com.hangzhou.tonight.view.XListView.IXListViewListener;

/**
 * @ClassName: 活动 主页
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yanchao
 * @date 2015-8-30 下午5:17:58
 * 
 */
public class PromotionActivity extends TabItemActivity implements
		OnClickListener, IXListViewListener {

	private Context mContext;
	private XListView xListView;
	// private PromotionListFragment mPeopleFragment;
	private Handler mHander;

	RelativeLayout index_head;
	LinearLayout sortlist;
	private ImageView title_search;
	private TextView tvCity,tvTitle;
	private int currentPage = 1,// 当期页码
			pageCount = 1,// 总页数
			pageSize = 15;// 每页数据量

	private Button btRq,btJl,btJg,btHp,btQx;
	private ActivesListAdapter mAdapter;
	public List<ActivesEntity> mActives = new ArrayList<ActivesEntity>();
	private PopupWindow mPopupWindow;
	private String cityId;
	private String cityName;
	private int sort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion);
		mContext = this;
		initViews();
		initEvents();
		init();
		getDataList(currentPage,cityId);
	}

	@Override
	protected void initViews() {
		index_head = (RelativeLayout) findViewById(R.id.index_head);
		title_search = (ImageView) findViewById(R.id.title_search);
		tvCity = (TextView) findViewById(R.id.tv_header_back);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		xListView = (XListView) findViewById(R.id.promotion_list);
		sortlist = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.sort_list, null);
		btRq = (Button) sortlist.findViewById(R.id.bt_rq);
		btJg = (Button) sortlist.findViewById(R.id.bt_jg);
		btHp = (Button) sortlist.findViewById(R.id.bt_hp);
		btJl = (Button) sortlist.findViewById(R.id.bt_jl);
		btQx = (Button) sortlist.findViewById(R.id.bt_qx);
	
	}

	@Override
	protected void initEvents() {

		//xListView.addHeaderView(mPlayView);
		title_search.setOnClickListener(this);
		tvCity.setOnClickListener(this);
		btRq.setOnClickListener(this);
		btJg.setOnClickListener(this);
		btHp.setOnClickListener(this);
		btJl.setOnClickListener(this);
		btQx.setOnClickListener(this);
		xListView.setPullLoadEnable(true);
		xListView.setPullRefreshEnable(true);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(new ItemtClickListener());
	}

	@Override
	protected void init() {
		mHander = new Handler();
		tvTitle.setText("不夜程");
		/*
		 * mPeopleFragment = new PromotionListFragment(mApplication, this,
		 * this); //mGroupFragment = new NearByGroupFragment(mApplication, this,
		 * this); FragmentTransaction ft =
		 * getSupportFragmentManager().beginTransaction();
		 * ft.replace(R.id.nearby_layout_content, mPeopleFragment).commit();
		 */
	}

	

	
	private class ItemtClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
				ActivesEntity bean  = new ActivesEntity();
				TextView tv = (TextView) view.findViewById(R.id.tv_title);
				bean = (ActivesEntity) tv.getTag();
				Bundle bundle = new Bundle();
				bundle.putString("id", bean.getAct_id());
				IntentJumpUtils.nextActivity(PromotionDetailActivity.class, PromotionActivity.this, bundle);
		}
	}
	
	
	/*
	 * public class OnSpinnerClickListener implements onSpinnerClickListener {
	 * 
	 * @Override public void onClick(boolean isSelect) { if (isSelect) {
	 * mPopupWindow .showViewTopCenter(findViewById(R.id.nearby_layout_root)); }
	 * else { mPopupWindow.dismiss(); } } }
	 */

	public class OnSearchClickListener implements onSearchListener {

		@Override
		public void onSearch(EditText et) {
			String s = et.getText().toString().trim();
			if (TextUtils.isEmpty(s)) {
				showCustomToast("请输入搜索关键字");
				et.requestFocus();
			} else {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(PromotionActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
					}

					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return false;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);
					}
				});
			}
		}

	}

	/**
	 * 
	 * @Title: getDataList
	 * 
	 * @Description: TODO(网络请求 活动列表数据)
	 * 
	 * @param 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	private void getDataList(final int currentPage,String cityid) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoadingDialog("正在加载,请稍后...");
			}

			@Override
			protected String doInBackground(Void... params) {

				Map<String, String> param = setParams(currentPage);

				return HttpRequest.submitPostData(
						PreferenceConstants.TONIGHT_SERVER, param, "UTF-8");
			}

			// [{"act_id":"46","address":"杭州市下城区再行路298号","des":"290元享价值总价值460元的雪花纯生啤酒套餐","endtime":"1451491200","img":"[\"0_0%E9%97%A8%E5%A4%B4.jpg\",\"0_1%E6%A0%BC%E5%B1%80.jpg\",\"0_2%E7%89%B9%E5%86%99.jpg\",\"0_3%E9%85%92%E6%B0%B4.jpg\",\"0_4%E5%A5%97%E9%A4%90.jpg\",\"0_5%E4%BA%BA%E7%89%A9.jpg\"]","lat":"30.312652","lon":"120.177033","mark":"0.0","name":"豪斯酒吧","price":"0.00","sales_num":"0","starttime":"1439827200","title":"畅销套餐","value":"0.00"}
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				dismissLoadingDialog();
				/*
				 * String str = null ; try { str = URLEncoder.encode(result,
				 * "utf-8"); } catch (UnsupportedEncodingException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("acts");
					mActives = resolveUtils(jsonArray);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//JSONArray array = new JSONArray(result);

				// mActives =
				// JSON.parseArray(jsonArray.toString(),ActivesEntity.class);
				mAdapter = new ActivesListAdapter(mApplication, mContext,mActives);
				xListView.setAdapter(mAdapter);
				//mAdapter.notifyDataSetChanged();
			}
		}.execute();

	}

	private List<ActivesEntity> resolveUtils(
			JSONArray jsonArray) throws JSONException {

		List<ActivesEntity> list = new ArrayList<ActivesEntity>();
		if (jsonArray != null) {
			ActivesEntity people = null;
			JSONObject object = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				object = jsonArray.getJSONObject(i);
				String act_id = object.getString("act_id");
				String title = object.getString("title");
				String des = object.getString("des");
				//0_0%E9%97%A8%E5%A4%B4.jpg
				String imgs = object.getString("img");
				String []imgArray =  imgs.split(",");
				String img = imgArray[0].substring(2, imgArray[0].length()-1);
				/*String img = null;
				try {
					img = URLEncoder.encode(imgStr, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				String value = object.getString("value");
				String price = object.getString("price");
				String starttime = object.getString("starttime");
				String endtime = object.getString("endtime");
				String sales_num = object.getString("sales_num");
				String name = object.getString("name");
				String address = object.getString("address");
				String lon = object.getString("lon");
				String lat = object.getString("lat");

				people = new ActivesEntity(act_id, title, des, img, value,
						price, starttime, endtime, sales_num, name, address,
						lon, lat);
				list.add(people);
			}

		}
		return list;
	}

	private Map<String, String> setParams(int currentPage) {

		Map<String, String> map = new HashMap<String, String>();
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("city", 179);
		parms.put("page", currentPage);
		ArrayList<Object> arry = new ArrayList<Object>();
		arry.add(0, "getActList");
		arry.add(1, 0);
		arry.add(2, parms);
		String data0 = RC4Utils.RC4("mdwi5uh2p41nd4ae23qy4",
				JsonUtils.list2json(arry));
		String encoded1 = "";
		try {
			encoded1 = new String(Base64Utils.encode(
					data0.getBytes("ISO-8859-1"), 0, data0.length()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("base64编码后：     " + encoded1);
		String decode = "";
		try {
			if (!encoded1.equals("")) {
				decode = new String(Base64.decode(encoded1, Base64.DEFAULT),
						"ISO-8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String data7 = RC4Utils.RC4("mdwi5uh2p41nd4ae23qy4", decode);
		map.put("d", encoded1);
		return map;

	}

	public class OnMiddleImageButtonClickListener implements
			onMiddleImageButtonClickListener {

		@Override
		public void onClick() {
		}
	}

	/*
	 * public class OnSwitcherButtonClickListener implements
	 * onSwitcherButtonClickListener {
	 * 
	 * @Override public void onClick(SwitcherButtonState state) {
	 * FragmentTransaction ft = getSupportFragmentManager() .beginTransaction();
	 * ft.setCustomAnimations(R.anim.fragment_fadein, R.anim.fragment_fadeout);
	 * switch (state) { case LEFT:
	 * mHeaderLayout.init(HeaderStyle.TITLE_NEARBY_PEOPLE);
	 * ft.replace(R.id.nearby_layout_content, mPeopleFragment) .commit(); break;
	 * 
	 * case RIGHT: mHeaderLayout.init(HeaderStyle.TITLE_NEARBY_GROUP);
	 * ft.replace(R.id.nearby_layout_content, mGroupFragment).commit(); break; }
	 * }
	 * 
	 * }
	 */

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_header_back://切换城市
			
			IntentJumpUtils.nextActivity(SelectCityActivity.class, PromotionActivity.this,null, 1001);
			
			break;
		case R.id.title_search://搜索
			
			mPopupWindow = new PopupWindow(sortlist, 450, LayoutParams.WRAP_CONTENT);
			mPopupWindow.showAsDropDown(index_head, ScreenUtils.getScreenWidth(mContext)/5, 200);
			break;
			
			case R.id.bt_rq:
				mPopupWindow.dismiss();
				sort = 1;
				getDataList(pageCount,cityId);
				break;
			case R.id.bt_jg:
				sort = 5;
				mPopupWindow.dismiss();
				getDataList(pageCount,cityId);
				break;
			case R.id.bt_hp:
				mPopupWindow.dismiss();
				getDataList(pageCount,cityId);
				break;
			case R.id.bt_jl:
				sort = 2;
				mPopupWindow.dismiss();
				getDataList(pageCount,cityId);
				break;
			case R.id.bt_qx:
				mPopupWindow.dismiss();
				break;
		default:
			break;
		}
	}

	
	@Override
	protected void onActivityResult( int requestCode,int resultCode,
            Intent imageReturnIntent) {
		/* if (resultCode != Activity.RESULT_OK)
	            return;*/
		
		 if(requestCode==1001){
			cityId =  imageReturnIntent.getExtras().getString("id");
			cityName = imageReturnIntent.getExtras().getString("name");
			//getDataList(currentPage,cityId);
			tvCity.setText(cityName);
			
		 }
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	protected void loadMore() {
		currentPage++;// 每次 点击加载更多，请求页码 +1
		// flagLoadMore = true;
		getDataList(currentPage,cityId);
	}

	@Override
	public void onRefresh() {
		mHander.postDelayed(new Runnable() {// 处理耗时 操作
					@Override
					public void run() {
						Date currentTime = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd H:mm:ss");
						String dateString = formatter.format(currentTime);
						// flagLoadMore = false;
						currentPage = 1;
						getDataList(currentPage,cityId);
						xListView.stopRefresh();
						xListView.stopLoadMore();
						xListView.setRefreshTime(dateString);
					}
				}, 1000);

	}

	@Override
	public void onLoadMore() {
		mHander.postDelayed(new Runnable() {// 处理耗时 操作
					@Override
					public void run() {
						loadMore();
						xListView.stopRefresh();
						xListView.stopLoadMore();
					}
				}, 200);

	}
}
