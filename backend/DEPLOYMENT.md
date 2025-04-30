# FrogCrew Deployment Guide

## Prerequisites

- Java 17 or higher
- PostgreSQL database
- SMTP email service (e.g., SendGrid, Amazon SES, or Mailgun)
- Node.js and npm (for frontend)

## Environment Variables

Create a `.env` file in your production environment with the following variables:

```bash
# Database Configuration
DATABASE_URL=jdbc:postgresql://your-database-host:5432/your-database-name
DATABASE_USERNAME=your-database-username
DATABASE_PASSWORD=your-database-password

# Email Configuration
SMTP_HOST=your-smtp-host
SMTP_PORT=587
SMTP_USERNAME=your-smtp-username
SMTP_PASSWORD=your-smtp-password

# Frontend Configuration
FRONTEND_URL=https://your-frontend-domain.com

# CORS Configuration
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com

# Server Configuration
PORT=8080
```

## Backend Deployment

1. Build the application:

```bash
./mvnw clean package -DskipTests
```

2. Run the application in production mode:

```bash
java -jar target/frogcrew.jar --spring.profiles.active=prod
```

## Frontend Deployment

1. Build the frontend:

```bash
cd frontend
npm install
npm run build
```

2. Deploy the built files to your web server or hosting service.

## Email Service Setup

1. Sign up for an email service provider (recommended: SendGrid, Amazon SES, or Mailgun)
2. Configure your domain's DNS settings for email authentication (SPF, DKIM, DMARC)
3. Update the SMTP configuration in your environment variables

## Database Setup

1. Create a production PostgreSQL database
2. Run the initial migrations:

```bash
./mvnw flyway:migrate
```

## Security Considerations

1. Ensure all sensitive information is stored in environment variables
2. Set up SSL/TLS certificates for your domain
3. Configure proper firewall rules
4. Set up monitoring and logging
5. Implement rate limiting for API endpoints
6. Regularly update dependencies and security patches

## Monitoring

1. Set up application monitoring (e.g., Spring Boot Actuator)
2. Configure logging to a centralized service
3. Set up alerts for critical errors

## Backup Strategy

1. Set up regular database backups
2. Implement a disaster recovery plan
3. Test backup restoration periodically

## Scaling Considerations

1. Configure connection pooling appropriately
2. Set up load balancing if needed
3. Consider implementing caching where appropriate
4. Monitor resource usage and scale accordingly
