create table workschd.account
(
    account_id        int auto_increment
        primary key,
    is_active         bit                                          null,
    created_at        timestamp    default CURRENT_TIMESTAMP       not null,
    created_by        varchar(255) default 'SYSTEM'                null,
    last_modified_at  timestamp    default CURRENT_TIMESTAMP       null on update CURRENT_TIMESTAMP,
    last_modified_by  varchar(255)                                 null,
    access_token      varchar(255)                                 null,
    email             varchar(255)                                 null,
    password          varchar(255)                                 null,
    phone             varchar(255)                                 null,
    profile_image_url varchar(255)                                 null,
    profile_video_url varchar(255)                                 null,
    refresh_token     varchar(255)                                 null,
    status            enum ('ACTIVE', 'INACTIVE', 'PAUSE', 'STOP') not null,
    username          varchar(255)                                 not null
);

create table workschd.account_info
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                    null,
    created_at       timestamp    default CURRENT_TIMESTAMP not null,
    created_by       varchar(255) default 'SYSTEM'          null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                           null,
    account_id       int                                    null,
    constraint UKrgvpox2nifnul0el1gw1aqgoj
        unique (account_id),
    constraint FKf2vtn8ov4btro0wh94nbfq6ou
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.account_role
(
    account_role_id  int auto_increment
        primary key,
    is_active        bit                                              null,
    created_at       timestamp    default CURRENT_TIMESTAMP           not null,
    created_by       varchar(255) default 'SYSTEM'                    null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP           null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                                     null,
    role_type        enum ('MANAGER', 'OWNER', 'SCHEDULER', 'WORKER') not null,
    account_id       int                                              null,
    constraint FK1f8y4iy71kb1arff79s71j0dh
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.account_sns
(
    account_sns_id    int auto_increment
        primary key,
    is_active         bit                                                    null,
    created_at        timestamp    default CURRENT_TIMESTAMP                 not null,
    created_by        varchar(255) default 'SYSTEM'                          null,
    last_modified_at  timestamp    default CURRENT_TIMESTAMP                 null on update CURRENT_TIMESTAMP,
    last_modified_by  varchar(255)                                           null,
    access_token      varchar(255)                                           null,
    email_verified_yn varchar(255)                                           not null,
    expired           datetime(6)                                            null,
    profile_image_url varchar(255)                                           null,
    profile_video_url varchar(255)                                           null,
    provider_type     enum ('FACEBOOK', 'GOOGLE', 'KAKAO', 'LOCAL', 'NAVER') not null,
    refresh_token     varchar(255)                                           null,
    sns_email         varchar(255)                                           null,
    sns_phone         varchar(255)                                           null,
    user_id           varchar(255)                                           not null,
    account_id        int                                                    null,
    constraint FKhnl104h4u8wtwrtpbkt2x97sj
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.account_work_hour
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                    null,
    created_at       timestamp    default CURRENT_TIMESTAMP not null,
    created_by       varchar(255) default 'SYSTEM'          null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                           null,
    date             date                                   null,
    day              varchar(255)                           null,
    endTime          varchar(255)                           null,
    preferred        bit                                    null,
    startTime        varchar(255)                           null,
    account_id       int                                    null,
    constraint FKhrm5c1sr2avsw9hce6lvg7q40
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.account_work_off_dates
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                    null,
    created_at       timestamp    default CURRENT_TIMESTAMP not null,
    created_by       varchar(255) default 'SYSTEM'          null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                           null,
    off_date         date                                   not null,
    account_id       int                                    null,
    constraint FKqgve4i0xjup2snx4rha28l7sn
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.team
(
    id                  bigint auto_increment
        primary key,
    is_active           bit                                    null,
    created_at          timestamp    default CURRENT_TIMESTAMP not null,
    created_by          varchar(255) default 'SYSTEM'          null,
    last_modified_at    timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by    varchar(255)                           null,
    invitationCreatedAt datetime(6)                            null,
    invitationExpireAt  datetime(6)                            null,
    invitationHash      varchar(255)                           null,
    location            varchar(255)                           null,
    name                varchar(255)                           not null,
    region              varchar(255)                           not null,
    scheduleType        varchar(255)                           null
);

create table workschd.shop
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                    null,
    created_at       timestamp    default CURRENT_TIMESTAMP not null,
    created_by       varchar(255) default 'SYSTEM'          null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                           null,
    address          varchar(255)                           not null,
    description      text                                   null,
    district         varchar(50)                            null,
    latitude         double                                 null,
    longitude        double                                 null,
    name             varchar(100)                           not null,
    phone            varchar(20)                            null,
    postal_code      varchar(20)                            null,
    total_rooms      int                                    null,
    team_id          bigint                                 null,
    constraint FKdevc4ok9252bm4xtomy60i0t
        foreign key (team_id) references workschd.team (id)
);

create table workschd.task
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                                         null,
    created_at       timestamp    default CURRENT_TIMESTAMP                      not null,
    created_by       varchar(255) default 'SYSTEM'                               null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP                      null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                                                null,
    description      varchar(255)                                                null,
    end_date_time    varchar(255)                                                not null,
    start_date_time  varchar(255)                                                not null,
    status           enum ('CANCELLED', 'COMPLETED', 'IN_PROGRESS', 'SCHEDULED') not null,
    title            varchar(255)                                                not null,
    worker_count     int                                                         not null,
    shop_id          bigint                                                      not null,
    team_id          bigint                                                      not null,
    constraint UK1lmaryioklqx7u9b0tnmy0n11
        unique (shop_id),
    constraint FK2gq7kg0se05jd4lndll961ddb
        foreign key (shop_id) references workschd.shop (id),
    constraint FK6r32b6vk1rpu7ww7gratmce1i
        foreign key (team_id) references workschd.team (id)
);

create table workschd.task_employee
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                                            null,
    created_at       timestamp    default CURRENT_TIMESTAMP                         not null,
    created_by       varchar(255) default 'SYSTEM'                                  null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP                         null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                                                   null,
    approved_at      datetime(6)                                                    null,
    joined_at        datetime(6)                                                    null,
    left_at          datetime(6)                                                    null,
    rejected_at      datetime(6)                                                    null,
    rejection_reason varchar(255)                                                   null,
    request_date     datetime(6)                                                    null,
    status           enum ('ACTIVE', 'APPROVED', 'INACTIVE', 'PENDING', 'REJECTED') not null,
    account_id       int                                                            not null,
    task_id          bigint                                                         not null,
    constraint UKjcfksod2tef7oy4a2fc8wj3q2
        unique (account_id),
    constraint FK24frxilh67j7lp13gwst1kw65
        foreign key (account_id) references workschd.account (account_id),
    constraint FKm2nxa33p5b10b1x00ebppeyiv
        foreign key (task_id) references workschd.task (id)
);

create table workschd.team_member
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                    null,
    created_at       timestamp    default CURRENT_TIMESTAMP not null,
    created_by       varchar(255) default 'SYSTEM'          null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                           null,
    join_date        datetime(6)                            null,
    status           varchar(255)                           null,
    account_id       int                                    null,
    team_id          bigint                                 null,
    constraint UKok0iua0q1m5njv37l4brbwdr1
        unique (account_id),
    constraint FK9ubp79ei4tv4crd0r9n7u5i6e
        foreign key (team_id) references workschd.team (id),
    constraint FKrnuy6ny305t173h2l3x0hxl70
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.translations
(
    id               bigint auto_increment
        primary key,
    is_active        bit                                    null,
    created_at       timestamp    default CURRENT_TIMESTAMP not null,
    created_by       varchar(255) default 'SYSTEM'          null,
    last_modified_at timestamp    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_modified_by varchar(255)                           null,
    en               varchar(255)                           null,
    es               varchar(255)                           null,
    fr               varchar(255)                           null,
    ja               varchar(255)                           null,
    ko               varchar(255)                           null,
    txt_key          varchar(255)                           not null
);

