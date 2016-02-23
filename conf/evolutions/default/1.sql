# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table music (
  music_id                  bigint auto_increment not null,
  artist                    varchar(255),
  title                     varchar(255),
  constraint pk_music primary key (music_id))
;

create table user (
  user_id                   bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_user primary key (user_id))
;


create table user_music (
  user_user_id                   bigint not null,
  music_music_id                 bigint not null,
  constraint pk_user_music primary key (user_user_id, music_music_id))
;



alter table user_music add constraint fk_user_music_user_01 foreign key (user_user_id) references user (user_id) on delete restrict on update restrict;

alter table user_music add constraint fk_user_music_music_02 foreign key (music_music_id) references music (music_id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists music;

drop table if exists user_music;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

