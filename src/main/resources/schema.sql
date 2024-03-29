create table if not exists users(id uuid default random_uuid() primary key, username varchar(20) unique not null, email varchar(255) unique not null,password varchar(20) not null, role varchar(255));
create table if not exists notes(id uuid default random_uuid() primary key, title varchar(255) not null, text varchar(5000), user_Id uuid, foreign key(user_Id) references users(id));
