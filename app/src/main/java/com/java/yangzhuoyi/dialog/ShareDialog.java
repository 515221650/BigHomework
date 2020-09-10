package com.java.yangzhuoyi.dialog;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import com.java.yangzhuoyi.R;
import com.java.yangzhuoyi.fragment.EventFragment;
import com.java.yangzhuoyi.util.T;
import com.java.yangzhuoyi.util.WebConstants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ShareDialog extends BottomBaseDialog<ShareDialog> {
    @Bind(R.id.ll_weibo) LinearLayout mLlWeibo;
    @Bind(R.id.ll_wechat_friend) LinearLayout mLlWechatFriend;
    @Bind(R.id.ll_qq) LinearLayout mLlQq;
    @Bind(R.id.ll_sms) LinearLayout mLlSms;
    Context mcontex;
    private PriorityListener listener;

    public ShareDialog(Context context, View animateView, PriorityListener listener) {
        super(context, animateView);
        mcontex = context;
        this.listener = listener;
    }

    public ShareDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(mContext, R.layout.share, null);
        ButterKnife.bind(this, inflate);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mLlWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getSource("weibo");
                dismiss();
            }
        });
        mLlWechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getSource("wechat");
                dismiss();
            }
        });
        mLlQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getSource("qq");
                dismiss();
            }
        });
        mLlSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getSource("message");
                dismiss();
            }
        });
    }

    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        public void getSource(String string);
    }
}