DELETE FROM players;
DELETE FROM teams;

ALTER TABLE teams ALTER COLUMN id RESTART WITH 1;
ALTER TABLE players ALTER COLUMN id RESTART WITH 1;
