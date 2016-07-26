package com.merchant.activity;

import com.merchant.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * @author yw
 * 商家接收智付手机插件返回数据界面
 */
public class MerchantPayResultActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_pay_result);
		
		TextView merchant_payResult = (TextView)this.findViewById(
				R.id.merchant_payResult);
			
		Bundle xmlData = getIntent().getExtras();
		if(xmlData!=null){
			String response = xmlData.getString("xml");
			System.out.println("response：" + response.length() + " -->" + response);
			try{
				String status = "<trade_status>";
				int start = response.indexOf(status);
				int end = response.indexOf("</trade_status>");
				String str = response.substring(start+status.length(), end);
				if("SUCCESS".equals(str)){
					merchant_payResult.setText("支付结果：支付成功");
				}else if("UNPAY".equals(str)){
					merchant_payResult.setText("支付结果：未支付");
				}else{
					merchant_payResult.setText("支付结果：支付失败");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode==event.KEYCODE_BACK){
//			Intent intent =new Intent(MerchantPayResultActivity.this,MainActivity.class);
//			startActivity(intent);
			
			this.finish();
		}
		return super.onKeyUp(keyCode, event);
	}
}
