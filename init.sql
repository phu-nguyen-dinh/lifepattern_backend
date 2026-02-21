-- Initialize LifePattern AI Database
CREATE DATABASE IF NOT EXISTS lifepattern_db;
USE lifepattern_db;

-- Grant privileges
GRANT ALL PRIVILEGES ON lifepattern_db.* TO 'lifepattern_user'@'%';
FLUSH PRIVILEGES;
