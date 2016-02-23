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


create table account_music (
  account_account_id             bigint not null,
  music_music_id                 bigint not null,
  constraint pk_account_music primary key (account_account_id, music_music_id))
;



alter table account_music add constraint fk_account_music_account_01 foreign key (account_account_id) references account (account_id) on delete restrict on update restrict;

alter table account_music add constraint fk_account_music_music_02 foreign key (music_music_id) references music (music_id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table account;

drop table account_music;

drop table music;

SET FOREIGN_KEY_CHECKS=1;

