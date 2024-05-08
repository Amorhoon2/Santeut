package com.santeut.auth.feign;

import com.santeut.auth.common.response.BasicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "guildFeign", url ="http://localhost:17650")
//@FeignClient(name = "guildFeign", url ="http://k10e201.p.ssafy.io:52713")
public interface GuildFeign {

    @GetMapping(path="/api/gulld/myguild")
    public BasicResponse myGuildList();
}
