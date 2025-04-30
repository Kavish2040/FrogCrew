-- Create positions
INSERT INTO positions (id, code, display_name, default_pay_rate, is_active) 
VALUES 
(gen_random_uuid(), 'ADMINISTRATOR', 'Administrator', 0.00, true),
(gen_random_uuid(), 'DIRECTOR', 'Director', 0.00, true),
(gen_random_uuid(), 'CAMERA', 'Camera', 0.00, true);

-- Create admin user with bcrypt encoded password '123456'
INSERT INTO frog_crew_users (id, first_name, last_name, email, password, phone_number, role, is_active) 
VALUES (
  gen_random_uuid(), 
  'Madhavam', 
  'Shahi', 
  'madhavam@test.com', 
  '$2a$10$PvSfj5xI6mFdxxU/vB5jB.NXmr2VDhP2kIuVJKW3PF9qiKpqK3oVC', -- encoded '123456'
  '014-988-1942', 
  'ADMIN', 
  true
);

-- Link user to position
-- We need to fetch the IDs dynamically because we generated them as UUIDs
INSERT INTO user_positions (user_id, position_id)
SELECT u.id, p.id
FROM frog_crew_users u, positions p
WHERE u.email = 'madhavam@test.com' AND p.code = 'ADMINISTRATOR'; 