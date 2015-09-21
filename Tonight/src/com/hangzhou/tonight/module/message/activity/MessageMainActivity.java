package com.hangzhou.tonight.module.message.activity;

import android.view.View;
import android.view.View.OnClickListener;

import com.hangzhou.tonight.R;
import com.hangzhou.tonight.module.base.BaseSingeFragmentActivity;
import com.hangzhou.tonight.module.base.CustomActionActivity;
import com.hangzhou.tonight.module.base.helper.model.TbarViewModel;
import com.hangzhou.tonight.module.message.fragment.CommentFragment;
import com.hangzhou.tonight.module.message.fragment.FriendMessageFragment;
import com.hangzhou.tonight.module.message.fragment.GoodFragment;
import com.hangzhou.tonight.module.message.fragment.ValidateMessageFragment;

/**
 * 消息主界面
 * 
 * @author hank
 */
public class MessageMainActivity extends CustomActionActivity{

	View vGood,vComment,vFriendMessage,vValidateMessage;
	@Override protected void doView() {
		setContentView(R.layout.activity_message_main);
		vGood    = findViewById(R.id.message_main_good);
		vComment = findViewById(R.id.message_main_comment);
		vFriendMessage = findViewById(R.id.message_main_friend_message);
		vValidateMessage = findViewById(R.id.message_main_validate_message);
	}

	@Override protected void doListeners() {
		vGood   .setOnClickListener(itemClick);
		vComment.setOnClickListener(itemClick);
		vFriendMessage.setOnClickListener(itemClick);
		vValidateMessage.setOnClickListener(itemClick);
	}

	@Override protected void doHandler() {
		setBackViewVisibility(View.GONE);
	}
	
	OnClickListener itemClick = new OnClickListener() {
		@Override public void onClick(View v) {
			if(v == vGood){
				BaseSingeFragmentActivity.startActivity(getActivity(), GoodFragment.class, new TbarViewModel(getResources().getString(R.string.good)));
			}else if(v == vComment){
				BaseSingeFragmentActivity.startActivity(getActivity(), CommentFragment.class, new TbarViewModel(getResources().getString(R.string.comment)));
			}else if(v == vFriendMessage){
				BaseSingeFragmentActivity.startActivity(getActivity(), FriendMessageFragment.class, new TbarViewModel(getResources().getString(R.string.friend_message)));
			}else if(v == vValidateMessage){
				BaseSingeFragmentActivity.startActivity(getActivity(), ValidateMessageFragment.class, new TbarViewModel(getResources().getString(R.string.validate_message)));
			}
		}
	};
}
