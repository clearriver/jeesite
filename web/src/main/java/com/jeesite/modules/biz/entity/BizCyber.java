package com.jeesite.modules.biz.entity;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import lombok.Data;

@Data
public class BizCyber {
  private String guid;
  private String version;
  private List<Address> addr;
  @Data
  public static class Address{
    private String addr;
    private String rtsp;
  }
  public boolean isOnline() {
    boolean r=false;
    if(addr!=null) {
      for(Address a:addr) {
        if(StringUtils.isNotBlank(a.getRtsp())){
          r=true;break;
        }
      }
    }
    return r;
  }
  public boolean isInstall(){
    return addr!=null&&addr.size()>0;
  }
}
