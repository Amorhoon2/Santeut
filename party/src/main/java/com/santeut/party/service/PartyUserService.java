package com.santeut.party.service;

import com.santeut.party.dto.response.HikingStartResponse;
import com.santeut.party.dto.response.PartyInfoResponseDto;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyUserService {

  void joinUserParty(int userId, int partyId);

  void deleteAllPartyUser(int partyId, char status);

  void withdrawUserFromParty(int userId, Integer partyId);

  Page<PartyInfoResponseDto> findMyParty(int userId, boolean includeEnd, LocalDate date,
      Pageable pageable);

  HikingStartResponse findMyHikingTrailByPartyUserId(int partyUserId);

}
