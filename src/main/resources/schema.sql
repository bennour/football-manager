DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS teams;

CREATE TABLE IF NOT EXISTS teams (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    acronym VARCHAR(5) NOT NULL UNIQUE,
    budget DOUBLE NOT NULL
);

CREATE INDEX idx_team_name ON teams(name);
CREATE INDEX idx_team_acronym ON teams(acronym);
CREATE INDEX idx_team_budget ON teams(budget);

CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    team_id BIGINT,
    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
);

