package com.liver_cloud.wechat.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.liver_cloud.wechat.http.ServiceResult;

public class JsonUtil {

	public static ValueFilter NullValueFilter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			if (v == null)
				return "{}";
			return v;
		}
	};

	public static String toJSONString(Object object) {
		//return JSON.toJSONString(map, NullValueFilter);
		return JSON.toJSONString(object);
	}
	public static String toJSONStringNull(Map<?, ?> map) {
		return JSON.toJSONString(map, NullValueFilter);
//		return JSON.toJSONString(map);
	}
	public static String toJSONString1(Map<?, ?> map) {
		//return JSON.toJSONString(map, NullValueFilter);
		return JSON.toJSONString(map,SerializerFeature.DisableCircularReferenceDetect);
	}

	public static <T> String toJSONString(ServiceResult<T> response) {
		return JSON.toJSONString(response);
//		return JSON.toJSONString(map);
	}
	
	public static <T> T parseJson(String jsonString,Class<T> clazz){
		T parseObject = JSON.parseObject(jsonString,clazz);
		return parseObject;
	}
	
/*	  public static <T> Response<T> parseObject(String text, Type type) {
		    try {
		      @SuppressWarnings("unchecked")
		      Response<T> jsonResult = JSON.parseObject(text, Response.class);
		      if ("ok".equals(jsonResult.result)) {
		        if (type == String.class) {
		          return jsonResult;
		        }else{
		          
		          T parseObject = JSON.parseObject(jsonResult.getValues(), type);
		          jsonResult.setValuesObj(parseObject);
		        }
		        return jsonResult;

		      } else {
		        // 错误处理 TODO
		        Log.e(TAG, "error msg：" + jsonResult.getErrormsg());
		        return jsonResult;
		      }
		    } catch (JSONException e) {
		      Response<T> response = new Response<T>();
		      response.errorcode = Constants.ERROR_JSON;
		      response.errormsg = "解析http响应的JSON文本时发生异常";
		      return response;
		    }

		  }*/
}
