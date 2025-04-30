#!/bin/bash

# Database Configuration (Transaction Pooler for IPv4 compatibility)
export DATABASE_URL="jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:6543/postgres"
export DATABASE_USERNAME="postgres.nhytirftmsajcthvuqrx"
export DATABASE_PASSWORD="Vacationvideos123!"

# Server Configuration
export PORT=8080

# Email Configuration
export SMTP_HOST="smtp.gmail.com"
export SMTP_PORT=587
export SMTP_USERNAME="kavishsoningra009@gmail.com"
export SMTP_PASSWORD="ozpfvucpspqvmvni"

# Frontend URL
export FRONTEND_URL="http://localhost:5173"

# CORS Configuration
export CORS_ALLOWED_ORIGINS="http://localhost:5173"

echo "Environment variables set for Supabase database connection (transaction pooler)" 