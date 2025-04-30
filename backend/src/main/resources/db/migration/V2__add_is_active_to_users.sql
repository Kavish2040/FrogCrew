ALTER TABLE frog_crew_users ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE;
UPDATE frog_crew_users SET is_active = TRUE WHERE is_active IS NULL; 