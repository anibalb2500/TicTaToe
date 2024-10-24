# Tic-Tac-Toe WebSocket App Monorepo

## About this Repo

This monorepo contains the code for a multiplayer Tic-Tac-Toe game built using an Android app and a WebSocket server. The Android application, developed with Jetpack Compose in Kotlin, allows users to log in with a username, create or join game rooms, and play Tic-Tac-Toe against another user. The WebSocket server, built using Socket.IO and written in JavaScript, manages real-time communication between clients, handling the creation of game rooms and coordinating game states.

### Features
- **User Login**: Users log in to the WebSocket server with a unique username.
- **Room Management**: Users can either create or join a game room to play Tic-Tac-Toe. Each room supports up to two players.
- **Real-Time Game**: The game state is updated in real-time via WebSocket communication, ensuring a smooth multiplayer experience.

### Project Structure
- **Android App**: Written in Kotlin and built with Jetpack Compose, this app handles user authentication, room creation, joining games, and the Tic-Tac-Toe gameplay interface.
- **WebSocket Server**: A Node.js server using Socket.IO to facilitate real-time communication between clients, handling room creation and game state synchronization.

## Tech Stack

### Android App:
- **Language**: Kotlin
- **Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Communication**: WebSocket client for real-time interaction with the server

### WebSocket Server:
- **Language**: JavaScript
- **Framework**: Socket.IO for WebSocket communication
- **Server Environment**: Node.js
