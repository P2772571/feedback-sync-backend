-- -- Drop tables and types if they exist, for a clean start
-- DROP TABLE IF EXISTS PIP CASCADE;
-- DROP TABLE IF EXISTS Goals CASCADE;
-- DROP TABLE IF EXISTS Feedback_Request CASCADE;
-- DROP TABLE IF EXISTS Feedback CASCADE;
-- DROP TABLE IF EXISTS Profile CASCADE;
-- DROP TABLE IF EXISTS User CASCADE;
-- DROP TYPE IF EXISTS user_role_enum CASCADE;
--
-- -- Create ENUM type for user roles
-- -- This ENUM type defines the user roles available in the system.
-- -- ROLE_EMPLOYEE: Represents an employee
-- -- ROLE_MANAGER: Represents a manager
-- -- ROLE_ADMIN: Represents an admin
-- CREATE TYPE user_role_enum AS ENUM ('ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN');
--
-- -- Create User Table
-- -- This table stores core user information such as email, password, and role.
-- -- Each user is assigned a role using the ENUM type user_role_enum.
-- CREATE TABLE User (
--     user_id SERIAL PRIMARY KEY,          -- Unique identifier for each user
--     email VARCHAR(150) UNIQUE NOT NULL,  -- User's email (used for login)
--     password_hash VARCHAR(255) NOT NULL, -- Encrypted password for security
--     role user_role_enum NOT NULL,        -- ENUM to indicate user role (manager, employee, admin)
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp for when the user account was created
-- );
--
-- -- Create Profile Table
-- -- This table holds additional user details such as first name, last name, job title, and manager relationships.
-- -- Each user has a one-to-one relationship with their profile.
-- CREATE TABLE Profile (
--     profile_id SERIAL PRIMARY KEY,       -- Unique identifier for the profile
--     user_id INT NOT NULL UNIQUE,         -- Foreign key linking to the User table (each user has one profile)
--     first_name VARCHAR(100) NOT NULL,    -- User's first name
--     last_name VARCHAR(100) NOT NULL,     -- User's last name
--     job_title VARCHAR(150),              -- User's job title (optional)
--     manager_id INT DEFAULT NULL,         -- Foreign key linking to the user's manager (NULL for managers)
--     FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the user is deleted, the profile is deleted
--     FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE SET NULL -- If the manager is deleted, set manager_id to NULL
-- );
--
-- -- Create Feedback Table
-- -- This table stores feedback exchanged between users.
-- -- Users can give and receive feedback, and feedback is tied to both the giver and the receiver.
-- CREATE TABLE Feedback (
--     feedback_id SERIAL PRIMARY KEY,      -- Unique identifier for each feedback entry
--     giver_id INT NOT NULL,               -- Foreign key referencing the user giving the feedback
--     receiver_id INT NOT NULL,            -- Foreign key referencing the user receiving the feedback
--     content TEXT NOT NULL,               -- The feedback content/message
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the feedback was given
--     FOREIGN KEY (giver_id) REFERENCES User(user_id) ON DELETE CASCADE,  -- If the giver is deleted, delete their feedback
--     FOREIGN KEY (receiver_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the receiver is deleted, delete related feedback
-- );
--
-- -- Create Feedback_Request Table
-- -- This table stores feedback requests made by users.
-- -- Users can request feedback from others, and the request status is tracked (pending or completed).
-- CREATE TABLE Feedback_Request (
--     request_id SERIAL PRIMARY KEY,       -- Unique identifier for each feedback request
--     requester_id INT NOT NULL,           -- Foreign key referencing the user requesting feedback
--     requestee_id INT NOT NULL,           -- Foreign key referencing the user being requested to provide feedback
--     message TEXT,                        -- Optional message included in the feedback request
--     status VARCHAR(20) DEFAULT 'pending',-- Status of the request (pending or completed)
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the request was made
--     FOREIGN KEY (requester_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the requester is deleted, delete the request
--     FOREIGN KEY (requestee_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the requestee is deleted, delete the request
-- );
--
-- -- Create Goals Table
-- -- This table stores goals set by employees themselves or assigned by managers.
-- -- A goal is tied to a user and can be tracked by its status and due date.
-- CREATE TABLE Goals (
--     goal_id SERIAL PRIMARY KEY,          -- Unique identifier for each goal
--     user_id INT NOT NULL,                -- Foreign key referencing the employee setting or assigned the goal
--     assigned_by INT DEFAULT NULL,        -- Foreign key referencing the manager who set the goal (NULL if self-set)
--     goal_name VARCHAR(255) NOT NULL,     -- Name/title of the goal
--     description TEXT NOT NULL,           -- Detailed description of the goal
--     status VARCHAR(20) DEFAULT 'pending',-- Status of the goal (pending, in progress, completed)
--     due_date DATE,                       -- Due date for the goal's completion
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the goal was created
--     FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the user is deleted, delete the goal
--     FOREIGN KEY (assigned_by) REFERENCES User(user_id) ON DELETE SET NULL -- If the manager is deleted, set assigned_by to NULL
-- );
--
-- -- Create PIP (Performance Improvement Plan) Table
-- -- This table tracks performance improvement plans (PIPs) set by managers for employees.
-- -- PIPs include objectives, timeline, support, and the outcome (e.g., improvement, partial progress, failure).
-- CREATE TABLE PIP (
--     pip_id SERIAL PRIMARY KEY,           -- Unique identifier for the PIP
--     employee_id INT NOT NULL,            -- Foreign key referencing the employee assigned the PIP
--     manager_id INT NOT NULL,             -- Foreign key referencing the manager who set the PIP
--     objectives TEXT NOT NULL,            -- Detailed objectives for the employee to meet
--     timeline VARCHAR(50),                -- Timeline for the improvement (e.g., "30 days", "60 days")
--     support TEXT,                        -- Support provided (e.g., training, mentorship)
--     outcome VARCHAR(20) DEFAULT 'improvement', -- Outcome of the PIP (e.g., improvement, partial progress, failure)
--     status VARCHAR(20) DEFAULT 'active', -- Status of the PIP (active, completed)
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the PIP was created
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the PIP was last updated
--     FOREIGN KEY (employee_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the employee is deleted, delete the PIP
--     FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the manager is deleted, delete the PIP
-- );


-- Drop tables and types if they exist, for a clean start
-- DROP TABLE IF EXISTS PIP CASCADE;
-- DROP TABLE IF EXISTS Goals CASCADE;
-- DROP TABLE IF EXISTS Feedback_Request CASCADE;
-- DROP TABLE IF EXISTS Feedback CASCADE;
-- DROP TABLE IF EXISTS Profile CASCADE;
-- DROP TABLE IF EXISTS Tasks CASCADE;
-- DROP TABLE IF EXISTS User CASCADE;
-- DROP TYPE IF EXISTS user_role_enum CASCADE;
--
-- -- Create ENUM type for user roles
-- -- This ENUM type defines the user roles available in the system.
-- -- ROLE_EMPLOYEE: Represents an employee
-- -- ROLE_MANAGER: Represents a manager
-- -- ROLE_ADMIN: Represents an admin
-- CREATE TYPE user_role_enum AS ENUM ('ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN');
--
-- -- Create User Table
-- -- This table stores core user information such as email, password, and role.
-- -- Each user is assigned a role using the ENUM type user_role_enum.
-- CREATE TABLE User (
--     user_id SERIAL PRIMARY KEY,          -- Unique identifier for each user
--     email VARCHAR(150) UNIQUE NOT NULL,  -- User's email (used for login)
--     password_hash VARCHAR(255) NOT NULL, -- Encrypted password for security
--     role user_role_enum NOT NULL,        -- ENUM to indicate user role (manager, employee, admin)
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp for when the user account was created
-- );
--
-- -- Create Profile Table
-- -- This table holds additional user details such as first name, last name, job title, and manager relationships.
-- -- Each user has a one-to-one relationship with their profile.
-- CREATE TABLE Profile (
--     profile_id SERIAL PRIMARY KEY,       -- Unique identifier for the profile
--     user_id INT NOT NULL UNIQUE,         -- Foreign key linking to the User table (each user has one profile)
--     first_name VARCHAR(100) NOT NULL,    -- User's first name
--     last_name VARCHAR(100) NOT NULL,     -- User's last name
--     job_title VARCHAR(150),              -- User's job title (optional)
--     manager_id INT DEFAULT NULL,         -- Foreign key linking to the user's manager (NULL for managers)
--     FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the user is deleted, the profile is deleted
--     FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE SET NULL -- If the manager is deleted, set manager_id to NULL
-- );
--
-- -- Create Feedback Table
-- -- This table stores feedback exchanged between users.
-- -- Users can give and receive feedback, and feedback is tied to both the giver and the receiver.
-- CREATE TABLE Feedback (
--     feedback_id SERIAL PRIMARY KEY,      -- Unique identifier for each feedback entry
--     giver_id INT NOT NULL,               -- Foreign key referencing the user giving the feedback
--     receiver_id INT NOT NULL,            -- Foreign key referencing the user receiving the feedback
--     content TEXT NOT NULL,               -- The feedback content/message
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the feedback was given
--     FOREIGN KEY (giver_id) REFERENCES User(user_id) ON DELETE CASCADE,  -- If the giver is deleted, delete their feedback
--     FOREIGN KEY (receiver_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the receiver is deleted, delete related feedback
-- );
--
-- -- Create Feedback_Request Table
-- -- This table stores feedback requests made by users.
-- -- Users can request feedback from others, and the request status is tracked (pending or completed).
-- CREATE TABLE Feedback_Request (
--     request_id SERIAL PRIMARY KEY,       -- Unique identifier for each feedback request
--     requester_id INT NOT NULL,           -- Foreign key referencing the user requesting feedback
--     requestee_id INT NOT NULL,           -- Foreign key referencing the user being requested to provide feedback
--     message TEXT,                        -- Optional message included in the feedback request
--     status VARCHAR(20) DEFAULT 'pending',-- Status of the request (pending or completed)
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the request was made
--     FOREIGN KEY (requester_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the requester is deleted, delete the request
--     FOREIGN KEY (requestee_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the requestee is deleted, delete the request
-- );
--
-- -- Create Goals Table
-- -- This table stores goals set by employees themselves or assigned by managers.
-- -- A goal is tied to a user and can be tracked by its status and due date.
-- CREATE TABLE Goals (
--     goal_id SERIAL PRIMARY KEY,          -- Unique identifier for each goal
--     user_id INT NOT NULL,                -- Foreign key referencing the employee setting or assigned the goal
--     assigned_by INT DEFAULT NULL,        -- Foreign key referencing the manager who set the goal (NULL if self-set)
--     goal_name VARCHAR(255) NOT NULL,     -- Name/title of the goal
--     description TEXT NOT NULL,           -- Detailed description of the goal
--     progress INT DEFAULT 0,              -- Progress percentage based on tasks completion
--     status VARCHAR(20) DEFAULT 'pending',-- Status of the goal (pending, in progress, completed)
--     due_date DATE,                       -- Due date for the goal's completion
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the goal was created
--     FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the user is deleted, delete the goal
--     FOREIGN KEY (assigned_by) REFERENCES User(user_id) ON DELETE SET NULL -- If the manager is deleted, set assigned_by to NULL
-- );
--
-- -- Create PIP (Performance Improvement Plan) Table
-- -- This table tracks performance improvement plans (PIPs) set by managers for employees.
-- -- PIPs include objectives, timeline, support, and the outcome (e.g., improvement, partial progress, failure).
-- CREATE TABLE PIP (
--     pip_id SERIAL PRIMARY KEY,           -- Unique identifier for the PIP
--     employee_id INT NOT NULL,            -- Foreign key referencing the employee assigned the PIP
--     manager_id INT NOT NULL,             -- Foreign key referencing the manager who set the PIP
--     objectives TEXT NOT NULL,            -- High-level objectives for the employee to meet
--     start_date DATE NOT NULL,            -- Start date of the PIP
--     end_date DATE NOT NULL,              -- End date of the PIP
--     progress INT DEFAULT 0,              -- Progress percentage based on task completion
--     support TEXT,                        -- Support provided (e.g., training, mentorship)
--     outcome VARCHAR(20) DEFAULT 'improvement', -- Outcome of the PIP (e.g., improvement, partial progress, failure)
--     status VARCHAR(20) DEFAULT 'active', -- Status of the PIP (active, completed)
--     comments TEXT,                       -- Comments or feedback from the manager during the PIP
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the PIP was created
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the PIP was last updated
--     FOREIGN KEY (employee_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the employee is deleted, delete the PIP
--     FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE CASCADE   -- If the manager is deleted, delete the PIP
-- );
--
-- -- Generalized Tasks Table
-- -- This table handles tasks for both Goals and PIPs.
-- CREATE TABLE Tasks (
--     task_id SERIAL PRIMARY KEY,              -- Unique identifier for each task
--     related_id INT NOT NULL,                 -- Foreign key referencing either a Goal or a PIP
--     task_type VARCHAR(20) NOT NULL,          -- Specifies whether the task is for a Goal or PIP ('goal' or 'pip')
--     task_name VARCHAR(255) NOT NULL,         -- Name or description of the task
--     is_completed BOOLEAN DEFAULT FALSE,      -- Whether the task is completed
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- When the task was created
--     FOREIGN KEY (related_id) REFERENCES Goals(goal_id) ON DELETE CASCADE -- Link to Goals table if task_type is 'goal'
--     OR REFERENCES PIP(pip_id) ON DELETE CASCADE                      -- Link to PIP table if task_type is 'pip'
-- );


-- Drop tables and types if they exist, for a clean start
DROP TABLE IF EXISTS PIP CASCADE;
DROP TABLE IF EXISTS Goals CASCADE;
DROP TABLE IF EXISTS Feedback_Request CASCADE;
DROP TABLE IF EXISTS Feedback CASCADE;
DROP TABLE IF EXISTS Profile CASCADE;
DROP TABLE IF EXISTS Tasks CASCADE;
DROP TABLE IF EXISTS User CASCADE;
DROP TYPE IF EXISTS user_role_enum CASCADE;

-- Create ENUM type for user roles
CREATE TYPE user_role_enum AS ENUM ('ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN');

-- Create User Table
CREATE TABLE User (
    user_id SERIAL PRIMARY KEY,          -- Unique identifier for each user
    email VARCHAR(150) UNIQUE NOT NULL,  -- User's email (used for login)
    password_hash VARCHAR(255) NOT NULL, -- Encrypted password for security
    role user_role_enum NOT NULL,        -- ENUM to indicate user role (manager, employee, admin)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp for when the user account was created
);

-- Create Profile Table
CREATE TABLE Profile (
    profile_id SERIAL PRIMARY KEY,       -- Unique identifier for the profile
    user_id INT NOT NULL UNIQUE,         -- Foreign key linking to the User table (each user has one profile)
    first_name VARCHAR(100) NOT NULL,    -- User's first name
    last_name VARCHAR(100) NOT NULL,     -- User's last name
    job_title VARCHAR(150),              -- User's job title (optional)
    manager_id INT DEFAULT NULL,         -- Foreign key linking to the user's manager (NULL for managers)
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the user is deleted, the profile is deleted
    FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE SET NULL -- If the manager is deleted, set manager_id to NULL
);

-- Updated Feedback Table with shared feedback tracking
CREATE TABLE Feedback (
    feedback_id SERIAL PRIMARY KEY,      -- Unique identifier for each feedback entry
    giver_id INT NOT NULL,               -- Foreign key referencing the user giving the feedback
    receiver_id INT NOT NULL,            -- Foreign key referencing the user receiving the feedback
    content TEXT NOT NULL,               -- The feedback content/message
    is_shared_with_manager BOOLEAN DEFAULT FALSE, -- Indicates if feedback has been shared with the manager
    shared_at TIMESTAMP,                 -- Timestamp for when the feedback was shared
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the feedback was given
    FOREIGN KEY (giver_id) REFERENCES User(user_id) ON DELETE CASCADE,  -- If the giver is deleted, delete their feedback
    FOREIGN KEY (receiver_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the receiver is deleted, delete related feedback
);

-- Create Feedback_Request Table
CREATE TABLE Feedback_Request (
    request_id SERIAL PRIMARY KEY,       -- Unique identifier for each feedback request
    requester_id INT NOT NULL,           -- Foreign key referencing the user requesting feedback
    requestee_id INT NOT NULL,           -- Foreign key referencing the user being requested to provide feedback
    message TEXT,                        -- Optional message included in the feedback request
    status VARCHAR(20) DEFAULT 'pending',-- Status of the request (pending or completed)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp when the request was made
    FOREIGN KEY (requester_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the requester is deleted, delete the request
    FOREIGN KEY (requestee_id) REFERENCES User(user_id) ON DELETE CASCADE -- If the requestee is deleted, delete the request
);

-- Create Goals Table
CREATE TABLE Goals (
    goal_id SERIAL PRIMARY KEY,          -- Unique identifier for each goal
    user_id INT NOT NULL,                -- Foreign key referencing the employee setting or assigned the goal
    assigned_by INT DEFAULT NULL,        -- Foreign key referencing the manager who set the goal (NULL if self-set)
    goal_name VARCHAR(255) NOT NULL,     -- Name/title of the goal
    description TEXT NOT NULL,           -- Detailed description of the goal
    progress INT DEFAULT 0,              -- Progress percentage based on tasks completion
    status VARCHAR(20) DEFAULT 'pending',-- Status of the goal (pending, in progress, completed)
    due_date DATE,                       -- Due date for the goal's completion
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the goal was created
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the user is deleted, delete the goal
    FOREIGN KEY (assigned_by) REFERENCES User(user_id) ON DELETE SET NULL -- If the manager is deleted, set assigned_by to NULL
);

-- Create PIP (Performance Improvement Plan) Table
CREATE TABLE PIP (
    pip_id SERIAL PRIMARY KEY,           -- Unique identifier for the PIP
    employee_id INT NOT NULL,            -- Foreign key referencing the employee assigned the PIP
    manager_id INT NOT NULL,             -- Foreign key referencing the manager who set the PIP
    title TEXT NOT NULL,            -- High-level objectives for the employee to meet
    start_date DATE NOT NULL,            -- Start date of the PIP
    end_date DATE NOT NULL,              -- End date of the PIP
    progress INT DEFAULT 0,              -- Progress percentage based on task completion
    support TEXT,                        -- Support provided (e.g., training, mentorship)
    outcome VARCHAR(20) DEFAULT 'improvement', -- Outcome of the PIP (e.g., improvement, partial progress, failure)
    status VARCHAR(20) DEFAULT 'active', -- Status of the PIP (active, completed)
    comments TEXT,                       -- Comments or feedback from the manager during the PIP
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the PIP was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the PIP was last updated
    FOREIGN KEY (employee_id) REFERENCES User(user_id) ON DELETE CASCADE, -- If the employee is deleted, delete the PIP
    FOREIGN KEY (manager_id) REFERENCES User(user_id) ON DELETE CASCADE   -- If the manager is deleted, delete the PIP
);

-- Generalized Tasks Table for Goals and PIPs
CREATE TABLE Tasks (
    task_id SERIAL PRIMARY KEY,              -- Unique identifier for each task
    goal_id INT DEFAULT NULL,                -- Foreign key referencing the goal (NULL if the task is for a PIP)
    pip_id INT DEFAULT NULL,                 -- Foreign key referencing the PIP (NULL if the task is for a goal)
    task_name VARCHAR(255) NOT NULL,         -- Name or description of the task
    is_completed BOOLEAN DEFAULT FALSE  ,      -- Whether the task is completed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- When the task was created
    FOREIGN KEY (goal_id) REFERENCES Goals(goal_id) ON DELETE CASCADE,  -- Link to Goals if the task is for a goal
    FOREIGN KEY (pip_id) REFERENCES PIP(pip_id) ON DELETE CASCADE       -- Link to PIP if the task is for a PIP
);


