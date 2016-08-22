package zjj.app.mobilesecurity.fragments;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.base.BaseFragment;
import zjj.app.mobilesecurity.dao.BlackListDao;

/**
 * Created by DouJ on 4/08/2016.
 */
public class InputNumberFragment extends BaseFragment implements View.OnClickListener{

    private EditText et_number;
    private EditText et_name_optional;
    private Button btn_cancel;
    private Button btn_ok;
    private View view;
    private BlackListDao dao;

    public InputNumberFragment() {
    }

    public static InputNumberFragment newInstance() {
        InputNumberFragment fragment = new InputNumberFragment();
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_input_number, null);
        et_number = (EditText) view.findViewById(R.id.et_number);
        et_name_optional = (EditText) view.findViewById(R.id.et_name_optional);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);

        return view;
    }

    @Override
    public void initListener() {
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.btn_ok == id){
            String number = et_number.getText().toString().trim();
            String name = et_name_optional.getText().toString().trim();
            if(validate(number)){
                dao = new BlackListDao(context);
                dao.addNumber(number, name);
            }
        }else if(R.id.btn_cancel == id){

        }

        context.getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public boolean validate(String number){
        if(TextUtils.isEmpty(number)){
            Snackbar.make(view, "号码不能为空", Snackbar.LENGTH_SHORT).show();
            return false;
        }else if(number.length() > 24){
            Snackbar.make(view, "号码不能多于24位", Snackbar.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }


    }
}
