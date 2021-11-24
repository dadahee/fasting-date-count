DROP TABLE IF EXISTS `review`;

CREATE TABLE `review` (
    `id`	bigint	PRIMARY KEY AUTO_INCREMENT,
    `food_id`	bigint	NOT NULL,
    `date`	date	NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `title`	varchar(100)	NOT NULL,
    `content`	varchar(500)	NOT NULL,
    `fasted`	boolean	NOT NULL	DEFAULT 1,
    `created_at`	datetime	NOT NULL,
    `updated_at`	datetime	NOT NULL
);

DROP TABLE IF EXISTS `food`;

CREATE TABLE `food` (
    `id`	bigint	PRIMARY KEY AUTO_INCREMENT,
    `user_id`	bigint	NOT NULL,
    `name`	varchar(50)	NOT NULL,
    `start_date`	date	NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `day_count`	integer	NOT NULL	DEFAULT 0,
    `created_at`	datetime	NOT NULL,
    `updated_at`	datetime	NOT NULL
);


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id`	bigint	PRIMARY KEY AUTO_INCREMENT,
    `email`	varchar(30)	NOT NULL,
    `name`	varchar(20)	NOT NULL,
    `created_at`	datetime	NOT NULL,
    `updated_at`	datetime	NOT NULL
);

ALTER TABLE `food` ADD CONSTRAINT `FK_user_TO_food_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`);

ALTER TABLE `review` ADD CONSTRAINT `FK_food_TO_review_1`
    FOREIGN KEY (`food_id`)
    REFERENCES `food` (`id`);

