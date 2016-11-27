package zjj.app.mobilesecurity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zjj.app.mobilesecurity.activities.HomeActivity;

public abstract class BaseHomePagerFragment extends BaseFragment{

    public void setUiColor(int color) {
        ((HomeActivity)getActivity()).getToolbar().setBackgroundColor(color);
    }

}
