package me.xucan.safedrive.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.xucan.safedrive.R;
/**
 * Created by crazyxu on 2016-03-28.
 * 统一风格的list item 布局
 */
public class CustomItem extends RelativeLayout{
    //左侧标题,必选
    private TextView leftTv;
    //中部编辑框，可选
    private EditText centerEt;
    //右侧文本,可选
    private TextView rightTv;
    //右侧箭头，可选
    private ImageView rightArrow;
    //右侧图标，可选
    private ImageView rightImg;
    //底部分割线
    private View divider;

    public void setLeftTitle(String title){
        leftTv.setText(title);
    }

    public void setRightTv(String text){
        rightTv.setText(text);
        rightTv.setVisibility(VISIBLE);
    }


    public CustomItem(Context context) {
        this(context, null, 0);
    }

    public CustomItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttr(context, attrs);

    }

    void initView(Context context){
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_custom_layout,this,true);
        leftTv = (TextView)rootView.findViewById(R.id.tv_left);
        rightTv = (TextView)rootView.findViewById(R.id.tv_right);
        centerEt = (EditText)rootView.findViewById(R.id.et_center);
        rightArrow = (ImageView)rootView.findViewById(R.id.arrow_go);
        rightImg = (ImageView)rootView.findViewById(R.id.iv_right);
        divider = rootView.findViewById(R.id.divider_bottom);
    }

    void initAttr(Context context, AttributeSet attrs){
        if (attrs == null)
            return;
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.AcceListItem);

        //leftTv ,Required
        String leftTxt = array.getString(R.styleable.AcceListItem_leftTv);
        if (!TextUtils.isEmpty(leftTxt)){
            leftTv.setText(leftTxt);
        }else {
            int leftTxtId = array.getResourceId(R.styleable.AcceListItem_leftTv,-1);
            if (leftTxtId != -1)
                leftTv.setText(leftTxtId);
        }

        //rightArrow,default true
        boolean rightArrowBool = array.getBoolean(R.styleable.AcceListItem_rightArrow,true);
        if (!rightArrowBool){
            rightArrow.setVisibility(GONE);
        }

        //centerEt, default GONE
        boolean centerEtBool = array.getBoolean(R.styleable.AcceListItem_centerEt,false);
        if (centerEtBool){
            centerEt.setVisibility(VISIBLE);
            String hint = array.getString(R.styleable.AcceListItem_etHint);
            if (!TextUtils.isEmpty(hint))
                centerEt.setHint(hint);
            //是否可编辑
            centerEt.setEnabled(array.getBoolean(R.styleable.AcceListItem_etEnable,true));
            //输入框类型
            int inputType = array.getInt(R.styleable.AcceListItem_etInputType,-1);
            if (inputType != -1)
                centerEt.setInputType(inputType);

        }

        //设置rightTv
        String rightTvTxt = array.getString(R.styleable.AcceListItem_rightTv);
        if (!TextUtils.isEmpty(rightTvTxt)){
            rightTv.setText(rightTvTxt);
            rightTv.setVisibility(VISIBLE);
        }


        //rightImg,default GONE
        int rightImgRes = array.getResourceId(R.styleable.AcceListItem_rightImg,-1);
        if (rightImgRes != -1){
            rightImg.setVisibility(VISIBLE);
            //计算图片大小
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),rightImgRes);
            LayoutParams params = (LayoutParams) rightImg.getLayoutParams();
            params.width = params.height * bitmap.getWidth() / bitmap.getHeight();
            //如果没有右侧箭头，则设置靠右显示
            if (!rightArrowBool){
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
            rightImg.setLayoutParams(params);
            rightImg.setImageResource(rightImgRes);

        }



        //divider ,default false
        boolean dividerBool = array.getBoolean(R.styleable.AcceListItem_bottomDivider,false);
        if (dividerBool){
            divider.setVisibility(VISIBLE);
        }


    }

    //设置layout点击事件
    public void setOnClickListener(final OnLayoutClickListener listener){
        if (listener != null)
            this.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                }
            });
    }


    public interface OnLayoutClickListener{
        void onClick(View view);
    }
}
