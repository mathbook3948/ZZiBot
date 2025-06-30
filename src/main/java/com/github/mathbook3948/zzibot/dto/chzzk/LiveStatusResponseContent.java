package com.github.mathbook3948.zzibot.dto.chzzk;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Alias("LiveStatusResponseContent")
public class LiveStatusResponseContent {
        private String liveTitle;
        private String status;
        private int concurrentUserCount;
        private int accumulateCount;
        private boolean paidPromotion;
        private boolean adult;
        private boolean krOnlyViewing;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime openDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeDate;

        private boolean clipActive;
        private String chatChannelId;
        private List<String> tags;
        private String categoryType;
        private String liveCategory;
        private String liveCategoryValue;
        private String livePollingStatusJson;
        private String faultStatus;
        private String userAdultStatus;
        private boolean abroadCountry;
        private String blindType;
        private boolean chatActive;
        private String chatAvailableGroup;
        private String chatAvailableCondition;
        private int minFollowerMinute;
        private boolean allowSubscriberInFollowerMode;
        private int chatSlowModeSec;
        private boolean chatEmojiMode;
        private boolean chatDonationRankingExposure;
        private String dropsCampaignNo;
        private List<String> liveTokenList;
        private String watchPartyNo;
        private String watchPartyTag;
        private boolean timeMachineActive;
        private String channelId;
        private String lastAdultReleaseDate;
        private String lastKrOnlyViewingReleaseDate;
}
