package network.kekejl.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText ed = (EditText) findViewById(R.id.ed);
        tv = (TextView) findViewById(R.id.tv);

        ed.addTextChangedListener(new MyWatcher());
    }

    class MyWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            Log.e("tzh :","s : " + s + "=="+"count : "+count + "===" + "after" + after);

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            Log.e("tzh :","s : " + s + "=="+"start : "+start + "===" + "before" + before
                    +"===" + "count" + count);

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("tzh :","s : "+ s.toString());
            tv.setText(s.toString());
        }

    }

}
