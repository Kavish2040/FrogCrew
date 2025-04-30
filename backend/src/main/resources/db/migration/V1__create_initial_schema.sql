-- Create enum types
CREATE TYPE user_role AS ENUM ('ADMIN', 'CREW');
CREATE TYPE position AS ENUM (
    'ADMINISTRATOR',
    'PRODUCER',
    'DIRECTOR',
    'TECHNICAL_DIRECTOR',
    'CAMERA',
    'AUDIO_A1',
    'AUDIO_A2',
    'GFX',
    'BUG_OPERATOR',
    'EVS_LEAD',
    'EVS_RO',
    'EIC',
    'UTILITY',
    'TOC'
);

-- Create frog_crew_users table
CREATE TABLE frog_crew_users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    role user_role NOT NULL
);

-- Create games table
CREATE TABLE games (
    id UUID PRIMARY KEY,
    sport VARCHAR(255) NOT NULL,
    opponent VARCHAR(255) NOT NULL,
    event_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    venue VARCHAR(255) NOT NULL,
    network VARCHAR(255) NOT NULL
);

-- Create crew_assignments table
CREATE TABLE crew_assignments (
    id UUID PRIMARY KEY,
    game_id UUID NOT NULL REFERENCES games(id),
    user_id UUID NOT NULL REFERENCES frog_crew_users(id),
    position position NOT NULL,
    report_time TIME NOT NULL,
    UNIQUE(game_id, user_id, position)
);

-- Create user_qualified_positions table (for the Set<Position> in FrogCrewUser)
CREATE TABLE user_qualified_positions (
    user_id UUID NOT NULL REFERENCES frog_crew_users(id),
    position position NOT NULL,
    PRIMARY KEY (user_id, position)
); 