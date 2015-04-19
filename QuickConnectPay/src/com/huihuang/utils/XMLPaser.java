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

import com.huichuang.http.RequestResult;
import com.huichuang.test.bean.BackrParameterModel;

	import android.content.Context;
import android.util.Log;
import android.util.Xml;

	public class XMLPaser {

		

		public static Map<String,Object>  parseXml(String xml) throws Exception {//path:你要解析的xml的路径
			Map<String, Object> map=new HashMap<String, Object>();
			ByteArrayInputStream tInputStringStream = null;  
			   
			  if (xml != null && !xml.trim().equals("")) {  
			   tInputStringStream = new ByteArrayInputStream(xml.getBytes());  
			  }  
			  
			  XmlPullParser parser = Xml.newPullParser();  
			
			   parser.setInput(tInputStringStream, "UTF-8");  
			 //得到事件(事件模型驱动)
				int type = parser.getEventType();
				RequestResult person = null;
				BackrParameterModel backrParameter=null;
				//直到解析到文档结尾 结束
				while(type!=XmlPullParser.END_DOCUMENT){
					//读取里面的子节点
					//读取该标签的标签头
					String name = parser.getName();
					switch (type) {
					case XmlPullParser.START_TAG://标签头
//						if("persons".equals(name)){//碰到persons标签头的时候，new一个List<Person>
//						}
						if("res".equals(name)){//new Person
							person = new RequestResult();
//							//获得属性id的值
//							String id = parser.getAttributeValue(0);
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
							type = parser.next();
								if("trade_id".equals(name)){//new Person
									backrParameter = new BackrParameterModel();
								}else if("trade_id".equals(name)){
									backrParameter.setTrade_id(parser.nextText());
								}else if("send_state".equals(name)){
									backrParameter.setSend_state(parser.nextText());
								}else if("respBody".equals(name)){
									type = parser.next();
								}
						}
						break;
					case XmlPullParser.END_TAG://标签头
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
