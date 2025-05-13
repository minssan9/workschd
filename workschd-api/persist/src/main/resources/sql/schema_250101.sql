create table workschd.Schedule
(
    id               bigint auto_increment
        primary key,
    date             date         null,
    employeeName     varchar(255) null,
    shift            varchar(255) null,
    is_active        bit          null,
    created_at       datetime(6)  not null,
    created_by       varchar(255) not null,
    last_modified_at datetime(6)  null,
    last_modified_by varchar(255) null
);

create table workschd.account
(
    account_id        int auto_increment
        primary key,
    username          varchar(255)                       not null,
    english_name      varchar(255)                       null,
    email             varchar(255)                       null,
    phone             varchar(255)                       null,
    incentive_percent double   default 1                 not null,
    password          varchar(255)                       null,
    profile_image_url varchar(255)                       not null,
    access_token      varchar(255)                       null,
    refresh_token     varchar(255)                       null,
    profile_video_url varchar(255)                       null,
    firebase_token    varchar(255)                       null,
    is_active         bit                                null,
    created_at        datetime default CURRENT_TIMESTAMP null,
    created_by        varchar(255)                       null,
    last_modified_at  datetime(6)                        null,
    last_modified_by  varchar(255)                       null
)
    comment '	';

create table workschd.account_role
(
    account_id       int                                null,
    account_role_id  int auto_increment
        primary key,
    role_type        tinyint                            not null,
    is_active        bit                                null,
    created_at       datetime default CURRENT_TIMESTAMP null,
    created_by       varchar(255)                       null,
    last_modified_at datetime(6)                        null,
    last_modified_by varchar(255)                       null,
    constraint FK1f8y4iy71kb1arff79s71j0dh
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.account_sns
(
    account_id        int                                null,
    account_sns_id    int auto_increment
        primary key,
    user_id           varchar(255)                       not null,
    sns_email         varchar(255)                       null,
    provider_type     tinyint                            not null,
    sns_phone         varchar(255)                       null,
    expired           datetime                           null,
    email_verified_yn varchar(255)                       not null,
    profile_image_url varchar(255)                       null,
    profile_video_url varchar(255)                       null,
    access_token      varchar(255)                       null,
    refresh_token     varchar(255)                       null,
    is_active         bit                                null,
    created_at        datetime default CURRENT_TIMESTAMP null,
    created_by        varchar(255)                       null,
    last_modified_at  datetime(6)                        null,
    last_modified_by  varchar(255)                       null,
    constraint account_sns_account_sns_id_uindex
        unique (account_sns_id),
    constraint FKhnl104h4u8wtwrtpbkt2x97sj
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.common_audit
(
    id               bigint auto_increment
        primary key,
    created_by       varchar(255)                         not null,
    created_at       timestamp  default CURRENT_TIMESTAMP null,
    is_active        tinyint(1) default 1                 null,
    last_modified_by varchar(255)                         null,
    last_modified_at timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create table workschd.accountWorkHour
(
    id            bigint auto_increment
        primary key,
    date          date         null,
    employee_name varchar(255) null,
    shift         varchar(255) null
);

create table workschd.task
(
    id               bigint auto_increment
        primary key,
    branch_id        bigint                               null,
    store_id         bigint                               null,
    additional_info  varchar(255)                         null,
    task_datetime    datetime                             null,
    start_time       time                                 null,
    end_time         time                                 null,
    daily_wage       decimal(38, 2)                       null,
    created_by       varchar(255)                         not null,
    created_at       timestamp  default CURRENT_TIMESTAMP null,
    is_active        tinyint(1) default 1                 null,
    last_modified_by varchar(255)                         null,
    last_modified_at timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    description      varchar(255)                         null,
    title            varchar(255)                         not null
);

create index branch_id
    on workschd.task (branch_id);

create index store_id
    on workschd.task (store_id);

create table workschd.team
(
    id                    bigint auto_increment
        primary key,
    name                  varchar(255)                         not null,
    region                varchar(255)                         not null,
    schedule_type         varchar(255)                         null,
    invitation_hash       varchar(255)                         null,
    invitation_created_at timestamp  default CURRENT_TIMESTAMP null,
    created_by            varchar(255)                         not null,
    created_at            timestamp  default CURRENT_TIMESTAMP null,
    is_active             tinyint(1) default 1                 null,
    last_modified_by      varchar(255)                         null,
    last_modified_at      timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    invitationCreatedAt   datetime(6)                          null,
    invitationHash        varchar(255)                         null,
    scheduleType          varchar(255)                         null
);

create table workschd.account_info
(
    id                       bigint auto_increment
        primary key,
    account_id               int                                  null,
    branch_id                bigint                               null,
    phone_number             varchar(255)                         null,
    email                    varchar(255)                         null,
    name                     varchar(255)                         null,
    preferred_branch_id      int                                  not null,
    preferred_day            varchar(255)                         null,
    unavailable_days_of_week varbinary(255)                       null,
    is_manager               tinyint(1) default 0                 null,
    is_active                tinyint(1) default 1                 null,
    created_by               varchar(255)                         null,
    created_at               timestamp  default CURRENT_TIMESTAMP null,
    last_modified_by         varchar(255)                         null,
    last_modified_at         timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint account_info_ibfk_1
        foreign key (account_id) references workschd.account (account_id),
    constraint account_info_ibfk_2
        foreign key (branch_id) references workschd.team (id)
);

create index account_id
    on workschd.account_info (account_id);

create index branch_id
    on workschd.account_info (branch_id);

create table workschd.attendance
(
    id                    bigint auto_increment
        primary key,
    branch_id             bigint                               null,
    task_id               bigint                               null,
    employee_id           bigint                               not null,
    attendance_date       date                                 not null,
    day_of_week           varchar(255)                         not null,
    start_time            time                                 null,
    end_time              time                                 null,
    actual_start_time     datetime                             null,
    actual_end_time       datetime                             null,
    calculated_daily_wage decimal(38, 2)                       null,
    created_by            varchar(255)                         not null,
    created_at            timestamp  default CURRENT_TIMESTAMP null,
    is_active             tinyint(1) default 1                 null,
    last_modified_by      varchar(255)                         null,
    last_modified_at      timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint attendance_ibfk_1
        foreign key (branch_id) references workschd.team (id),
    constraint attendance_ibfk_2
        foreign key (task_id) references workschd.task (id)
);

create table workschd.attendance_records
(
    id              bigint auto_increment
        primary key,
    employee_id     bigint      not null,
    branch_id       bigint      not null,
    attendance_date date        not null,
    day_of_week     varchar(10) not null,
    start_time      time        null,
    end_time        time        null,
    constraint attendance_records_ibfk_1
        foreign key (employee_id) references workschd.account_info (id)
            on delete cascade,
    constraint attendance_records_ibfk_2
        foreign key (branch_id) references workschd.team (id)
            on delete cascade
);

create index branch_id
    on workschd.attendance_records (branch_id);

create index employee_id
    on workschd.attendance_records (employee_id);

create table workschd.employee_off_days
(
    id              bigint auto_increment
        primary key,
    employee_id     bigint not null,
    off_date        date   not null,
    account_info_id bigint null,
    constraint FK6ghn66ucmmllf0861hqti441l
        foreign key (account_info_id) references workschd.account_info (id),
    constraint employee_off_days_ibfk_1
        foreign key (employee_id) references workschd.account_info (id)
            on delete cascade
);

create index employee_id
    on workschd.employee_off_days (employee_id);

create table workschd.store
(
    id               bigint auto_increment
        primary key,
    name             varchar(255)                         not null,
    address          varchar(255)                         null,
    region           varchar(255)                         null,
    branch_id        bigint                               null,
    created_by       varchar(255)                         not null,
    created_at       timestamp  default CURRENT_TIMESTAMP null,
    is_active        tinyint(1) default 1                 null,
    last_modified_by varchar(255)                         null,
    last_modified_at timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint store_ibfk_1
        foreign key (branch_id) references workschd.team (id)
);

create index branch_id
    on workschd.store (branch_id);

create table workschd.task_employee
(
    task_id          bigint                               not null,
    employee_id      bigint                               not null,
    created_by       varchar(255)                         not null,
    created_at       timestamp  default CURRENT_TIMESTAMP null,
    is_active        tinyint(1) default 1                 null,
    last_modified_by varchar(255)                         null,
    last_modified_at timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    id               bigint                               not null,
    account_id       int                                  not null,
    primary key (task_id, employee_id),
    constraint FK24frxilh67j7lp13gwst1kw65
        foreign key (account_id) references workschd.account (account_id),
    constraint FKm2nxa33p5b10b1x00ebppeyiv
        foreign key (task_id) references workschd.task (id),
    constraint task_employee_ibfk_2
        foreign key (employee_id) references workschd.account_info (id)
);

create index employee_id
    on workschd.task_employee (employee_id);

create table workschd.team_member
(
    id               bigint auto_increment
        primary key,
    account_id       int          null,
    team_id          bigint       null,
    is_active        bit          null,
    created_at       datetime(6)  not null,
    created_by       varchar(255) not null,
    last_modified_at datetime(6)  null,
    last_modified_by varchar(255) null,
    constraint FK9ubp79ei4tv4crd0r9n7u5i6e
        foreign key (team_id) references workschd.team (id),
    constraint FKrnuy6ny305t173h2l3x0hxl70
        foreign key (account_id) references workschd.account (account_id)
);

create table workschd.translations
(
    id               bigint auto_increment
        primary key,
    txt_key          varchar(255)                        not null,
    en               varchar(255)                        null,
    ko               varchar(255)                        null,
    fr               varchar(255)                        null,
    es               varchar(255)                        null,
    ja               varchar(255)                        null,
    is_active        bit                                 null,
    created_by       varchar(255)                        not null,
    last_modified_by varchar(255)                        null,
    created_at       timestamp default CURRENT_TIMESTAMP not null,
    last_modified_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

create table workschd.unavailable_dates
(
    id               bigint auto_increment
        primary key,
    account_id       int                                  null,
    date             date                                 not null,
    created_by       varchar(255)                         not null,
    created_at       timestamp  default CURRENT_TIMESTAMP null,
    is_active        tinyint(1) default 1                 null,
    last_modified_by varchar(255)                         null,
    last_modified_at timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    off_date         date                                 not null,
    account_info_id  bigint                               null,
    constraint FK34d6kpi3jbhfhm0k9t3c7bxv6
        foreign key (account_info_id) references workschd.account_info (id),
    constraint unavailable_dates_ibfk_1
        foreign key (account_id) references workschd.account (account_id)
);

create index account_id
    on workschd.unavailable_dates (account_id);

create table workschd.unavailable_days
(
    id               bigint auto_increment
        primary key,
    account_id       int                                                                                 null,
    day_of_week      enum ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday') not null,
    created_by       varchar(255)                                                                        not null,
    created_at       timestamp  default CURRENT_TIMESTAMP                                                null,
    is_active        tinyint(1) default 1                                                                null,
    last_modified_by varchar(255)                                                                        null,
    last_modified_at timestamp  default CURRENT_TIMESTAMP                                                null on update CURRENT_TIMESTAMP,
    constraint FK_unavailable_days_account_id
        foreign key (account_id) references workschd.account (account_id)
);

