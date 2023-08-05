package com.planner.journeyplanner.apisLimit;

import com.planner.journeyplanner.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class ApiUsageDTO {

    private Long id;
    private Role userType;
    private int gptApiCount;
    private int mapApiCount;
    private Boolean runOutGpt;
    private Boolean runOutMap;

    public ApiUsageDTO(ApiUsage apiUsage, Role userType){
        this.id = apiUsage.getId();
        this.userType = userType;
        this.gptApiCount = apiUsage.getGptApiCount();
        this.mapApiCount = apiUsage.getMapApiCount();
        this.runOutGpt = apiUsage.getRunOutGpt();
        this.runOutMap = apiUsage.getRunOutMap();
    }

}
