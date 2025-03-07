# FrogCrew - TCU Crew Scheduling System

FrogCrew is a comprehensive crew scheduling system designed for TCU sporting events. It automates the process of gathering crew availability, constructing game-day crew lists, and managing crew members.

## Project Structure

The project is structured as follows:

- `client/` - Vue.js frontend application
- `server/` - C# backend API (to be implemented)

## Features

- **User Management**: Create and manage crew member profiles
- **Availability & Scheduling**: Submit availability, create game schedules, assign crew
- **Notifications**: Notify crew members of assignments and schedule changes
- **Position & Template Management**: Manage positions and crew list templates
- **Reports & Financials**: Generate financial, position, and individual crew member reports

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm (v6 or higher)
- .NET Core SDK (v6 or higher) for the backend

### Frontend Setup

1. Navigate to the client directory:
   ```
   cd client
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Run the development server:
   ```
   npm run dev
   ```

4. Access the application at http://localhost:3000

### Backend Setup (To be implemented)

The backend will be implemented using C# with an MS SQL database.

## User Roles

- **Admin**: Complete control—adds games, schedules crew, generates reports, etc.
- **Crew Member**: Submits availability, views schedule, requests shift exchanges.

## Development Roadmap

1. **Phase 1**: User Management & Authentication
   - User registration and login
   - Profile management

2. **Phase 2**: Availability & Basic Scheduling
   - Crew members can submit availability
   - Admins can view availability and create schedules

3. **Phase 3**: Advanced Scheduling & Notifications
   - Shift exchange requests
   - Notification system

4. **Phase 4**: Position & Template Management
   - Create and manage positions
   - Create and manage crew list templates

5. **Phase 5**: Reporting
   - Financial reports
   - Position reports
   - Individual crew member reports

## Mock Login Credentials

For testing purposes, you can use the following credentials:

- **Admin**:
  - Email: admin@example.com
  - Password: password

- **Crew Member**:
  - Email: crew@example.com
  - Password: password

## License

This project is proprietary and confidential. # FrogCrew
