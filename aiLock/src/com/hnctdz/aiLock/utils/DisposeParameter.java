package com.hnctdz.aiLock.utils;

import org.apache.commons.lang.StringUtils;

/** 
 * @ClassName DisposeParameter.java
 * @Author WangXiangBo 
 */
public class DisposeParameter {
	

 /**
  * 获取指定参数后面的值(返回String)
  * @param src url
  * @param tag 指定参数
  * @return 获取后的值
  */
  public static String getStringValueFromTag(String src ,String tag){
	  String result = "";
	  if(null != src && !src.equalsIgnoreCase("")){
		  if(src.contains(tag)){
			  String temp = src.substring(src.indexOf(tag), src.length());
			  if(null != temp && temp.indexOf("&") != -1) {
				  result = temp.substring(tag.length()+1,temp.indexOf("&"));
			  }else{
				  result = temp.substring(tag.length()+1,temp.length());
			  }
		  }
	  }
	  return result;
  }
  
  /**
   * 获取指定参数后面的值(返回String)
   * @param src url json格式
   * @param tag 指定参数
   * @return 获取后的值
   */
	public static String getStringValueFromTagToJson(String src, String tag) {
		String result = "";
		if (null != src && !src.equals("")) {
			if (src.contains(tag)) {
				String temp = src.substring(src.indexOf(tag), src.length());
				int dkhInd = temp.indexOf("}");
				int dhInd = temp.indexOf(",");
				if(dhInd != -1 && dkhInd != -1){
					if(dhInd < dkhInd){
						temp = temp.substring(tag.length(), dhInd);
					}else{
						temp = temp.substring(tag.length(), dkhInd);
					}
				}else{
					if(dhInd != -1){
						temp = temp.substring(tag.length(), dhInd);
					}else if(dkhInd != -1){
						temp = temp.substring(tag.length(), dkhInd);
					}
				}
				
				if (null != temp && temp.indexOf(":") != -1) {
					result = temp.substring(temp.indexOf(":") + 1, temp.length());
				}
				
			}
		}
		// 去除json格式中的"号
		if (StringUtil.isNotEmpty(result)) {
			result = result.replaceAll("\"", "").trim();
		}
		return result;
	}
	
	public static void main(String[] args) {
		String s = "\"ROUTE_VALUE\":\"18224284157\"}},\"BODY\":{\"RETURN_CODE\": 0 ,\"LOGIN_NO\":";
		System.out.println(DisposeParameter.getStringValueFromTagToJson(s, "RETURN_CODE"));
	}
  
  /**
   * 获取指定参数后面的值(返回Long )
   * @param src url
   * @param tag 指定参数
   * @return 获取后的值
   */
   public static Long getLongValueFromTag(String src ,String tag){
 	  String stringTemp="";
 	  Long result=0L;
 	  if(null!=src&&!src.equals("")){
 		  if(src.indexOf(tag)!=-1){
 			  String temp=src.substring(src.indexOf(tag),src.length());
 			 // System.out.println("temp:"+temp);
 			  if(null!=temp&&temp.indexOf("&")!=-1){
 				 stringTemp=temp.substring(tag.length()+1,temp.indexOf("&"));
 			  }else{
 				 stringTemp=temp.substring(tag.length()+1,temp.length());
 			  }
 		  }
 	  }
 	  if(null!=stringTemp&&!stringTemp.equals("")){
 		  result=Long.parseLong(stringTemp);
 	  }
 	  
 	  return result;
   }
  
   /**
    * 获取指定参数和后面的值(返回String)
    * @param src url
    * @param tag 指定参数
    * @return 获取后的值
    */
    public static String getParameterFromTag(String src ,String tag){
  	  String result="";
  	  if(null!=src&&!src.equals("")){
  		  if(src.indexOf(tag)!=-1){
  			  String temp=src.substring(src.indexOf(tag),src.length());
  			 // System.out.println("temp:"+temp);
  			  if(null!=temp&&temp.indexOf("&")!=-1){
  				  result=temp.substring(0,temp.indexOf("&"));
  			  }else{
  				  result=temp;
  			  }
  		  }else{
  			  result=tag+"=";
  		  }
  	  }
  	  
  	  
  	  return result;
    }
    /**
     * 判断url是否包括某个数据项
     * @param src url
     * @param tag 指定数据项
     * @return true or false
     */
   public static boolean containsKey(String src ,String tag){
	   boolean b=false;
	   if(null!=src&&src.indexOf(tag)!=-1){
		   b=true;
	   }
	   return b;
   }
   /**
	 * 替换开始字符和结束字符间的内容
	 * @param src 源字符串
	 * @param value 起始字符串
	 * @param tag 结束字符串
	 * @param newValue 替换的新内容
	 * @return
	 */
	public static String replaceValueFromSrc(String src,String startTag,String endTag ,String newValue){
		String endUrl=src;
		if(StringUtils.isNotEmpty(src)&&src.contains(startTag)&&src.contains(endTag)){
			StringBuffer sb=new StringBuffer(src);
			int start=src.indexOf(startTag);
			int end=src.indexOf(endTag, start);
			sb.replace(start+startTag.length(),end, newValue);
			endUrl=sb.toString();
		}
		return endUrl;
	}
 
}
