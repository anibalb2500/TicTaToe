import { Server } from "socket.io";
import Lobby from "./lobby.js";
import User from "./user.js";
import UserTracker from "./userTracker.js";
import Room from "./room.js";

const io = new Server({ /* options */ });

// Update the messages variable to be an array of arrays of nullable Player
// Initially:
//     messages[0] = [[null, null, null]], messages[1] = [[null, null, null]], and messages[2] = [[null, null, null]]
const coordinates = Array(3).fill().map(() => [null, null, null]);

// Track the users
let userTracker = new UserTracker();
let lobby = new Lobby();

// This will limit us to how many rooms can be created. Look into improving room creation. 
let rooms = [];
let roomIdCounter = 0; 

io.on("connection", (socket) => {
  console.log("New client connected");

  socket.on("login", (message) => {
    handleLogin(socket, message);
  })

  socket.on("createNewGameRoom", (message) => {
    createNewGameRoom(socket, message);
  })

  socket.on("joinGameRoom", (message) => {
    joinGameRoom(socket, message);
  });

  socket.on("getRoomData", (message) => {
    getRoomData(socket, message);
  });
 
  socket.on("newMove", (message) => {
    handleNewMove(socket, message);
  });
});

const port = 3000;//process.env.PORT || 3000;

io.listen(port);

function handleLogin(socket, message) {
  const { username } = message;

  const userExists = userTracker.getUsers().some(user => user.getUsername() === username);
  if (userExists) {
    socket.emit("loginFailed");
    console.log(`User ${username} already exists`);
    return;
  } else {
    console.log(`User ${username} has logged in`);
    const user = new User(socket.id, username, null);

    // Add the user to the users array
    userTracker.addUser(user);

    // Add user to the lobby
    lobby.addUser(user);
  }

  socket.emit("loginSuccess", { username });
}

function createNewGameRoom(socket, message) {
  console.log("Creating new game room");
  try {
    const { username } = message;
    userTracker.getUsers().forEach(element => {
      console.log(element);
    });
    const user = userTracker.getUserByName(username);
    console.log(user);
  
    const roomId = `room-${roomIdCounter++}`;
    const room = new Room();
    room.roomId = roomId;
    rooms.push(room);
    room.addUser(user);
    rooms[roomId] = room;
    console.log('1');
    const player = room.getPlayerByUser(user);

    console.log('9 - ${player}')
  
    if (player === null) {
      console.log('2');
      socket.emit("gameRoomCreationFailure", { message: "Unexpected error" });
    } else {
      console.log('gameRoomCreationSuccess');
      socket.emit("gameRoomCreationSuccess", { roomId, player: player }); 
    }
  } catch (error) {
    console.log('4');
    console.log(error.message);
    socket.emit("gameRoomCreationFailure", { message: error.message });
  }
}


function joinGameRoom(socket, message) {
  console.log("Joining game room");
  try {
    const { username, roomId } = message;

    // Find the user by username
    const user = userTracker.getUserByName(username);
    if (!user) {
      console.log('user is null');
      socket.emit("joinRoomFailure", { message: "User not found" });
      return;
    }

    // Check if the room exists
    const room = rooms[roomId];
    console.log(rooms);
    if (!room) {
      console.log('room is null');
      socket.emit("joinRoomFailure", { message: "Room not found" });
      return;
    }

    // Add the user to the room
    room.addUser(user); // Assuming player type will be assigned later

    // Get the player type for the user
    const player = room.getPlayerByUser(user);

    if (player === null) {
      console.log('player is null');
      socket.emit("joinRoomFailure", { message: "Unexpected error" });
    } else {
      console.log(`User ${username} has joined room ${roomId}`);
      socket.join(roomId);
      io.emit("joinRoomSuccess", { roomId, player });
    }
  } catch (error) {
    console.log(error.message);
    socket.emit("joinRoomFailure", { message: error.message });
  }
}


function getRoomData(socket, message) {
  const { username, roomId } = message;
  const user = userTracker.getUserByName(username);

  // Find the room with the matching roomId
  const room = rooms[roomId];
  if (!room) {
    socket.emit("getRoomStateFailure", { message: "Room not found" });
    return;
  }

  // Emit the coordinates value of the room
  socket.emit("getRoomStateSuccess", { 
    coordinates: room.getCoordinates(), 
    isFull: room.isFull(), 
    yourPlayer: room.getPlayerByUser(user),
    currentPlayer: room.getCurrentPlayer()
  });
}

function handleNewMove(socket, message) {
  const { roomId, player, x, y } = message;
  
  try {
    console.log("player value", player)
    var validPlayer = player == "X" || player == "O" 
    console.log("Params", player, x, y)

    if (validPlayer && Number.isInteger(x) && Number.isInteger(y)) {
      console.log("failed check")
    
      const room = rooms[roomId];
      if (!room) {
        socket.emit("newMoveFailure", { message: "Room not found" });
        return;
      }
  
      // Process the message
      console.log(`Received message: ${message}`);
      // At coordinates[x][y] set the value to player - Maybe add a check to see if the coordinates are valid
      const success = room.setCoordinate(x, y, player)
      // if (!success) {
      //   console.log("newMoveFailure - failed to set coordinates")
      //   socket.emit("newMoveFailure", { message: "Failed to set coordinate" });
      //   return;
      // }
      
      room.updateCurrentPlayer();
      
      io.emit("newMoveSucces", {
        roomId: roomId,
        coordinates: room.getCoordinates(),
        currentPlayer: room.getCurrentPlayer()
      });
    } else {
        console.error("Invalid move");
        socket.emit("newMoveFailure", { message: "Invalid move" });
    }
  } catch (error) {
    console.log("handleNewMove error", error)
    socket.emit("newMoveFailure", { message: "Error occured ${error}" });
  }
}


// io.on("connection", (socket) => {

//     console.log("New client connected");

//     // Check if the room is full
//     if (users.length >= 2) {
//         console.log("Room is full");
//         socket.emit("roomFull");
//         socket.disconnect();
//         return;
//     }

//     // Assign player
//     const player = users.length === 0 ? Player.X : Player.O;
//     users.push({ id: socket.id, player });

//     if (users.length == 2) {
//         console.log("WE CAN START THE GAME");
//         // Randomly emit X or O to the clients
//         const randomIndex = Math.floor(Math.random() * 2);
//         const startingPlayer = randomIndex === 0 ? Player.X : Player.O;
//         io.emit("startGame", { player: startingPlayer });
//       }

//     // Emit the initial state of the game to the client
//     socket.emit("initialState", { 
//         coordinates,
//         player
//      } );

//   socket.on("newMove", (message) => {
//     const { player, x, y } = message;
//     console.log("New move received ${message}");

//     if (player === Player.X || player === Player.O && Number.isInteger(x) && Number.isInteger(y)) {
//         // Process the message
//         console.log(`Received message: ${message}`);
//         // At coordinates[x][y] set the value to player - Maybe add a check to see if the coordinates are valid
//         coordinates[x][y] = player;
//         // Emit the updated coordinates to all clients - Maybe the DS update was successful
//         io.emit("successfullyAdded", message);
//       } else {
//         console.error("Invalid message type");
//       }
//   });
// });

// io.on("disconnect", (socket) => {
//     console.log("Client disconnected");
//     users = users.filter(user => user.id !== socket.id);
// });
