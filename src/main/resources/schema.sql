CREATE TABLE IF NOT EXISTS "films" (
  "id" int PRIMARY KEY AUTO_INCREMENT,
  "name" varchar NOT NULL,
  "description" varchar NOT NULL,
  "releaseDate" timestamp NOT NULL,
  "duration" float8 NOT NULL,
  "rating" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp),
  "updated_at" timestamp
);

CREATE TABLE IF NOT EXISTS "users" (
  "id" int auto_increment primary key,
  "name" varchar NOT NULL,
  "email" varchar NOT NULL,
  "login" varchar NOT NULL,
  "birthday" date NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp),
  "updated_at" timestamp
);

CREATE TABLE IF NOT EXISTS "genres" (
  "id" int PRIMARY KEY AUTO_INCREMENT,
  "name" varchar NOT NULL,
  "description" varchar,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp),
  "updated_at" timestamp
);

CREATE TABLE IF NOT EXISTS "ratings" (
  "id" int PRIMARY KEY AUTO_INCREMENT,
  "name" varchar NOT NULL,
  "description" varchar,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp),
  "updated_at" timestamp
);

CREATE TABLE IF NOT EXISTS "film_genre" (
  "id" int PRIMARY KEY AUTO_INCREMENT,
  "film_id" int NOT NULL,
  "genre_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp)
);

CREATE TABLE IF NOT EXISTS "user_film_likes" (
  "id" int PRIMARY KEY AUTO_INCREMENT,
  "film_id" int NOT NULL,
  "user_id" int NOT NULL,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp)
);

CREATE TABLE IF NOT EXISTS "friends" (
  "id" int PRIMARY KEY AUTO_INCREMENT,
  "user_id" int NOT NULL,
  "friend_id" int NOT NULL,
  "confirmed" boolean DEFAULT false,
  "created_at" timestamp NOT NULL DEFAULT (current_timestamp),
  "updated_at" timestamp
);

ALTER TABLE "films" ADD FOREIGN KEY ("rating") REFERENCES "ratings" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "film_genre" ADD FOREIGN KEY ("genre_id") REFERENCES "genres" ("id");

ALTER TABLE "user_film_likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("id");

ALTER TABLE "user_film_likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("id");
