//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.2.
//


package com.huichuang.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huichuang.quickconnectpay.R.id;
import com.huichuang.quickconnectpay.R.layout;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NewlandActivity_
    extends NewlandActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.newland_view_activity);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static NewlandActivity_.IntentBuilder_ intent(Context context) {
        return new NewlandActivity_.IntentBuilder_(context);
    }

    public static NewlandActivity_.IntentBuilder_ intent(Fragment supportFragment) {
        return new NewlandActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        gathering_progressbar = ((RelativeLayout) hasViews.findViewById(id.gathering_progressbar));
        expandableList = ((ExpandableListView) hasViews.findViewById(id.option_list));
        gathering_accountName = ((TextView) hasViews.findViewById(id.gathering_accountName));
        gathering_account = ((TextView) hasViews.findViewById(id.gathering_account));
        gathering_money = ((EditText) hasViews.findViewById(id.gathering_money));
        {
            View view = hasViews.findViewById(id.back);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        NewlandActivity_.this.myButtonClicked();
                    }

                }
                );
            }
        }
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<NewlandActivity_.IntentBuilder_>
    {

        private Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, NewlandActivity_.class);
        }

        public IntentBuilder_(Fragment fragment) {
            super(fragment.getActivity(), NewlandActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                super.startForResult(requestCode);
            }
        }

    }

}
