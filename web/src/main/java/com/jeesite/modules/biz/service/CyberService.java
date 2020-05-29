package com.jeesite.modules.biz.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.jeesite.modules.biz.entity.BizCyber;
import com.jeesite.modules.sys.utils.ConfigUtils;
@Service
public class CyberService {
  @Autowired
  private Gson gson;
  @Autowired
  private RestTemplate restTemplate;
  public Map<String,BizCyber> getCybers(){
    String cyberUrl=ConfigUtils.getConfig("sys.cyber.url").getConfigValue();
    cyberUrl=StringUtils.isBlank(cyberUrl)?"http://gs.nxwh.org/cyber":cyberUrl;
    RequestEntity<?> request = RequestEntity.get(UriComponentsBuilder.fromUriString(cyberUrl).build().toUri()).accept(MediaType.APPLICATION_JSON).build();
    Type type = new TypeToken<List<BizCyber>>() {}.getType();
    List<BizCyber> response = gson.fromJson(restTemplate.exchange(request,String.class).getBody(),type);
    return response.stream().collect(Collectors.toMap(e->e.getGuid(),e->e));
  }
}
