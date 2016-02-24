# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  account_id                bigint auto_increment not null,
  name                      varchar(255),
  room_id                   bigint,
  constraint pk_account primary key (account_id))
;

create table music (
  music_id                  bigint auto_increment not null,
  artist                    varchar(255),
  title                     varchar(255),
  constraint pk_music primary key (music_id))
;

create table now_room (
  room_name                 varchar(255) not null,
  room_id                   bigint,
  constraint pk_now_room primary key (room_name))
;

create table room (
  room_id                   bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_room primary key (room_id))
;

create table sung_music (
  sung_id                   bigint auto_increment not null,
  account_account_id        bigint,
  room_room_id              bigint,
  music_music_id            bigint,
  constraint pk_sung_music primary key (sung_id))
;

create sequence now_room_seq;

alter table sung_music add constraint fk_sung_music_account_1 foreign key (account_account_id) references account (account_id) on delete restrict on update restrict;
create index ix_sung_music_account_1 on sung_music (account_account_id);
alter table sung_music add constraint fk_sung_music_room_2 foreign key (room_room_id) references room (room_id) on delete restrict on update restrict;
create index ix_sung_music_room_2 on sung_music (room_room_id);
alter table sung_music add constraint fk_sung_music_music_3 foreign key (music_music_id) references music (music_id) on delete restrict on update restrict;
create index ix_sung_music_music_3 on sung_music (music_music_id);


create index ix_now_room_room_id_4 on now_room(room_id);
create index ix_room_name_5 on room(name);

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists account;

drop table if exists music;

drop table if exists now_room;

drop table if exists room;

drop table if exists sung_music;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists now_room_seq;

