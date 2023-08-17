create table utilisateur(
    id int auto_increment,
    nom varchar(250) not null,
    taille double,
    primary key(id)
);

create table config_siege_defaut(
    id int auto_increment,
    taille_min double not null,
    taille_max double not null,
    position int,
    primary key(id)
);

create table config_clim_defaut(
    id int auto_increment,
    heure_debut time,
    heure_fin time,
    degre double,
    primary key(id)
);

create table config_volant_defaut(
    id int auto_increment,
    taille_min double not null,
    taille_max double not null,
    position int,
    primary key(id)
);

create table config_clim (
    id int auto_increment,
    utilisateur_id int not null,
    heure_debut time,
    heure_fin time,
    degre double not null,
    primary key(id),
    constraint fk_config_clim_utilisateur foreign key(utilisateur_id) references utilisateur(id)
);

create table config_siege(
    id int auto_increment,
    utilisateur_id int not null,
    position int not null,
    primary key(id),
    constraint fk_config_siege_utilisateur foreign key(utilisateur_id) references utilisateur(id)
);

create table config_volant(
    id int auto_increment,
    utilisateur_id int not null,
    position int not null,
    primary key(id),
    constraint fk_config_volant_utilisateur foreign key(utilisateur_id) references utilisateur(id)
);