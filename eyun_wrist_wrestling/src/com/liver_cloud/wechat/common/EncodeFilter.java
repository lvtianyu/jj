package com.liver_cloud.wechat.common;

import javax.servlet.*;
import java.io.IOException;

public class EncodeFilter implements Filter {
  private FilterConfig filterConfig;
  private String encode;
  @Override
  public void destroy() {
    filterConfig=null;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain filterChain) throws IOException, ServletException {
    request.setCharacterEncoding(encode);
    //设置编码
    filterChain.doFilter(request, response);
    //调用FilterChain中的下一个过滤器，如果这是最后一个过滤器，调用请求的资源
    //过滤器是在传送参数之后，被接收到之前调用的

  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
    encode=filterConfig.getInitParameter("Encode");
    //读取web.xml中配置的参数
  }

}
