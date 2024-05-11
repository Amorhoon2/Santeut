package com.santeut.auth.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPartyMemberInfoResponse {

  List<PartyMemberInfo> partyMembers;

  @Data
  @AllArgsConstructor
  static public class PartyMemberInfo {
    public int userId;
    public String userNickname;
    public String userProfile;
  }

}
