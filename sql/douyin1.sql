create database if not exists douyin1;
use douyin1;

create table douyin1.user
(
    id            varchar(8)  not null
        primary key,
    name          varchar(32) null,
    password      varchar(32) null,
    followCount   bigint      null,
    followerCount bigint      null
)
    comment '用户信息表';

create table douyin1.relation
(
    author_id    varchar(8) not null,
    favourite_id varchar(8) not null,
    constraint relation_pk
        unique (author_id, favourite_id),
    constraint relation_user_id_fk
        foreign key (author_id) references douyin1.user (id)
            on update cascade on delete cascade,
    constraint relation_user_id_fk_2
        foreign key (favourite_id) references douyin1.user (id)
            on update cascade on delete cascade
)
    comment '粉丝表';

create index relation_favourite_id_index
    on douyin1.relation (favourite_id);

create table douyin1.video
(
    id              varchar(8)   not null
        primary key,
    author_id       varchar(8)   null,
    play_url        varchar(255) null,
    cover_url       varchar(255) null,
    favourite_count bigint       null,
    comment_count   bigint       null,
    create_time     datetime     null,
    constraint Video_id_uindex
        unique (id),
    constraint Video_user_id_fk
        foreign key (author_id) references douyin1.user (id)
            on update cascade on delete cascade
)
    comment '视频信息表';


create table douyin1.comment
(
    comment_id   varchar(8)   not null
        primary key,
    user_id      varchar(8)   null,
    video_id     varchar(8)   null,
    comment_text varchar(255) null,
    comment_data datetime     null,
    constraint comment_user_id_fk
        foreign key (user_id) references douyin1.user (id)
            on update cascade on delete cascade,
    constraint comment_video_id_fk
        foreign key (video_id) references douyin1.video (id)
            on update cascade on delete cascade
)
    comment '用户评论表';

create table douyin1.favourite
(
    user_id  varchar(8) not null,
    video_id varchar(8) not null,
    primary key (user_id, video_id),
    constraint favourite_user_id_fk
        foreign key (user_id) references douyin1.user (id)
            on update cascade on delete cascade,
    constraint favourite_video_id_fk
        foreign key (video_id) references douyin1.video (id)
            on update cascade on delete cascade
)
    comment '用户点赞表';


