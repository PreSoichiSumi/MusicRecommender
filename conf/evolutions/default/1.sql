# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  account_id                bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_account primary key (account_id))
;

create table music (
  music_id                  bigint auto_increment not null,
  artist                    varchar(255),
  title                     varchar(255),
  constraint pk_music primary key (music_id))
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

alter table sung_music add constraint fk_sung_music_account_1 foreign key (account_account_id) references account (account_id) on delete restrict on update restrict;
create index ix_sung_music_account_1 on sung_music (account_account_id);
alter table sung_music add constraint fk_sung_music_room_2 foreign key (room_room_id) references room (room_id) on delete restrict on update restrict;
create index ix_sung_music_room_2 on sung_music (room_room_id);
alter table sung_music add constraint fk_sung_music_music_3 foreign key (music_music_id) references music (music_id) on delete restrict on update restrict;
create index ix_sung_music_music_3 on sung_music (music_music_id);


create index ix_room_name_4 on room(name);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table account;

drop table music;

drop table room;

drop table sung_music;

SET FOREIGN_KEY_CHECKS=1;

