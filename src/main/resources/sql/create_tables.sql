
CREATE TABLE projects(
    id INTEGER PRIMARY KEY,
    title VARCHAR(50),
    file VARCHAR(100)
);

CREATE TABLE users(
    id INTEGER PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    project_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (project_id) REFERENCES projects(id)  
);

CREATE TABLE tasks (
    id INTEGER PRIMARY KEY,     
    theme VARCHAR(50) NOT NULL UNIQUE,
    taskType VARCHAR(50),
    priority INTEGER,
    description VARCHAR(200),
    user_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)    
);






