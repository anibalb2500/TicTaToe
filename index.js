import { Server } from "socket.io";

const io = new Server({ /* options */ });

const messages = [];

io.on("connection", (socket) => {
  console.log("user connected")

  socket.on("newMessage", (message) => {
    console.log("new message", message);
    // Store the new message
    messages.push(message);

    // Broadcast the new message to all connected clients
    io.emit("newMessage", message);
  });
});

io.on("disconnect", (socket) => {});

const port = process.env.PORT || 3000;

io.listen(port);