Music Recommender (team_e: ohsaka)【バックエンド】
====
![badge](https://heroku-badge.herokuapp.com/?app=musicrecommender)<br><br>
![music recommender](https://github.com/kentx422/Resource/blob/master/img/Intro.png?raw=true)  

カラオケで空気が読めるアプリ  
（RHD Winter Internship 2016）  

## Description

一緒に来た他の人の過去の歌唱履歴から、盛り下がらないであろう、  
むしろ盛り上がる楽曲を推薦するアプリケーション

##Equipment

* 構成

　　Java + PlayFramework + Heroku + MySQL + GraceNoteAPI

## Usage

* webAPIの仕様

　 /conf/routes　を参照して下さい <br>
　 例：<br>
　 # artist_name:String, page_length:Integer, page_number:Integer <br> 
　 GET	/search_music_artist_ranged_fast	<br>
　 →　http://musicrecommender.herokuapp.com/search_music_artist_ranged_fast?artist_name=aiko&page_length=10&page_number=0 <br>
　 
<br>
* ローカルで実行する場合

　1. リポジトリをクローン

　2. リポジトリ内のactivatorを実行

　3. runコマンドを実行<br>
　

* ローカルに開発環境を整えたいとき(eclipse)

　1. リポジトリをクローン

　2. リポジトリ内のactivatorを実行

　3. ecliseコマンドを実行

　4. リポジトリをeclipseプロジェクトとしてインポート<br>
<br>
## Authors

有馬諒　（バックエンド）  
鷲見創一（バックエンド）  
前田欣耶（フロントエンド）  
松井健人（フロントエンド）  

![music recommender](https://github.com/kentx422/Resource/blob/master/img/function.png?raw=true) 
![music recommender](https://github.com/kentx422/Resource/blob/master/img/iconBlue2.png?raw=true)  
