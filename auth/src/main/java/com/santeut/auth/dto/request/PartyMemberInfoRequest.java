package com.santeut.auth.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class PartyMemberInfoRequest {

  public int partyId;
  public List<Integer> userIdList;

}
