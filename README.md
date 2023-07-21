# Electronic Platform for Transportation
> ℹ️ This project was carried out collaboratively as part of a _Designing IT Systems_ course. This repository contains partial implementation (as required by the syllabus).

The aim of the project is to deliver an IT system that meets the social expectations of current and potential users of public transportation. This will be achieved through the following objectives:
- Increasing access to passenger information throughout all stages of the journey, from recognizing the transportation need, waiting at the stop, to the actual travel.
- Enabling ticket purchase at the same price as in physical ticket sales points, outside their opening hours, or at significant distances from them.

The project includes the following components:
- ticket sales application,
- ticket inspection application,
- schedule creation application,
- real-time vehicle location monitoring.

## User stories
Checkmarks indicate implemented features.
#### Authentication
1. ✅ As a user, I want to have the ability to register and log in to access the system.
2. ◻️ As a user, I want to have the option to delete my account from the system.
#### Tickets purchase
3. ✅ As a passenger, I want to be able to browse the ticket offers to make the best choice for my travel needs.
4. ◻️ As a passenger, I want to be able to top up my wallet through a payment operator to purchase tickets.
5. ✅ As a passenger, I want to be able to check my current wallet balance to know if I have enough funds for future ticket purchases.
6. ✅ As a passenger, I want to be able to buy one-time and time-based tickets to be able to punch them later.
7. ◻️ As a passenger, I want to be able to purchase periodical tickets to travel the same route multiple times at a discounted price.
8. ✅ As a passenger, I want to be able to review my purchased tickets to keep track of my travel needs.
9. ✅ As a passenger, I want to be able to punch my purchased tickets to be eligible for transportation.
10. ◻️ As a passenger, I want to be able to view my punched tickets to determine the historical cost of my travels.
11. ◻️ As a passenger, I want to be able to review my periodical tickets to remember their expiration dates and the routes they are valid for.

#### Tickets inspection
12. ◻️ As an inspector, I want to be able to check the validity of passengers' tickets using QR codes to quickly verify their eligibility for public transportation.
13. ✅ As an inspector, I want to be able to check the validity of passengers' tickets using ticket identifiers in case QR code scanning fails.
14. ✅ As a passenger, I want to be able to present my active tickets to prove my eligibility for travel.

#### Schedule Management
15. ✅ As a passenger, I want to be able to view the timetable based on lines and stops to plan my journey.
16. ◻️ As a planner, I want to have the ability to preview and modify existing schedules to ensure their accuracy.

#### Fleet Management
17. ✅ As a passenger, I want to be able to check the current estimated arrival times of buses to be informed about potential delays.
18. ✅ As a planner, I want to be able to check the current locations of vehicles to diagnose the causes of delays.

#### Offer Management
19. ◻️ As a moderator, I want to be able to review the current ticket offerings to verify their correctness.
20. ◻️ As a moderator, I want to have the ability to edit the current ticket offerings to customize the ticket options.

## Technology used
- Kotlin
- Spring

## Local deployment
### Prerequisites
- Computer supporitng amd64 or arm64/v8 instruction set.
- Installed Docker Desktop or Docker Engine on Linux.
- Downloaded source code.<br>
> ⚠️ Using default git conversion of line endings for Windows (LF→CRLF) may hinder compilation (maven scripts may not work).

### Run the environment
1. Run with `docker compose up --build`.
2. The application will be available at [localhost](http://localhost).

## Some screenshots

