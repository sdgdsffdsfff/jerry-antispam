create table antispam_keyword (
	`id` int(11) not null primary key auto_increment,
	`keyword` varchar(255) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='违禁词库';