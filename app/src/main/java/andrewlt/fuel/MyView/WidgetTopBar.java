package andrewlt.fuel.MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import andrewlt.fuel.R;

/**
 * Created by liut1 on 7/20/16.
 */
public class WidgetTopBar extends RelativeLayout {
    private ImageButton ibLeft;
    private ImageButton ibRight;
    private Button btnLeft;
    private Button btnRight;
    private TextView tvTitle;
    private String title;
    private String leftText;
    private String rightText;
    private Drawable leftDrawable;
    private Drawable rightDrawable;
    public WidgetTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化属性
        initAttrs(context, attrs);
        //填充视图
        View.inflate(context, R.layout.widget_top_bar, this);
        btnLeft = (Button) findViewById(R.id.btn_left_top_bar);
        ibLeft = (ImageButton) findViewById(R.id.ib_left_top_bar);
        tvTitle = (TextView) findViewById(R.id.tv_title_top_bar);
        btnRight = (Button) findViewById(R.id.btn_right_top_bar);
        ibRight = (ImageButton) findViewById(R.id.ib_right_top_bar);

        //如果属性有值的话，那我们就需要给控件初始化数据了
        initData();
    }
    //初始化属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WidgetTopBar);
        // WidgetTopBar_m_left这个名字很奇怪，我们明明没有定义，
        // 这是因为系统自动把我们定义的m_title加到WidgetTopBar后面去了
        //标题
        title = typedArray.getString(R.styleable.WidgetTopBar_m_title);
        //左边按钮的文字
        leftText = typedArray.getString(R.styleable.WidgetTopBar_m_left_text);
        //左边按钮的图片
        rightText = typedArray.getString(R.styleable.WidgetTopBar_m_right_text);
        //右边按钮的文字
        leftDrawable = typedArray.getDrawable(R.styleable.WidgetTopBar_m_left_image);
        //右边按钮的图片
        rightDrawable = typedArray.getDrawable(R.styleable.WidgetTopBar_m_right_image);
    }
    // 初始化数据,因为每个按钮，我都在布局文件中将其显示状态设置为GONE 了
    // 所以在填充数据的时候，要显示一下
    private void initData() {
        if (title != null) {
            tvTitle.setText(title);
        }
        if (leftText != null) {
            btnLeft.setVisibility(VISIBLE);
            btnLeft.setText(leftText);
        }
        if (rightText != null) {
            btnRight.setVisibility(VISIBLE);
            btnRight.setText(rightText);
        }
        if (leftDrawable != null) {
            ibLeft.setVisibility(VISIBLE);
            ibLeft.setBackground(leftDrawable);
        }
        if (rightDrawable != null) {
            ibRight.setVisibility(VISIBLE);
            ibRight.setBackground(rightDrawable);
        }
    }

    //获取左边文字按钮
    public Button getLeftBtnText() {
        return btnLeft;
    }

    //获取右边文字按钮
    public Button getRightBtnText() {
        return btnRight;
    }

    //获取左边图片按钮
    public ImageButton getLeftBtnImage() {
        return ibLeft;
    }

    //获取右边图片按钮
    public ImageButton getRightBtnImage() {
        return ibRight;
    }
}
