package com.huihuang.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.media.MediaMuxer.OutputFormat;
import android.provider.DocumentsContract.Document;
import android.sax.Element;

	import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

	import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.huichuang.log.L;
import com.huichuang.test.bean.BackrParameterModel;
import com.huichuang.test.bean.RequestResult;

	import android.content.Context;
import android.util.Log;
import android.util.Xml;

	public class XMLPaser {

		

		public static Map<String, Object> parseXml(String xml) throws Exception {//path:你要解析的xml的路径
			Map<String, Object> map=null;
			ByteArrayInputStream tInputStringStream = null;  
			  if (xml != null && !xml.trim().equals("")) {  
			   tInputStringStream = new ByteArrayInputStream(xml.getBytes());  
			  }  
			  XmlPullParser parser = Xml.newPullParser();  
			   parser.setInput(tInputStringStream, "UTF-8");  
			 //得到事件(事件模型驱动)
				int type = parser.getEventType();
				RequestResult person = null;
				//直到解析到文档结尾 结束
				while(type!=XmlPullParser.END_DOCUMENT){
					//读取里面的子节点
					//读取该标签的标签头
					String name = parser.getName();
					switch (type) {
					case XmlPullParser.START_DOCUMENT:
//						map=new HashMap<String, Object>();
						break;
					case XmlPullParser.START_TAG://标签头
						if("res".equals(name)){//new Person
							person = new RequestResult();
						}
						else if("returnCode".equals(name)){
							//parser.nextText()--->parser.next(),  parser.getText();
								person.setReturnCode(parser.nextText());//拿下一个文本
						}
						else if("comments".equals(name)){
							person.setComments(parser.nextText());
						}
						else if("resTime".equals(name)){
							person.setResTime(parser.nextText());
						}
						else if("respBody".equals(name)){
						}else if("trade_id".equals(name)){
							BackrParameterModel.getInstance().setTrade_id(parser.nextText());
						}else if("send_state".equals(name)){
							BackrParameterModel.getInstance().setSend_state(parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG://标签头
						if ("respBody".equals(name)) {
							person.setRespBody(BackrParameterModel.getInstance());
						}
						if("res".equals(name)){
							  map = BeanToMap.bean2Map(person);//一个person标签结束，就将该person数据加入到集合list
						}
						
						break;
 					default:
						break;
					}
					//每读一个节点就往下走，需要改变事件
					type = parser.next();
				}
			return map;
			  }
}
