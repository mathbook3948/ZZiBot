package com.github.mathbook3948.zzibot.dto.chzzk;


import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("ChannelResponseContent")
public class ChannelResponseContent implements ChzzkContentBase{
    private String channelId;
    private String channelName;
    private String channelImageUrl;
    private boolean verifiedMark;
    private String channelType;
    private String channelDescription;
    private int followerCount;
    private boolean openLive;
    private boolean subscriptionAvailability;
    private SubscriptionPaymentAvailability subscriptionPaymentAvailability;
    private boolean adMonetizationAvailability;
    private String[] activatedChannelBadgeIds;
    private boolean paidProductSaleAllowed;

    @Getter
    @Setter
    public static class SubscriptionPaymentAvailability {
        private boolean iapAvailability;
        private boolean iabAvailability;
    }
}