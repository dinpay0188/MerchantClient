package com.merchant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.dinpay.plugin.activity.DinpayChannelActivity;
import com.itrus.util.sign.RSAWithHardware;
import com.itrus.util.sign.RSAWithSoftware;
import com.merchant.R;
import com.merchant.model.OrderInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class MainActivity extends Activity {

    private EditText edt_merchantcode, edt_notifyurl, edt_version, edt_orderno,
            edt_ordertime, edt_orderamount, edt_productname, edt_redoflag, edt_productcode, edt_productnum,
            edt_produnctdesc, edt_extra_return_param;
    private Button btn_submit;
    private RadioGroup radiogroup;
    private RadioButton radioRSA_S, radioRSA;

    private String merchant_code, notify_url, interface_version, sign_type,
            order_no, order_time, order_amount, product_name, redo_flag, product_code,
            product_num, product_desc, extra_return_param;
         
    private String UserKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALf/+xHa1fDTCsLY"
			+ "PJLHy80aWq3djuV1T34sEsjp7UpLmV9zmOVMYXsoFNKQIcEzei4QdaqnVknzmIl7"
			+ "n1oXmAgHaSUF3qHjCttscDZcTWyrbXKSNr8arHv8hGJrfNB/Ea/+oSTIY7H5cAtW"
			+ "g6VmoPCHvqjafW8/UP60PdqYewrtAgMBAAECgYEAofXhsyK0RKoPg9jA4NabLuuu"
			+ "u/IU8ScklMQIuO8oHsiStXFUOSnVeImcYofaHmzIdDmqyU9IZgnUz9eQOcYg3Bot"
			+ "UdUPcGgoqAqDVtmftqjmldP6F6urFpXBazqBrrfJVIgLyNw4PGK6/EmdQxBEtqqg"
			+ "XppRv/ZVZzZPkwObEuECQQDenAam9eAuJYveHtAthkusutsVG5E3gJiXhRhoAqiS"
			+ "QC9mXLTgaWV7zJyA5zYPMvh6IviX/7H+Bqp14lT9wctFAkEA05ljSYShWTCFThtJ"
			+ "xJ2d8zq6xCjBgETAdhiH85O/VrdKpwITV/6psByUKp42IdqMJwOaBgnnct8iDK/T"
			+ "AJLniQJABdo+RodyVGRCUB2pRXkhZjInbl+iKr5jxKAIKzveqLGtTViknL3IoD+Z"
			+ "4b2yayXg6H0g4gYj7NTKCH1h1KYSrQJBALbgbcg/YbeU0NF1kibk1ns9+ebJFpvG"
			+ "T9SBVRZ2TjsjBNkcWR2HEp8LxB6lSEGwActCOJ8Zdjh4kpQGbcWkMYkCQAXBTFiy"
			+ "yImO+sfCccVuDSsWS+9jrc5KadHGIvhfoRjIj2VuUKzJ+mXbmXuXnOYmsAefjnMC"
			+ "I6gGtaqkzl527tw=";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        edt_merchantcode = (EditText) this.findViewById(R.id.edt_merchantcode);
        edt_notifyurl = (EditText) this.findViewById(R.id.edt_notifyurl);
        edt_version = (EditText) this.findViewById(R.id.edt_version);

        edt_orderno = (EditText) this.findViewById(R.id.edt_orderno);
        edt_ordertime = (EditText) this.findViewById(R.id.edt_ordertime);
        edt_orderamount = (EditText) this.findViewById(R.id.edt_orderamount);
        edt_productname = (EditText) this.findViewById(R.id.edt_productname);
        
        edt_redoflag = (EditText) this.findViewById(R.id.edt_redoflag);
        edt_productcode = (EditText) this.findViewById(R.id.edt_productcode);
        edt_productnum = (EditText) this.findViewById(R.id.edt_productnum);
        edt_produnctdesc = (EditText) this.findViewById(R.id.edt_produnctdesc);
        edt_extra_return_param = (EditText) this.findViewById(R.id.edt_extra_return_param);
        
        radiogroup = (RadioGroup) this.findViewById(R.id.radiogroup);
        radioRSA_S = (RadioButton) this.findViewById(R.id.radioRSA_S);
        radioRSA = (RadioButton) this.findViewById(R.id.radioRSA);
        btn_submit = (Button) this.findViewById(R.id.btn_submit);

        edt_merchantcode.setText("1111110166");	//商户号(商户需替换成自己的商户号)
        edt_notifyurl.setText("http://15l0549c66.iask.in:11999/B2C_Dinpay/Notify_Url.jsp"); //服务器异步通知地址(商户需替换成自己的服务通知地址)
        edt_version.setText("V3.0"); //接口版本
        
        edt_orderno.setText(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); //商户网站唯一订单号
        edt_ordertime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //商户订单时间
        edt_orderamount.setText("0.01"); //商户订单总金额
        edt_productname.setText("productname"); //商品名称
        
        edt_redoflag.setText("1"); //订单是否允许重复标识  可选
        edt_productcode.setText(""); //商品编号   可选
        edt_productnum.setText(""); //商品数量   可选
        edt_produnctdesc.setText(""); //商品描述   可选
        edt_extra_return_param.setText(""); //公用回传参数    可选

        sign_type="RSA-S"; //初始化
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == radioRSA_S.getId()) {
                    sign_type = "RSA-S";
                } else {
                    sign_type = "RSA";
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pay();
            }
        });        
    }

    @Override
    protected void onResume() {
    	edt_orderno.setText(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); //商户网站唯一订单号
        edt_ordertime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //商户订单时间
        super.onResume();
    }

    private void pay() {
        merchant_code = edt_merchantcode.getText().toString();
        notify_url = edt_notifyurl.getText().toString();
        interface_version = edt_version.getText().toString();
        order_no = edt_orderno.getText().toString();
        order_time = edt_ordertime.getText().toString();
        order_amount = edt_orderamount.getText().toString();
        product_name = edt_productname.getText().toString();
        redo_flag = edt_redoflag.getText().toString();
        product_code = edt_productcode.getText().toString();
        product_num = edt_productnum.getText().toString();
        product_desc = edt_produnctdesc.getText().toString();
        extra_return_param = edt_extra_return_param.getText().toString();

        OrderInfo info = new OrderInfo();
        info.setMerchant_code(merchant_code);
        info.setNotify_url(notify_url);
        info.setInterface_version(interface_version);
        info.setOrder_no(order_no);
        info.setOrder_time(order_time);
        info.setOrder_amount(order_amount);
        info.setProduct_name(product_name);
        info.setRedo_flag(redo_flag); //订单是否允许重复标识  可选
        info.setProduct_code(product_code); //商品编号   可选
        info.setProduct_num(product_num); //商品数量   可选
        info.setProduct_desc(product_desc); //商品描述   可选
        info.setExtra_return_param(extra_return_param); //公用回传参数    可选

        //组织签名规则格式
        Map<String, String> maps = info.getMap();
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            if (!TextUtils.isEmpty(value)) {
                sb.append(entry.getKey() + "=" + value + "&");
            }
        }

        String sign = sb.toString().substring(0, sb.toString().length() - 1);
        System.out.println("商家发送的签名规则字符串格式：" + sign.length() + " -->" + sign);
        try {
            if(sign_type.equals("RSA-S")){
                //RSA-s签名方式
                sign = getRSASSignature(sign);
            }else{
                //RSA签名方式
                sign = getRSASignature(sign);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sign = sign.replaceAll("\\+", "%2B");

        if(merchant_code.equals("")){
            Toast.makeText(this, "请输入您注册的商家号", Toast.LENGTH_SHORT).show();
            return;
        }

        if(notify_url.equals("")){
            Toast.makeText(this, "请填写异步通知地址", Toast.LENGTH_SHORT).show();
           return;
        }

        //组织报文
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<dinpay><request><merchant_code>" + info.getMerchant_code() + "</merchant_code>" +
                "<notify_url>" + info.getNotify_url() + "</notify_url>" +
                "<interface_version>" + info.getInterface_version() + "</interface_version>" +
                "<sign_type>" + sign_type + "</sign_type>" +
                "<sign>" + sign + "</sign>" +
                "<trade><order_no>" + info.getOrder_no() + "</order_no>" +
                "<order_time>" + info.getOrder_time() + "</order_time>" +
                "<order_amount>" + info.getOrder_amount() + "</order_amount>" +
                "<product_name>" + info.getProduct_name() + "</product_name>" +
                "<redo_flag>" + info.getRedo_flag() + "</redo_flag>" +
                "<product_code>" + info.getProduct_code() + "</product_code>" +
                "<product_num>" + info.getProduct_num() + "</product_num>" +
                "<product_desc>" + info.getProduct_desc() + "</product_desc>" +
                "<extra_return_param>" + info.getExtra_return_param() + "</extra_return_param>" +
                "</trade></request></dinpay>";
        Log.i("xml=", xml);
        Intent intent = new Intent(this, DinpayChannelActivity.class);
        intent.putExtra("xml", xml);
        intent.putExtra("ActivityName", "com.merchant.activity.MerchantPayResultActivity");
        startActivity(intent);
    }
    
//------------------数据签名，支持RSA，和RSA_S两种签名方式，在后台进行数据签名传给客户端调起插件------------------------
//------------------具体后台签名规则，签名方法见文档及后台开发包-------------------------------------------------------
    /**
     * RSA-S签名
     */
    private String getRSASSignature(String plainText) throws Exception {

        //商户秘钥,可以自己生成，也可以调用这里面的方法生成
        //注意，必须将秘钥上传到我们的智付商家后台，不然无法调用插件
        // （具体操作请参看我们RSA-S秘钥对的生成与使用文档）

        // Map<String, Object> keyMap2 = RSAWithSoftware.genKeyPair();
        // String UserKey = RSAWithSoftware.getPrivateKey(keyMap2);

        if(UserKey.equals("")){
            return "";
        }
        String signData = RSAWithSoftware.signByPrivateKey(plainText, UserKey);
        System.out.println("商家发送的RSA-S签名：" + signData.length() + " -->" + signData);
        return signData;
    }

    /**
     * RSA签名
     */
    private String getRSASignature(String plainText) throws Exception {

    	 if(UserKey.equals("")){
             return "";
         }
        String pfxPath = Environment.getExternalStorageDirectory() + "/" + "1111110166.pfx"; //商户自己填写服务端的pfx证书路径，证书从我们的官方商家后台里面下载
        String pfxPass = "87654321"; // 商家pfx文件密码
        RSAWithHardware mh = new RSAWithHardware();
        mh.initSigner(pfxPath, pfxPass); 
        String signData = mh.signByPriKey(plainText);
        System.out.println("商家发送的RSA签名：" + signData.length() + " -->" + signData);
        return signData;
    }
}
