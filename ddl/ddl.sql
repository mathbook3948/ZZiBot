create table chzzk_live_status
(
    id                                serial
        primary key,
    channel_id                        varchar(64) not null,
    live_title                        text,
    status                            varchar(16),
    concurrent_user_count             integer,
    accumulate_count                  integer,
    paid_promotion                    boolean,
    adult                             boolean,
    kr_only_viewing                   boolean,
    open_date                         timestamp,
    close_date                        timestamp,
    clip_active                       boolean,
    chat_channel_id                   varchar(64),
    tags                              text[],
    category_type                     varchar(64),
    live_category                     varchar(64),
    live_category_value               varchar(128),
    live_polling_status_json          text,
    fault_status                      text,
    user_adult_status                 varchar(32),
    abroad_country                    boolean,
    blind_type                        varchar(64),
    chat_active                       boolean,
    chat_available_group              varchar(64),
    chat_available_condition          varchar(64),
    min_follower_minute               integer,
    allow_subscriber_in_follower_mode boolean,
    chat_slow_mode_sec                integer,
    chat_emoji_mode                   boolean,
    chat_donation_ranking_exposure    boolean,
    drops_campaign_no                 varchar(64),
    live_token_list                   text[],
    watch_party_no                    varchar(64),
    watch_party_tag                   varchar(64),
    time_machine_active               boolean,
    last_adult_release_date           timestamp,
    last_kr_only_viewing_release_date timestamp,
    created_at                        timestamp default now(),
    updated_at                        timestamp default now(),
    channel_name                      varchar(100)
);

create table live_alarm_channel
(
    channel_id varchar(64) not null
        constraint unique_channel_id
            unique,
    guild_id   varchar(64) not null,
    primary key (channel_id, guild_id)
);

create index idx_live_alarm_channel_guild_id
    on live_alarm_channel (guild_id);

create unique index uq_live_alarm_channel_channel_id
    on live_alarm_channel (channel_id);

create table live_subscription
(
    guild_id   varchar(50) not null,
    channel_id varchar(64) not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    primary key (guild_id, channel_id)
);

create index idx_live_subscription_channel_id
    on live_subscription (channel_id);

create index idx_live_subscription_guild_id
    on live_subscription (guild_id);

CREATE TABLE admin (
                       admin_id varchar(50) NOT NULL,
                       admin_name varchar(255) NOT NULL,
                       admin_password varchar NOT NULL,
                       admin_refreshtoken varchar,
                       admin_is_enable boolean DEFAULT true,
                       admin_created_at timestamp DEFAULT CURRENT_TIMESTAMP,
                       admin_updated_at timestamp,
                       admin_is_superadmin boolean DEFAULT false,
                       PRIMARY KEY (admin_id)
);

create table discord_log
(
    discord_log_idx        serial
        primary key,
    discord_log_command    varchar(255) not null,
    discord_log_is_success boolean      not null,
    discord_log_time       timestamp default CURRENT_TIMESTAMP,
    discord_log_content    text,
    discord_log_channel_id varchar(64),
    discord_log_guild_id   varchar(64),
    discord_log_user_id    varchar(255),
    discord_log_user_tag   varchar(255)
);