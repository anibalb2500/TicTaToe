import { Server } from "socket.io";
import Lobby from "./lobby.js";
import User from "./user.js";
import UserTracker from "./userTracker.js";

const io = new Server({ /* options */ });

// Update the messages variable to be an array of arrays of nullable Player
// Initially:
//     messages[0] = [[null, null, null]], messages[1] = [[null, null, null]], and messages[2] = [[null, null, null]]
const coordinates = Array(3).fill().map(() => [null, null, null]);

// Track the users
let users = new UserTracker();
let lobby = new Lobby();


io.on("connection", (socket) => {
  console.log("New client connected");

  socket.on("login", (message) => {
    handleLogin(socket, message);
  })

  
});

const port = 3000;//process.env.PORT || 3000;

io.listen(port);

function handleLogin(socket, message) {
  const { username } = message;

  const userExists = users.getUsers().some(user => user.getUsername() === username);
  if (userExists) {
    socket.emit("loginFailed");
    console.log(`User ${username} already exists`);
    return;
  } else {
    console.log(`User ${username} has logged in`);
    const user = new User(socket.id, username, null);

    // Add the user to the users array
    users.addUser(user);

    // Add user to the lobby
    lobby.addUser(user);
  }

  socket.emit("loginSuccess", { username });
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
